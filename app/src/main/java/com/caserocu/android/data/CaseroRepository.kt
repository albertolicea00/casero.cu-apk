package com.caserocu.android.data

import android.content.Context
import com.caserocu.android.data.model.Guest
import com.caserocu.android.data.net.HttpClientFactory
import com.caserocu.android.data.net.PersistentCookieJar
import com.caserocu.android.data.net.PortalApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Single entry point the UI talks to. Owns the cookie jar so it can clear the
 * portal session on sign-out.
 */
class CaseroRepository(context: Context) {

    private val cookieJar = PersistentCookieJar(context.applicationContext)
    private val api = PortalApi(HttpClientFactory.create(context, cookieJar))

    suspend fun login(user: String, password: String) = api.login(user, password)

    /** Registered guests within [from]..[to], first [pageSize] of [page]. */
    suspend fun registeredGuests(
        from: LocalDate,
        to: LocalDate,
        pageSize: Int = 20,
        page: Int = 1,
    ): List<Guest> = api.listGuests(
        fechaInicio = from.format(PORTAL_DATE),
        fechaFin = to.format(PORTAL_DATE),
        pageSize = pageSize,
        page = page,
    ).guests

    suspend fun signOut() {
        runCatching { api.signOut() }
        cookieJar.clear()
    }

    private companion object {
        /** The portal formats dates as `DD/MM/YY`. */
        val PORTAL_DATE: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")
    }
}
