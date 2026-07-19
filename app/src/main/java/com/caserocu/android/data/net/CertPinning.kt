package com.caserocu.android.data.net

import android.content.Context
import java.io.IOException
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

/**
 * Builds a TLS stack that trusts ONLY the bundled `casero.rem.cu` certificate.
 *
 * The portal serves a certificate that fails standard chain validation
 * (`net::ERR_CERT_AUTHORITY_INVALID`). Rather than disabling validation globally
 * (which would allow any MITM), we pin the known certificate: place it at
 * `app/src/main/assets/casero_rem_cu.cer`. If it is missing the client fails
 * closed with [CertificateNotBundledException].
 */
object CertPinning {

    private const val CERT_ASSET = "casero_rem_cu.cer"

    data class Pinned(val socketFactory: SSLSocketFactory, val trustManager: X509TrustManager)

    fun build(context: Context): Pinned {
        val certificate = loadCertificate(context)

        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
            load(null, null)
            setCertificateEntry("casero.rem.cu", certificate)
        }

        val trustManagerFactory =
            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()).apply {
                init(keyStore)
            }
        val trustManager = trustManagerFactory.trustManagers
            .filterIsInstance<X509TrustManager>()
            .first()

        val sslContext = SSLContext.getInstance("TLS").apply {
            init(null, arrayOf(trustManager), null)
        }
        return Pinned(sslContext.socketFactory, trustManager)
    }

    private fun loadCertificate(context: Context): X509Certificate {
        val factory = CertificateFactory.getInstance("X.509")
        return try {
            context.assets.open(CERT_ASSET).use { factory.generateCertificate(it) as X509Certificate }
        } catch (_: IOException) {
            throw CertificateNotBundledException()
        }
    }
}
