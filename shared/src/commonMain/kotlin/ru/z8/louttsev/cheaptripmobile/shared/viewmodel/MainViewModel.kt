/**
 * CheapTrip Mobile
 * This is mobile client for LowCostsTrip server.
 */
package ru.z8.louttsev.cheaptripmobile.shared.viewmodel

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.launch
import ru.z8.louttsev.cheaptripmobile.shared.model.LocationRepository
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location.Type

/**
 * Declares UX logic for managing the data and handling the UI actions.
 *
 * @property locationRepository Read-only storage of available locations
 * @property origins Available origin locations
 * @property destinations Available destination locations
 */
class MainViewModel(
    private val locationRepository: LocationRepository,
) : ViewModel() {
    private var selectedOrigin: Location? = null
    private var selectedDestination: Location? = null

    val origins = object : AutoCompleteHandler<Location> {
        private val locations = MutableLiveData<List<Location>>(emptyList())
        override val data: LiveData<List<Location>>
            get() = locations

        override var isBeingUpdated: Boolean = false

        override fun onTextChanged(text: String, emptyResultHandler: () -> Unit) {
            viewModelScope.launch {
                val result = locationRepository.searchLocationsByName(text, Type.FROM)
                if (result.isEmpty()) {
                    emptyResultHandler()
                } else {
                    locations.value = result
                }
            }
        }

        override fun onItemSelected(item: Location) {
            selectedOrigin = item
        }

        override fun onItemReset() {
            selectedOrigin = null
        }

        override fun isItemSelected(): Boolean = selectedOrigin != null
    }

    val destinations = object : AutoCompleteHandler<Location> {
        private val locations = MutableLiveData<List<Location>>(emptyList())
        override val data: LiveData<List<Location>>
            get() = locations

        override var isBeingUpdated: Boolean = false

        override fun onTextChanged(text: String, emptyResultHandler: () -> Unit) {
            viewModelScope.launch {
                val result = locationRepository.searchLocationsByName(text, Type.TO)
                if (result.isEmpty()) {
                    emptyResultHandler()
                } else {
                    locations.value = result
                }
            }
        }

        override fun onItemSelected(item: Location) {
            selectedDestination = item
        }

        override fun onItemReset() {
            selectedDestination = null
        }

        override fun isItemSelected(): Boolean = selectedDestination != null
    }

    fun getRoute(): String {
        val origin = selectedOrigin?.name ?: "null"
        val destination = selectedDestination?.name ?: "null"

        return "$origin -> $destination"
    }
}