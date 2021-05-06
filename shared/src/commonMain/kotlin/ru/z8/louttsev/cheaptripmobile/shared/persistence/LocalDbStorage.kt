package ru.z8.louttsev.cheaptripmobile.shared.persistence

import com.squareup.sqldelight.db.SqlDriver
import ru.z8.louttsev.cheaptripmobile.persistence.LocalDb
import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource.ParamsBundle
import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource.ParamsBundle.Key
import ru.z8.louttsev.cheaptripmobile.shared.model.DataStorage
import ru.z8.louttsev.cheaptripmobile.shared.model.data.*
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Route.Type

/**
 * Declares data storage based on local database.
 *
 * @param sqlDriver A platform-specific implementation of the SQLite driver.
 */
abstract class LocalDbStorage<T>(sqlDriver: SqlDriver) : DataStorage<T> {
    protected val queries = LocalDb(sqlDriver).localDbQueries
}

/**
 * Declares locations storage implementation.
 */
class LocationDb(sqlDriver: SqlDriver) : LocalDbStorage<Location>(sqlDriver) {
    override fun put(data: List<Location>, parameters: ParamsBundle) {
        val type = parameters.get(Key.TYPE) as Location.Type
        val locale = parameters.get(Key.LOCALE) as Locale
        data.forEach { location: Location ->
            queries.upsertLocation(
                id = location.id,
                name = location.name,
                type = type.name,
                languageCode = locale.languageCode
            )
        }
    }

    override fun get(parameters: ParamsBundle): List<Location> {
        val needle = parameters.get(Key.NEEDLE) as String
        val limit = parameters.get(Key.LIMIT) as Long
        return queries.selectLocationsByName(
            needle = needle,
            limit = limit
        ) { id, name ->
            Location(
                id = id,
                name = name
            )
        }.executeAsList()
    }
}

/**
 * Declares routes storage implementation.
 */
class RouteDb(sqlDriver: SqlDriver) : LocalDbStorage<Route>(sqlDriver) {
    override fun put(data: List<Route>, parameters: ParamsBundle) {
        val fromLocation = parameters.get(Key.FROM) as Location
        val toLocation = parameters.get(Key.TO) as Location
        val locale = parameters.get(Key.LOCALE) as Locale
        data.forEach { route: Route ->
            queries.transaction {
                queries.upsertRoute(
                    type = route.routeType.name,
                    price = route.euroPrice,
                    duration = route.durationMinutes,
                    fromLocationName = fromLocation.name,
                    toLocationName = toLocation.name,
                    languageCode = locale.languageCode
                )
                val routeId = queries.selectRouteId(
                    type = route.routeType.name,
                    fromLocationName = fromLocation.name,
                    toLocationName = toLocation.name,
                    languageCode = locale.languageCode
                ).executeAsOne()
                queries.deletePaths(routeId)
                route.directPaths.forEach { path: Path ->
                    queries.insertPath(
                        transportationType = path.transportationType.name,
                        price = path.euroPrice,
                        duration = path.durationMinutes,
                        fromLocationName = path.from,
                        toLocationName = path.to,
                        routeId = routeId
                    )
                }
            }
        }
    }

    override fun get(parameters: ParamsBundle): List<Route> {
        val fromLocation = parameters.get(Key.FROM) as Location
        val toLocation = parameters.get(Key.TO) as Location
        val locale = parameters.get(Key.LOCALE) as Locale
        return queries.selectRoutes(
            fromLocationName = fromLocation.name,
            toLocationName = toLocation.name,
            languageCode = locale.languageCode,
            mapper = { id, type, price, duration ->
                val paths = queries.selectPaths(id) { transportationType,
                                                      pathPrice,
                                                      pathDuration,
                                                      fromLocationName,
                                                      toLocationName ->
                    Path(
                        transportationType = TransportationType.valueOf(transportationType),
                        euroPrice = pathPrice,
                        durationMinutes = pathDuration,
                        from = fromLocationName,
                        to = toLocationName
                    )
                }.executeAsList()
                Route(
                    routeType = Type.valueOf(type),
                    euroPrice = price,
                    durationMinutes = duration,
                    directPaths = paths
                )
            }
        ).executeAsList()
    }
}
