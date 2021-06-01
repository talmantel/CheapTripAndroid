package ru.z8.louttsev.cheaptripmobile.shared.viewmodel

import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.launch
import ru.z8.louttsev.cheaptripmobile.shared.model.LocationRepository
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location
import ru.z8.louttsev.cheaptripmobile.shared.model.data.Location.Type

class MainViewModel(
    private val locationRepository: LocationRepository,
) : ViewModel() {
    private val _fromLocations = MutableLiveData(
        locationRepository.searchLocationsByName("", Type.ALL)
    )
    val fromLocations: LiveData<List<Location>>
        get() = _fromLocations
    private var selectedFromLocation: Location? = null

    private val _toLocations = MutableLiveData(
        locationRepository.searchLocationsByName("", Type.ALL)
    )
    val toLocations: LiveData<List<Location>>
        get() = _toLocations
    private var selectedToLocation: Location? = null

    fun onFromLocationInputFieldTextChanged(needle: String) {
        viewModelScope.launch {
            _fromLocations.value = locationRepository.searchLocationsByName(needle, Type.ALL)
        }
    }

    fun onFromLocationSelected(location: Location?) {
        selectedFromLocation = location
    }

    fun onToLocationInputFieldTextChanged(needle: String) {
        viewModelScope.launch {
            _toLocations.value = locationRepository.searchLocationsByName(needle, Type.ALL)
        }
    }

    fun onToLocationSelected(location: Location?) {
        selectedToLocation = location
    }
}