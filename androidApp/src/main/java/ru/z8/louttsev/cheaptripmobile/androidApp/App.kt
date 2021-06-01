package ru.z8.louttsev.cheaptripmobile.androidApp

import android.app.Application
import ru.z8.louttsev.cheaptripmobile.shared.DatabaseDriverFactory
import ru.z8.louttsev.cheaptripmobile.shared.convertToString
import ru.z8.louttsev.cheaptripmobile.shared.infrastructure.datasource.FullDb
import ru.z8.louttsev.cheaptripmobile.shared.infrastructure.datasource.LocationDataSourceFullDb
import ru.z8.louttsev.cheaptripmobile.shared.infrastructure.persistence.LocalDb
import ru.z8.louttsev.cheaptripmobile.shared.infrastructure.persistence.LocationDb
import ru.z8.louttsev.cheaptripmobile.shared.model.LocationRepository
import ru.z8.louttsev.cheaptripmobile.shared.model.RepositoryStrategy

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        convertToString = { toString(this@App) }

        sLocationRepository = LocationRepository(
            mainSource = LocationDataSourceFullDb(
                DatabaseDriverFactory(this).getDriver(FullDb.Schema, "fullDb.sqlite3")
            ),
            reserveSource = LocationDb(
                DatabaseDriverFactory(this).createDriver(LocalDb.Schema, "localDb.sqlite3")
            ),
            strategy = RepositoryStrategy.CACHING
        )
    }

    companion object {
        lateinit var sLocationRepository: LocationRepository
    }
}