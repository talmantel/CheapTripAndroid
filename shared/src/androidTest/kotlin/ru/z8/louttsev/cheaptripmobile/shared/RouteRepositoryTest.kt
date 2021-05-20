package ru.z8.louttsev.cheaptripmobile.shared

import android.content.Context
import android.support.test.runner.AndroidJUnit4
import androidx.test.core.app.ApplicationProvider
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ru.z8.louttsev.cheaptripmobile.shared.infrastructure.persistence.LocalDb
import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource
import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource.ParamsBundle
import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource.ParamsBundle.Key
import ru.z8.louttsev.cheaptripmobile.shared.model.RepositoryStrategy
import ru.z8.louttsev.cheaptripmobile.shared.model.RouteRepository
import ru.z8.louttsev.cheaptripmobile.shared.model.data.*
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Route.Type
import ru.z8.louttsev.cheaptripmobile.shared.infrastructure.persistence.LocalDbStorage
import ru.z8.louttsev.cheaptripmobile.shared.infrastructure.persistence.RouteDb
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Config(sdk = [24])
@RunWith(AndroidJUnit4::class)
class RouteRepositoryTest {
    private val dataSourceFake = object : DataSource<Route> {
        var isAvailable: Boolean = true
        val routeStorage = emptyMap<Quadruple<Type, Location, Location, Locale>, Route>()
            .toMutableMap()
            .also {
                it[Quadruple(
                    Type.DIRECT,
                    Location(1, "Moscow"),
                    Location(2, "Petersburg"),
                    Locale.EN
                )] =
                    Route(
                        Type.DIRECT,
                        35.3F,
                        280,
                        listOf(
                            Path(
                                TransportationType.TRAIN,
                                35.3F,
                                280,
                                "Moscow",
                                "Petersburg"
                            )
                        )
                    )
                it[Quadruple(
                    Type.GROUND,
                    Location(1, "Moscow"),
                    Location(2, "Petersburg"),
                    Locale.EN
                )] =
                    Route(
                        Type.GROUND,
                        48.5F,
                        360,
                        listOf(
                            Path(
                                TransportationType.BUS,
                                28.5F,
                                200,
                                "Moscow",
                                "Tver"
                            ),
                            Path(
                                TransportationType.BUS,
                                20.0F,
                                160,
                                "Tver",
                                "Petersburg"
                            )
                        )
                    )
            }

        override fun get(parameters: ParamsBundle): List<Route>? {
            if (!isAvailable) {
                return null
            }
            val from = parameters.get(Key.FROM) as Location
            val to = parameters.get(Key.TO) as Location
            val locale = parameters.get(Key.LOCALE) as Locale
            return routeStorage
                .filterKeys {
                    it.second == from && it.third == to && it.fourth == locale
                }
                .values.toList()
        }
    }

    private val dataStorage: LocalDbStorage<Route>

    init {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val driver = DatabaseDriverFactory(context).createDriver(LocalDb.Schema, "localDb.sqlite3")
        dataStorage = RouteDb(driver)
    }

    private val repositoryUnderTest = RouteRepository(
        dataSourceFake,
        dataStorage,
        RepositoryStrategy.CACHING
    )

    @Test
    fun getRoutes() {
        val fromLocation = Location(1, "Moscow")
        val toLocation = Location(2, "Petersburg")
        assertTrue(dataSourceFake.isAvailable)
        val result: List<Route> =
            repositoryUnderTest.getRoutes(fromLocation, toLocation, Locale.EN)
        dataSourceFake.isAvailable = false
        val cachedResult: List<Route> =
            repositoryUnderTest.getRoutes(fromLocation, toLocation, Locale.EN)
        assertEquals(result, cachedResult)
    }
}

data class Quadruple<out A, out B, out C, out D>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D
)