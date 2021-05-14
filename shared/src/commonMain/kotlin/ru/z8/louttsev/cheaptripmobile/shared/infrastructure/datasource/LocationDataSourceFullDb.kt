/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared.infrastructure.datasource

import com.squareup.sqldelight.db.SqlDriver
import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource.ParamsBundle
import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource.ParamsBundle.Key
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Locale
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location

/**
 * Declares locations data source implementation based on a copy of the server DB.
 *
 * @param sqlDriver A platform-specific implementation of the SQLite driver.
 */
class LocationDataSourceFullDb(sqlDriver: SqlDriver) : FullDbDataSource<Location>(sqlDriver) {
    override fun get(parameters: ParamsBundle): List<Location>? {
        val needle = parameters.get(Key.NEEDLE) as String
        val type = parameters.get(Key.TYPE) as Location.Type
        val limit = parameters.get(Key.LIMIT) as Long
        val locale = parameters.get(Key.LOCALE) as Locale

        return selectLocationsByName(needle, type, limit, locale)
    }
}