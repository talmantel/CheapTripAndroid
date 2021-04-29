package ru.z8.louttsev.cheaptripmobile.shared.persistence

import com.squareup.sqldelight.db.SqlDriver
import ru.z8.louttsev.cheaptripmobile.persistence.LocalDb
import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource.ParamsBundle
import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource.ParamsBundle.Key
import ru.z8.louttsev.cheaptripmobile.shared.model.DataStorage
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Locale
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location

/**
 * Declares data storage implementation based on local database.
 *
 * @param sqlDriver A platform-specific implementation of the SQLite driver.
 */
class LocalDbStorage(sqlDriver: SqlDriver) : DataStorage<Location> {
    private val queries = LocalDb(sqlDriver).localDbQueries

    override fun put(data: List<Location>) {
        data.forEach { location: Location ->
            queries.upsertLocation(
                id = location.id,
                name = location.name,
                type = location.type.name,
                language = location.locale.languageCode
            )
        }
    }

    override fun get(parameters: ParamsBundle): List<Location> {
        val needle = parameters.get(Key.NEEDLE) as String
        val limit = parameters.get(Key.LIMIT) as Long
        return queries.selectLocationsByName(needle, limit) { id, name, type, language ->
            Location(id, name, Location.Type.valueOf(type), Locale.fromLanguageCode(language))
        }.executeAsList()
    }
}
