package com.caserocu.android.data.net

import org.jsoup.Jsoup

/** Extracts the ASP.NET anti-forgery form token embedded in every portal page. */
object AntiForgery {

    fun extract(html: String): String {
        val token = Jsoup.parse(html)
            .selectFirst("input[name=__RequestVerificationToken]")
            ?.attr("value")
            .orEmpty()
        if (token.isBlank()) throw AntiForgeryTokenNotFoundException()
        return token
    }
}
