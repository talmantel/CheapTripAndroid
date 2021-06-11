/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared.viewmodel

import dev.icerock.moko.mvvm.livedata.LiveData
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Route

/**
 * Determines UI actions handle logic and data for build routes button.
 *
 * @property data Items list
 * @property isReadyToBuild Correct state indicator
 */
interface GoButtonHandler {
    val data: LiveData<List<Route>>
    val isReadyToBuild: LiveData<Boolean>

    fun build(emptyResultHandler: () -> Unit)
}