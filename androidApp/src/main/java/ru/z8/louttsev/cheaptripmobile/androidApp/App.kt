package ru.z8.louttsev.cheaptripmobile.androidApp

import android.app.Application
import ru.z8.louttsev.cheaptripmobile.shared.DatabaseDriverFactory
import ru.z8.louttsev.cheaptripmobile.shared.convertToString
import ru.z8.louttsev.cheaptripmobile.shared.infrastructure.datasource.FullDb
import ru.z8.louttsev.cheaptripmobile.shared.infrastructure.datasource.LocationDataSourceFullDb
import ru.z8.louttsev.cheaptripmobile.shared.infrastructure.datasource.RouteDataSourceFullDb
import ru.z8.louttsev.cheaptripmobile.shared.infrastructure.persistence.LocalDb
import ru.z8.louttsev.cheaptripmobile.shared.infrastructure.persistence.LocationDb
import ru.z8.louttsev.cheaptripmobile.shared.infrastructure.persistence.RouteDb
import ru.z8.louttsev.cheaptripmobile.shared.model.LocationRepository
import ru.z8.louttsev.cheaptripmobile.shared.model.RepositoryStrategy.*
import ru.z8.louttsev.cheaptripmobile.shared.model.RouteRepository

/**
 * Declares DI container.
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        convertToString = { toString(this@App) }

        val fullDbDriver = DatabaseDriverFactory(this).getDriver(FullDb.Schema, "fullDb.sqlite3")
        val localDbDriver = DatabaseDriverFactory(this).createDriver(LocalDb.Schema, "localDb.sqlite3")

        sLocationRepository = LocationRepository(
            mainSource = LocationDataSourceFullDb(fullDbDriver),
            reserveSource = LocationDb(localDbDriver),
            strategy = DIRECT_READ
        )

        sRouteRepository = RouteRepository(
            mainSource = RouteDataSourceFullDb(fullDbDriver),
            reserveSource = RouteDb(localDbDriver),
            strategy = CACHING
        )
    }

    /**
     * Access point to initiated repositories.
     *
     * @property sLocationRepository Read-only storage of available locations.
     * @property sRouteRepository Read-only storage of available routes.
     */
    companion object {
        lateinit var sLocationRepository: LocationRepository
        lateinit var  sRouteRepository: RouteRepository
    }
}