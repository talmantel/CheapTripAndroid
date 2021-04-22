/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared.model

import ru.z8.louttsev.cheaptripmobile.shared.currentLocale
import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource.*
import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource.ParamsBundle.*
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Route

/**
 * Declares read-only storage of available routes.
 */
class RouteRepository(
    private val mainSource: DataSource,
    private val reserveSource: DataStorage
) {
    /**
     * Finds possible routes between specified locations.
     *
     * @param from origin location
     * @param to destination location
     * @param locale results language
     * @return list of matching results (m.b. empty)
     */
    fun getRoutes(
        from: Location,
        to: Location,
        locale: Locale = currentLocale
    ): List<Route> {
        val params = ParamsBundle().apply {
            put(Key.FROM, from)
            put(Key.TO, to)
            put(Key.LOCALE, locale)
        }
        val result = mainSource.getRoutes(params)
        return result?.also {
            reserveSource.saveRoutes(result)
        } ?: reserveSource.getRoutes(params)
    }
}
