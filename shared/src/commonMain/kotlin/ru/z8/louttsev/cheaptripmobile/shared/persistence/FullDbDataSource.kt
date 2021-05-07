/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared.persistence

import com.squareup.sqldelight.db.SqlDriver
import ru.z8.louttsev.cheaptripmobile.persistence.FullDb
import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource

/**
 * Declares fake remote data source implementation based on a copy of the server DB.
 *
 * Will be deprecated later, after network data source implementation.
 *
 * @param sqlDriver A platform-specific implementation of the SQLite driver.
 */
abstract class FullDbDataSource<T>(sqlDriver: SqlDriver) : DataSource<T> {
    private val queries = FullDb(sqlDriver).fullDbQueries
}