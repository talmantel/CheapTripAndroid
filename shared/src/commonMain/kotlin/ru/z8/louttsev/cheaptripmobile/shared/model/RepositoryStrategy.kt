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
    /**
     * Strategy implementation that receives data from main (read-only) source if it available,
     * and saves result to reserve (full access) storage. When main source isn't available,
     * data receive from reserve source.
     */
    BACKUP {
        override fun <T> combineLoaderFrom(
            dataSource: DataSource<T>,
            dataStorage: DataStorage<T>
        ): (ParamsBundle) -> List<T> = { params: ParamsBundle ->
            val result = dataSource.get(params)
            result?.also {
                dataStorage.put(result, params)
            } ?: dataStorage.get(params)
        }
    };

    /**
     *
     * @param dataSource Read-only data access source
     * @param dataStorage Full data access source
     * @return Function that encapsulates data sources interaction
     */
    abstract fun <T> combineLoaderFrom(
        dataSource: DataSource<T>,
        dataStorage: DataStorage<T>
    ): (ParamsBundle) -> List<T>
}
