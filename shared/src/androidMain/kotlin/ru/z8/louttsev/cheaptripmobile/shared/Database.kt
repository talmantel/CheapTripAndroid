/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

/**
 * A platform-specific implementation of the SQLite driver factory.
 */
actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(schema: SqlDriver.Schema, fileName: String): SqlDriver {
        return AndroidSqliteDriver(schema, context, fileName)
    }
}