package ru.z8.louttsev.cheaptripmobile.shared

import android.content.Context
import android.support.test.runner.AndroidJUnit4
import androidx.test.core.app.ApplicationProvider
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import ru.z8.louttsev.cheaptripmobile.shared.infrastructure.datasource.FullDbDataSource
import ru.z8.louttsev.cheaptripmobile.shared.infrastructure.datasource.RouteDataSourceFullDb
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
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Route.Type.*
import ru.z8.louttsev.cheaptripmobile.shared.model.data.TransportationType.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Config(sdk = [24])
@RunWith(AndroidJUnit4::class)
class RouteRepositoryTest {
    private val dataSourceWrapper = object : DataSource<Route> {
        private val dataSource: FullDbDataSource<Route>

        init {
            val context = ApplicationProvider.getApplicationContext<Context>()
            val driver = DatabaseDriverFactory(context).getDriver(LocalDb.Schema, "fullDb.sqlite3")
            dataSource = RouteDataSourceFullDb(driver)
        }

        var isAvailable: Boolean = true

        override fun get(parameters: ParamsBundle): List<Route>? {
            if (isAvailable) {
                return dataSource.get(parameters)
            } else {
                return null
            }
        }
    }

    private val dataStorage: LocalDbStorage<Route>

    init {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val driver = DatabaseDriverFactory(context).createDriver(LocalDb.Schema, "localDb.sqlite3")
        dataStorage = RouteDb(driver)
    }

    private val repositoryUnderTest = RouteRepository(
        dataSourceWrapper,
        dataStorage,
        RepositoryStrategy.CACHING
    )

    @Test
    fun getRoutes() {
        val fromLocation = Location(387, "Moscow")
        val toLocation = Location(9, "Tashkent")

        val expectedResult = listOf(
            Route(GROUND, 36.61F, 4020, listOf(Path(TRAIN, 36.61F, 4020, "Moscow", "Tashkent"))),
            Route(FLYING, 151.256F, 916, listOf(Path(FLIGHT, 53.2F, 246, "Moscow", "Ashgabat"), Path(FLIGHT, 98.056F, 670, "Ashgabat", "Tashkent"))),
            Route(DIRECT, 188.23F, 398, listOf(Path(FLIGHT, 188.23F, 398, "Moscow", "Tashkent")))
        )

        assertTrue(dataSourceWrapper.isAvailable)
        val result: List<Route> =
            repositoryUnderTest.getRoutes(fromLocation, toLocation, Locale.EN)
        assertEquals(expectedResult, result)
        dataSourceWrapper.isAvailable = false
        val cachedResult: List<Route> =
            repositoryUnderTest.getRoutes(fromLocation, toLocation, Locale.EN)
        assertEquals(expectedResult, cachedResult)
    }
}
