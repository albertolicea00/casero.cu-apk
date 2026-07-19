package com.caserocu.android.data.net

/** Base type for every error surfaced by the portal client. */
sealed class PortalException(message: String, cause: Throwable? = null) : Exception(message, cause)

/** The portal answered with the `{ "message": "REDIRECT" }` session sentinel, or a 302 to login. */
class SessionExpiredException : PortalException("Portal session expired.")

/** Login POST re-rendered the login page instead of redirecting: bad credentials. */
class InvalidCredentialsException : PortalException("Invalid username or password.")

/** The anti-forgery token could not be found in the page HTML. */
class AntiForgeryTokenNotFoundException : PortalException("Anti-forgery token not found in page HTML.")

/** The pinned certificate is not bundled, so the client refuses to connect (fail closed). */
class CertificateNotBundledException : PortalException(
    "Pinned certificate not bundled. Add the casero.rem.cu certificate at " +
        "app/src/main/assets/casero_rem_cu.cer — see CLAUDE.md for how to extract it.",
)

/** Any non-success, non-redirect HTTP status from the portal. */
class PortalHttpException(val code: Int) : PortalException("Portal returned HTTP $code.")
