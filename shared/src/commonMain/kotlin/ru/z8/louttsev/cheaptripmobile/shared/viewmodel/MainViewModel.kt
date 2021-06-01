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
 * @property oroginLocations Available origin locations
 * @property destinationLocations Available destination locations
 */
class MainViewModel(
    private val locationRepository: LocationRepository,
) : ViewModel() {
    private val _originLocations = MutableLiveData<List<Location>>(emptyList())

    init {
        viewModelScope.launch {
            _originLocations.value = locationRepository.searchLocationsByName("", Type.FROM)
        }
    }

    val oroginLocations: LiveData<List<Location>>
        get() = _originLocations

    private var selectedOrigin: Location? = null

    private val _destinationLocations = MutableLiveData<List<Location>>(emptyList())

    init {
        viewModelScope.launch {
            _destinationLocations.value = locationRepository.searchLocationsByName("", Type.TO)
        }
    }

    val destinationLocations: LiveData<List<Location>>
        get() = _destinationLocations

    private var selectedDestination: Location? = null

    /**
     * Updates origin locations list accordingly search pattern.
     *
     * @param needle Typed part of autocomplete input field
     */
    fun onOriginInputFieldTextChanged(needle: String) {
        viewModelScope.launch {
            _originLocations.value = locationRepository.searchLocationsByName(needle, Type.FROM)
        }
    }

    /**
     * Sets selected origin location.
     *
     * @param location Selected item into autocomplete input field
     */
    fun onOriginSelected(location: Location?) {
        selectedOrigin = location
    }

    /**
     * Resets the previously selected origin.
     */
    fun onOriginReset() {
        selectedOrigin = null
    }

    /**
     * Updates destination locations list accordingly search pattern.
     *
     * @param needle Typed part of autocomplete input field
     */
    fun onDestinationInputFieldTextChanged(needle: String) {
        viewModelScope.launch {
            _destinationLocations.value = locationRepository.searchLocationsByName(needle, Type.TO)
        }
    }

    /**
     * Sets selected destination location.
     *
     * @param location Selected item into autocomplete input field
     */
    fun onDestinationSelected(location: Location?) {
        selectedDestination = location
    }

    /**
     * Resets the previously selected destination.
     */
    fun onDestinationReset() {
        selectedDestination = null
    }
}