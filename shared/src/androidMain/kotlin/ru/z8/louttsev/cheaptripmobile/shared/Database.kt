/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import ru.z8.louttsev.cheaptripmobile.MR
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

/**
 * A platform-specific implementation of the SQLite driver factory.
 */
actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(schema: SqlDriver.Schema, fileName: String): SqlDriver {
        return AndroidSqliteDriver(schema, context, fileName)
    }

    actual fun getDriver(schema: SqlDriver.Schema, fileName: String): SqlDriver {
        val database: File = context.getDatabasePath(fileName)

        if (!database.exists()) {
            val inputStream = context.resources.openRawResource(MR.files.fullDb.rawResId)
            val outputStream = FileOutputStream(database.absolutePath)

            inputStream.use { input: InputStream ->
                outputStream.use { output: FileOutputStream ->
                    input.copyTo(output)
                }
            }
        }

        return AndroidSqliteDriver(schema, context, fileName)
    }
}