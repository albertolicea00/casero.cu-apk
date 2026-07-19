package com.caserocu.android.data.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant

/**
 * Decodes ASP.NET JSON dates of the form `/Date(1772600400000)/` into [Instant].
 *
 * The value is epoch milliseconds (may be negative). The portal uses
 * `-62135578800000` (.NET `DateTime.MinValue`) to mean "no date"; that decodes to
 * a valid but clearly-ancient [Instant], so callers should treat pre-epoch dates
 * as absent.
 */
object DotNetInstantSerializer : KSerializer<Instant> {

    private val PATTERN = Regex("""/Date\((-?\d+)\)/""")

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("DotNetInstant", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Instant {
        val raw = decoder.decodeString()
        val millis = PATTERN.find(raw)?.groupValues?.get(1)?.toLongOrNull() ?: 0L
        return Instant.ofEpochMilli(millis)
    }

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString("/Date(${value.toEpochMilli()})/")
    }
}
