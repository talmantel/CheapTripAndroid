package ru.z8.louttsev.cheaptripmobile.shared

import android.content.Context
import android.support.test.runner.AndroidJUnit4
import androidx.test.core.app.ApplicationProvider
import org.junit.runner.RunWith
import ru.z8.louttsev.cheaptripmobile.shared.infrastructure.persistence.LocalDb
import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource
import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource.ParamsBundle
import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource.ParamsBundle.Key
import ru.z8.louttsev.cheaptripmobile.shared.model.LocationRepository
import ru.z8.louttsev.cheaptripmobile.shared.model.RepositoryStrategy
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Locale
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location.Type
import ru.z8.louttsev.cheaptripmobile.shared.infrastructure.persistence.LocalDbStorage
import ru.z8.louttsev.cheaptripmobile.shared.infrastructure.persistence.LocationDb
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
class LocationRepositoryTest {
    private val dataSourceFake = object : DataSource<Location> {
        var isAvailable: Boolean = true
        val locationStorage = emptyMap<Triple<Int, Type, Locale>, Location>()
            .toMutableMap()
            .also {
                it[Triple(1, Type.ALL, Locale.EN)] = Location(1, "Moscow")
                it[Triple(1, Type.FROM, Locale.EN)] = Location(1, "Moscow")
                it[Triple(1, Type.TO, Locale.EN)] = Location(1, "Moscow")
                it[Triple(1, Type.ALL, Locale.RU)] = Location(1, "Москва")
            }

        override fun get(parameters: ParamsBundle): List<Location>? {
            if (!isAvailable) {
                return null
            }
            val needle = parameters.get(Key.NEEDLE) as String
            val type = parameters.get(Key.TYPE) as Type
            val locale = parameters.get(Key.LOCALE) as Locale
            if (needle.isEmpty() || needle.isBlank()) {
                return emptyList()
            } else {
                return locationStorage
                    .filterValues { it.name.contains(needle) }
                    .filterKeys { it.second == type && it.third == locale }
                    .values.toList()
            }
        }
    }

    private val dataStorage: LocalDbStorage<Location>

    init {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val driver = DatabaseDriverFactory(context).createDriver(LocalDb.Schema, "local.db")
        dataStorage = LocationDb(driver)
    }

    private val repositoryUnderTest = LocationRepository(
        dataSourceFake,
        dataStorage,
        RepositoryStrategy.BACKUP
    )

    @Test
    fun searchLocationsByName() {
        assertTrue(dataSourceFake.isAvailable)
        val result: List<Location> =
            repositoryUnderTest.searchLocationsByName(needle = "Mos", locale = Locale.EN)
        dataSourceFake.isAvailable = false
        val cachedResult: List<Location> =
            repositoryUnderTest.searchLocationsByName(needle = "Mos", locale = Locale.EN)
        assertEquals(result, cachedResult)
    }
}