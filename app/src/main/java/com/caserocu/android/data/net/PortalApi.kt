package com.caserocu.android.data.net

import com.caserocu.android.data.model.GuestListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

/** Portal endpoints, relative to [BASE]. */
object PortalRoutes {
    const val BASE = "https://casero.rem.cu/Plataforma/"
    const val LOGIN = "Cuenta/IniciarSesion"
    const val LOGOUT = "Cuenta/CerrarSesion"
    const val REGISTERED_GUESTS_PAGE = "Huespedes/HuespedesRegistrados"
    const val LIST_GUESTS = "Huespedes/ListarHuespedes"
    const val LIST_COMPANIONS = "Huespedes/ListarAcompannantes"
}

/**
 * Thin client over the CASERO web portal. It behaves like a browser: cookies and
 * the anti-forgery token are carried on every request, and the portal's login
 * redirect / `REDIRECT` sentinel are mapped to [SessionExpiredException].
 *
 * All calls are blocking OkHttp calls moved onto [Dispatchers.IO].
 */
class PortalApi(private val client: OkHttpClient) {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    /** Authenticate. Throws [InvalidCredentialsException] when the portal rejects the login. */
    suspend fun login(user: String, password: String) = withContext(Dispatchers.IO) {
        val token = AntiForgery.extract(getHtml(PortalRoutes.LOGIN))
        val form = FormBody.Builder()
            .add("__RequestVerificationToken", token)
            .add("User", user)
            .add("Password", password)
            .build()
        val request = Request.Builder().url(url(PortalRoutes.LOGIN)).post(form).build()
        client.newCall(request).execute().use { response ->
            when {
                // Success: the portal redirects to /Plataforma/.
                response.isRedirect -> Unit
                // The login page was re-rendered: bad credentials.
                response.code == 200 -> throw InvalidCredentialsException()
                else -> throw PortalHttpException(response.code)
            }
        }
    }

    /** List registered guests for a date range. Dates are `DD/MM/YY` as the portal expects. */
    suspend fun listGuests(
        fechaInicio: String,
        fechaFin: String,
        pageSize: Int,
        page: Int,
    ): GuestListResponse = withContext(Dispatchers.IO) {
        val token = AntiForgery.extract(getHtml(PortalRoutes.REGISTERED_GUESTS_PAGE))
        val form = FormBody.Builder()
            .add("fechaInicio", fechaInicio)
            .add("fechaFin", fechaFin)
            .add("cantMaxRegistros", pageSize.toString())
            .add("pagina", page.toString())
            .add("__RequestVerificationToken", token)
            .build()
        val body = postForJson(PortalRoutes.LIST_GUESTS, form, referer = PortalRoutes.REGISTERED_GUESTS_PAGE)
        json.decodeFromString(GuestListResponse.serializer(), body)
    }

    suspend fun signOut() = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(url(PortalRoutes.LOGOUT)).get().build()
        client.newCall(request).execute().close()
    }

    // --- helpers -----------------------------------------------------------

    private fun getHtml(path: String): String {
        val request = Request.Builder().url(url(path)).get().build()
        client.newCall(request).execute().use { response ->
            if (response.isRedirect) throw SessionExpiredException()
            if (!response.isSuccessful) throw PortalHttpException(response.code)
            return response.body?.string().orEmpty()
        }
    }

    private fun postForJson(path: String, form: RequestBody, referer: String): String {
        val request = Request.Builder()
            .url(url(path))
            .post(form)
            .header("X-Requested-With", "XMLHttpRequest")
            .header("Accept", "application/json, text/javascript, */*; q=0.01")
            .header("Referer", url(referer))
            .build()
        client.newCall(request).execute().use { response ->
            if (response.isRedirect) throw SessionExpiredException()
            if (!response.isSuccessful) throw PortalHttpException(response.code)
            val text = response.body?.string().orEmpty()
            if (isRedirectSentinel(text)) throw SessionExpiredException()
            return text
        }
    }

    /** The portal returns `{ "message": "REDIRECT" }` on AJAX calls when the session is dead. */
    private fun isRedirectSentinel(text: String): Boolean = runCatching {
        val element = json.parseToJsonElement(text)
        element is JsonObject && element["message"]?.jsonPrimitive?.contentOrNull == "REDIRECT"
    }.getOrDefault(false)

    private fun url(path: String) = PortalRoutes.BASE + path
}
