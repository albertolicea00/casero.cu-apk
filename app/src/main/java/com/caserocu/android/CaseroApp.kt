package com.caserocu.android

import android.app.Application
import com.caserocu.android.data.CaseroRepository

class CaseroApp : Application() {
    /** Process-wide repository. Built lazily so the TLS stack loads on first use. */
    val repository: CaseroRepository by lazy { CaseroRepository(this) }
}
