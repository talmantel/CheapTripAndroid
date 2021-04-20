/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared.model

import ru.z8.louttsev.cheaptripmobile.shared.currentLocale
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location.Type
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location.Type.ALL

/**
 * Declares read-only storage of available locations.
 */
class LocationRepository {
    fun getLocationById(id: Int): Location {
        return null
    }

    fun searchLocationsByName(
        needle: String,
        type: Type = ALL,
        limit: Int = 10,
        locale: Locale = currentLocale
    ): List<Location> {
        return emptyList()
    }
}
