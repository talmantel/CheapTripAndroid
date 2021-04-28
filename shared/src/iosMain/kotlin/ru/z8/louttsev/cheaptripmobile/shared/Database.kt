/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared

import com.squareup.sqldelight.db.SqlDriver

/**
 * A platform-specific implementation of the SQLite driver factory.
 */
actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        TODO("Not yet implemented")
    }
}