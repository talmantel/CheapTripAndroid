/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared.persistence

import com.squareup.sqldelight.db.SqlDriver
import ru.z8.louttsev.cheaptripmobile.persistence.LocalDb
import ru.z8.louttsev.cheaptripmobile.shared.model.DataStorage
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Path
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Route
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Route.Type
import ru.z8.louttsev.cheaptripmobile.shared.model.data.TransportationType

/**
 * Declares data storage based on local database.
 *
 * @param sqlDriver A platform-specific implementation of the SQLite driver.
 */
abstract class LocalDbStorage<T>(sqlDriver: SqlDriver) : DataStorage<T> {
    private val queries = LocalDb(sqlDriver).localDbQueries

    protected fun updateOrInsertLocation(
        location: Location,
        typeName: String,
        languageCode: String
    ) {
        val (locationId, locationName) = location

        // the method violates the expected order of the arguments
        queries.updateOrInsertLocation(locationName, locationId, typeName, languageCode)
    }

    protected fun selectLocationsByName(needle: String, limit: Long): List<Location> =
        queries.selectLocationsByName(
            needle,
            limit,
            mapper = { id, name -> Location(id, name) }
        ).executeAsList()

    protected fun updateOrInsertRoute(
        route: Route,
        fromLocationName: String,
        toLocationName: String,
        languageCode: String
    ) {
        val (type, price, duration, paths) = route
        val typeName = type.name

        queries.transaction {
            val routeId =
                // the method violates the expected order of the arguments
                updateOrInsertRouteAndGetId(
                    price,
                    duration,
                    typeName,
                    fromLocationName,
                    toLocationName,
                    languageCode
                )

            deletePathsByRouteId(routeId)

            paths.forEach { path: Path ->
                insertPath(path, routeId)
            }
        }
    }

    protected fun selectRoutesByLocations(
        fromLocationName: String,
        toLocationName: String,
        languageCode: String
    ): List<Route> =
        queries.selectRoutesByLocations(
            fromLocationName,
            toLocationName,
            languageCode,
            mapper = { id, type, price, duration ->
                Route(Type.valueOf(type), price, duration, selectPathsByRouteId(id))
            }
        ).executeAsList()

    private fun updateOrInsertRouteAndGetId(
        price: Float,
        duration: Int,
        typeName: String,
        fromLocationName: String,
        toLocationName: String,
        languageCode: String
    ): Long {
        queries.updateOrInsertRoute(
            price,
            duration,
            typeName,
            fromLocationName,
            toLocationName,
            languageCode
        )

        return queries.selectRouteId(
            typeName,
            fromLocationName,
            toLocationName,
            languageCode
        ).executeAsOne()
    }

    private fun selectRouteId(
        typeName: String,
        fromLocationName: String,
        toLocationName: String,
        languageCode: String
    ): Long =
        queries.selectRouteId(
            typeName,
            fromLocationName,
            toLocationName,
            languageCode
        ).executeAsOne()

    private fun deletePathsByRouteId(routeId: Long) {
        queries.deletePathsByRouteId(routeId)
    }

    private fun insertPath(path: Path, routeId: Long) {
        val (transportationType, price, duration, fromLocationName, toLocationName) = path
        val transportationTypeName = transportationType.name

        queries.insertPath(
            transportationTypeName,
            price,
            duration,
            fromLocationName,
            toLocationName,
            routeId
        )
    }

    private fun selectPathsByRouteId(routeId: Long): List<Path> =
        queries.selectPathsByRouteId(
            routeId,
            mapper = { transportationType,
                       price,
                       duration,
                       fromLocationName,
                       toLocationName ->
                Path(
                    TransportationType.valueOf(transportationType),
                    price,
                    duration,
                    fromLocationName,
                    toLocationName
                )
            }
        ).executeAsList()
}
