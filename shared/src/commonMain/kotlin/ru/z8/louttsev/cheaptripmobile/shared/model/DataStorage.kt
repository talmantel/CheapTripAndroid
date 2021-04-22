/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared.model

import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource.ParamsBundle
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Route

/**
 * Determines full data access logic.
 */
interface DataStorage : DataSource {
    /**
     * Keeps a list of locations.
     *
     * @param locations List of locations (m.b. empty)
     */
    fun saveLocations(locations: List<Location>)

    /**
     * Keeps a list of routes.
     *
     * @param routes List of routes (m.b. empty)
     */
    fun saveRoutes(routes: List<Route>)

    /**
     * Narrows data source result to nonnull value, excluding internal errors.
     */
    override fun getLocations(parameters: ParamsBundle): List<Location>

    /**
     * Narrows data source result to nonnull value, excluding internal errors.
     */
    override fun getRoutes(parameters: ParamsBundle): List<Route>
}
