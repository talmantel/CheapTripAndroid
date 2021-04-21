/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared.model

import ru.z8.louttsev.cheaptripmobile.shared.currentLocale
import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource.ParamsBundle
import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource.ParamsBundle.Key
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location.Type
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location.Type.ALL

/**
 * Declares read-only storage of available locations.
 */
class LocationRepository(
    private val mainSource: DataSource,
    private val reserveSource: DataStorage
) {
    /**
     * Finds all locations with matching fragments in the name.
     *
     * @param needle search pattern
     * @param type narrows the search by route start/finish point
     * @param limit desired number of search results
     * @param locale search and results language
     * @return list of matching results (m.b. empty)
     */
    fun searchLocationsByName(
        needle: String,
        type: Type = ALL,
        limit: Int = 10,
        locale: Locale = currentLocale
    ): List<Location> {
        val params = ParamsBundle().apply {
            put(Key.NEEDLE, needle)
            put(Key.TYPE, type)
            put(Key.LIMIT, limit)
            put(Key.LOCALE, locale)
        }
        val result = mainSource.getLocations(params)

        return result?.also {
            reserveSource.saveLocations(result)
        } ?: reserveSource.getLocations(params)
    }
}
