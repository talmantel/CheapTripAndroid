package ru.z8.louttsev.cheaptripmobile.shared.persistence

import com.squareup.sqldelight.db.SqlDriver
import ru.z8.louttsev.cheaptripmobile.persistence.LocalDb
import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource.*
import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource.ParamsBundle.*
import ru.z8.louttsev.cheaptripmobile.shared.model.DataStorage
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location

/**
 *
 */
class LocalDbStorage(sqlDriver: SqlDriver): DataStorage<Location> {
    private val queries = LocalDb(sqlDriver).localDbQueries

    override fun put(data: List<Location>) {
        data.forEach { location: Location ->
            queries.upsertLocation(id = location.id, name = location.name)
        }
    }

    override fun get(parameters: ParamsBundle): List<Location> {
        val needle = parameters.get(Key.NEEDLE) as String
        return queries.selectLocationByName(needle) { id: Int, name: String ->
            Location(id, name)
        }.executeAsList()
    }
}
