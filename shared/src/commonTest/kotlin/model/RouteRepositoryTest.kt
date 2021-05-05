package model

import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource
import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource.ParamsBundle
import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource.ParamsBundle.Key
import ru.z8.louttsev.cheaptripmobile.shared.model.DataStorage
import ru.z8.louttsev.cheaptripmobile.shared.model.RepositoryStrategy
import ru.z8.louttsev.cheaptripmobile.shared.model.RouteRepository
import ru.z8.louttsev.cheaptripmobile.shared.model.data.*
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location.*
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Route.RouteType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RouteRepositoryTest {
    private val dataSourceFake = object : DataSource<Route> {
        var isAvailable: Boolean = true

        override fun get(parameters: ParamsBundle): List<Route>? {
            if (!isAvailable) {
                return null
            }
            val from = parameters.get(Key.FROM) as Location
            val to = parameters.get(Key.TO) as Location
            return listOf(
                Route(
                    RouteType.DIRECT,
                    35.3F,
                    280,
                    listOf(
                        Path(
                            TransportationType.BUS,
                            35.3F,
                            280,
                            from.name,
                            to.name
                        )
                    )
                )
            )
        }
    }

    private val dataStorageFake = object : DataStorage<Route> {
        val routeStorage = emptyMap<Pair<String, String>, Route>().toMutableMap()

        override fun put(data: List<Route>, parameters: ParamsBundle) {
            data.forEach {
                routeStorage[it.directPaths.first().from to it.directPaths.last().to] = it
            }
        }

        override fun get(parameters: ParamsBundle): List<Route> {
            val from = parameters.get(Key.FROM) as Location
            val to = parameters.get(Key.TO) as Location
            return routeStorage.filterKeys { it == from.name to to.name }.values.toList()
        }
    }

    private val repositoryUnderTest = RouteRepository(
        dataSourceFake,
        dataStorageFake,
        RepositoryStrategy.BACKUP
    )

    @Test
    fun getRoutes() {
        val fromLocation = Location(1, "Moscow")
        val toLocation = Location(2, "Petersburg")
        assertTrue(dataSourceFake.isAvailable)
        val result = repositoryUnderTest.getRoutes(fromLocation, toLocation)
        assertEquals(result, dataStorageFake.routeStorage.values.toList())
        dataSourceFake.isAvailable = false
        val cachedResult = repositoryUnderTest.getRoutes(fromLocation, toLocation)
        assertEquals(result, cachedResult)
    }
}