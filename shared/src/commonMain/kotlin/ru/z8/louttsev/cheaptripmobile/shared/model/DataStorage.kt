/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared.model

import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Route

/**
 * Determines full data access logic.
 */
interface DataStorage : DataSource {
    fun saveLocation(location: Location)

    fun saveRoute(route: Route)
}
