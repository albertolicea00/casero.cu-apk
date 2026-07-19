package com.caserocu.android.data.net

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import java.util.concurrent.TimeUnit

/** Assembles the [OkHttpClient] the portal client uses. */
object HttpClientFactory {

    /**
     * A desktop browser User-Agent. The portal is a plain ASP.NET MVC site with no
     * bot checks, but we present a browser identity to match the pages it serves.
     */
    private const val USER_AGENT =
        "Mozilla/5.0 (Linux; Android 14) AppleWebKit/537.36 (KHTML, like Gecko) " +
            "Chrome/149.0.0.0 Mobile Safari/537.36"

    fun create(context: Context, cookieJar: PersistentCookieJar): OkHttpClient {
        val pinned = CertPinning.build(context.applicationContext)
        return OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .sslSocketFactory(pinned.socketFactory, pinned.trustManager)
            .addInterceptor(UserAgentInterceptor())
            // We detect the portal's login redirect ourselves to tell auth state apart.
            .followRedirects(false)
            .followSslRedirects(false)
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    private class UserAgentInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .header("User-Agent", USER_AGENT)
                .build()
            return chain.proceed(request)
        }
    }
}
