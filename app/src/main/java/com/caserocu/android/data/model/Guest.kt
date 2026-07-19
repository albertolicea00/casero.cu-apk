package com.caserocu.android.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * One guest as returned by `Huespedes/ListarHuespedes`.
 *
 * Portal JSON keys are PascalCase, mapped explicitly with [SerialName]. Foreign
 * guests are identified by passport ([identificador]); nationality arrives both
 * as a numeric code ([nationalityCode]) and a display name ([nationality]).
 */
@Serializable
data class Guest(
    @SerialName("Biografico") val fullName: String = "",
    @SerialName("Identificador") val identificador: String = "",
    @SerialName("FechaEntrada")
    @Serializable(with = DotNetInstantSerializer::class) val checkIn: Instant? = null,
    @SerialName("FechaSalida")
    @Serializable(with = DotNetInstantSerializer::class) val checkOut: Instant? = null,
    @SerialName("FechaNacimiento")
    @Serializable(with = DotNetInstantSerializer::class) val birthDate: Instant? = null,
    @SerialName("Nacionalidad") val nationalityCode: String? = null,
    @SerialName("DescNacionalidad") val nationality: String? = null,
    @SerialName("Procedencia") val origin: String? = null,
    @SerialName("Sexo") val sex: String? = null,
    @SerialName("EstanciaId") val stayId: String = "",
)

/** Envelope returned by `Huespedes/ListarHuespedes`. */
@Serializable
data class GuestListResponse(
    @SerialName("Error") val error: String = "",
    @SerialName("Pagina") val page: Int = 1,
    @SerialName("Total") val total: Int = 0,
    @SerialName("ListaHuespedes") val guests: List<Guest> = emptyList(),
)
