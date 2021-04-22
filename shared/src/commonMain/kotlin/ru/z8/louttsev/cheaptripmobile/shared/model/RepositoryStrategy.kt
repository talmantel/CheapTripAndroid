/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared.model

import ru.z8.louttsev.cheaptripmobile.shared.model.DataSource.ParamsBundle

/**
 * Declares data sources usage logic.
 */
enum class RepositoryStrategy {
    BACKUP {
        override fun <T> combineLoaderFrom(
            dataSource: DataSource<T>,
            dataStorage: DataStorage<T>
        ): (ParamsBundle) -> List<T> = { params: ParamsBundle ->
            val result = dataSource.get(params)
            result?.also {
                dataStorage.put(result)
            } ?: dataStorage.get(params)
        }
    };

    abstract fun <T> combineLoaderFrom(
        dataSource: DataSource<T>,
        dataStorage: DataStorage<T>
    ): (ParamsBundle) -> List<T>
}
