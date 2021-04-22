package model

import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource
import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource.ParamsBundle
import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource.ParamsBundle.Key
import ru.z8.louttsev.cheaptripmobile.shared.model.DataStorage
import ru.z8.louttsev.cheaptripmobile.shared.model.LocationRepository
import ru.z8.louttsev.cheaptripmobile.shared.model.RepositoryStrategy
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LocationRepositoryTest {
    private val dataSourceFake = object : DataSource<Location> {
        var isAvailable: Boolean = true

        override fun get(parameters: ParamsBundle): List<Location>? {
            if (!isAvailable) {
                return null
            }
            val needle = parameters.get(Key.NEEDLE).toString()
            if (needle.isEmpty() || needle.isBlank()) {
                return emptyList()
            } else {
                return listOf(
                    Location(1, "$needle-started-fake-city")
                )
            }
        }
    }

    private val dataStorageFake = object : DataStorage<Location> {
        val locationStorage = emptyMap<String, Location>().toMutableMap()

        override fun put(data: List<Location>) {
            data.forEach { locationStorage[it.name] = it }
        }

        override fun get(parameters: ParamsBundle): List<Location> {
            val needle = parameters.get(Key.NEEDLE).toString()
            if (needle.isEmpty() || needle.isBlank()) {
                return emptyList()
            } else {
                return locationStorage.filterKeys { it.contains(needle) }.values.toList()
            }
        }
    }

    private val repositoryUnderTest = LocationRepository(
        dataSourceFake,
        dataStorageFake,
        RepositoryStrategy.BACKUP
    )

    @Test
    fun searchLocationsByName() {
        assertTrue(dataSourceFake.isAvailable)
        val result = repositoryUnderTest.searchLocationsByName("Mos")
        assertEquals(result, dataStorageFake.locationStorage.values.toList())
        dataSourceFake.isAvailable = false
        val cachedResult = repositoryUnderTest.searchLocationsByName("Mos")
        assertEquals(result, cachedResult)
    }
}