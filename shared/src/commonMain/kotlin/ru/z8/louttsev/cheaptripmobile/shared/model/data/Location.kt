/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared.model.data

/**
 * Declares concrete location (city).
 *
 * @param id Location ID.
 * @param name Location name.
 * @param type Location type.
 * @param locale Location's name language.
 */
data class Location(
    val id: Int,
    val name: String,
    val type: Type,
    val locale: Locale
) {
    /**
     * Declares location type in relation to route.
     */
    enum class Type {
        ALL, FROM, TO
    }
}
