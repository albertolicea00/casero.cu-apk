package com.caserocu.android.data.report

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.telephony.SmsManager

/**
 * Offline reporting channel: dial a USSD code or send an SMS to immigration.
 *
 * TODO: the exact USSD/SMS report format is not specified yet. [USSD_TEMPLATE]
 * and [SMS_DESTINATION] are placeholders — fill them from the official reporting
 * spec before shipping. Do not guess the code in production.
 *
 * Callers must hold `CALL_PHONE` (for [dialUssd]) and `SEND_SMS` (for [sendSms]);
 * the UI requests them at runtime.
 */
class UssdSmsReporter(private val context: Context) {

    /** Launches the dialer with a USSD code already running. `#` must be URL-encoded as `%23`. */
    fun dialUssd(code: String) {
        val encoded = Uri.encode(code)
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$encoded"))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun sendSms(destination: String, body: String) {
        smsManager().sendTextMessage(destination, null, body, null, null)
    }

    @Suppress("DEPRECATION")
    @SuppressLint("NewApi")
    private fun smsManager(): SmsManager =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.getSystemService(SmsManager::class.java)
        } else {
            SmsManager.getDefault()
        }

    companion object {
        /** Placeholder — replace `{passport}` etc. with the real reporting code. */
        const val USSD_TEMPLATE = "*TODO*{passport}#"

        /** Placeholder short number for SMS reports. */
        const val SMS_DESTINATION = "TODO"
    }
}
