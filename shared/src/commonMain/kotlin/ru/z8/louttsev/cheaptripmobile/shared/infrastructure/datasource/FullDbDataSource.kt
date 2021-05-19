/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared.infrastructure.datasource

import com.squareup.sqldelight.db.SqlDriver
import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Locale
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location.Type

/**
 * Declares data source implementation based on a copy of the server DB.
 *
 * Will be deprecated later, after network data source implementation.
 *
 * @param sqlDriver A platform-specific implementation of the SQLite driver.
 */
abstract class FullDbDataSource<T>(sqlDriver: SqlDriver) : DataSource<T> {
    private val queries = FullDb(sqlDriver).fullDbQueries

    /**
     * Finds locations that have search string in their name.
     *
     * @param needle Search string
     * @param type Narrows the search by route start/finish point
     * @param limit Desired number of locations in the response
     * @param locale Desired results language
     * @return List of matching locations no larger than the specified size
     */
    protected fun selectLocationsByName(
        needle: String,
        type: Type,
        limit: Long,
        locale: Locale
    ): List<Location> {
        val suitableLocations: List<Location> = queries.selectLocationsByName(
            needle,
            limit,
            mapper = { id, name, name_ru ->
                Location(
                    id,
                    name = when (locale) {
                        Locale.RU -> name_ru
                        else -> name
                    }
                )
            }
        ).executeAsList()

        return suitableLocations.filterByType(type)
    }

    private fun List<Location>.filterByType(type: Type): List<Location> = when (type) {
        Type.ALL -> this // no filtration required
        Type.FROM -> {
            this.filter { (id) -> queries.selectFrom().executeAsList().contains(id) }
        }
        Type.TO -> {
            this.filter { (id) -> queries.selectTo().executeAsList().contains(id) }
        }
    }
}