/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared

import com.squareup.sqldelight.db.SqlDriver

/**
 * A platform-specific declaration of the SQLite driver factory.
 */
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}