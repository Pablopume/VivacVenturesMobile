package com.example.vivacventuresmobile.ui.screens.listplaces

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vivacventuresmobile.domain.modelo.AutocompleteResult
import com.example.vivacventuresmobile.domain.usecases.GetVivacPlaceByTypeUseCase
import com.example.vivacventuresmobile.domain.usecases.GetVivacPlaceNearbyUseCase
import com.example.vivacventuresmobile.domain.usecases.GetVivacPlacesWithFavouritesUseCase
import com.example.vivacventuresmobile.utils.NetworkResult
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListPlacesViewModel @Inject constructor(
    private val getVivacPlacesUseCase: GetVivacPlacesWithFavouritesUseCase,
    private val getVivacPlacesByTypeUseCase: GetVivacPlaceByTypeUseCase,
    private val getVivacPlacesNearbyUseCase: GetVivacPlaceNearbyUseCase
) : ViewModel() {
    private val _uiState: MutableStateFlow<ListPlacesState> by lazy {
        MutableStateFlow(ListPlacesState())
    }

    val uiState: StateFlow<ListPlacesState> = _uiState

    val locationAutofill = mutableStateListOf<AutocompleteResult>()

    lateinit var placesClient: PlacesClient

    init {
        _uiState.value = ListPlacesState(
            error = null,
            loading = false
        )
    }

    fun handleEvent(event: ListPlacesEvent) {
        when (event) {
            ListPlacesEvent.ErrorVisto -> _uiState.value = _uiState.value.copy(error = null)
            is ListPlacesEvent.GetVivacPlaces -> getVivacPlaces()
            is ListPlacesEvent.GetVivacPlacesByType -> getVivacPlacesByType(event.type)
            is ListPlacesEvent.SearchPlaces -> searchPlaces(event.query)
            is ListPlacesEvent.SaveUsername -> {
                _uiState.value = _uiState.value.copy(username = event.username)
                getVivacPlaces()
            }
        }
    }

    private fun getVivacPlacesByType(type: String) {
        _uiState.update { it.copy(loading = true) }
        if (type == "") {
            getVivacPlaces()
        } else {
            viewModelScope.launch {
                getVivacPlacesByTypeUseCase(type, _uiState.value.username)
                    .catch(action = { cause ->
                        _uiState.update {
                            it.copy(
                                error = cause.message,
                                loading = false
                            )
                        }
                    })
                    .collect { result ->
                        when (result) {
                            is NetworkResult.Error -> {
                                _uiState.update {
                                    it.copy(
                                        error = result.message,
                                        loading = false
                                    )
                                }
                            }

                            is NetworkResult.Success -> {
                                result.data?.let { places ->
                                    _uiState.update {
                                        it.copy(
                                            vivacPlaces = places,
                                            loading = false
                                        )
                                    }
                                }
                            }

                            is NetworkResult.Loading -> {
                                _uiState.update {
                                    it.copy(
                                        loading = true
                                    )
                                }
                            }
                        }
                    }
            }
        }

    }

    private fun getVivacPlaces() {
        if (_uiState.value.username.isNotEmpty()){
            _uiState.update { it.copy(loading = true) }
            viewModelScope.launch {
                getVivacPlacesUseCase(_uiState.value.username)
                    .catch(action = { cause ->
                        _uiState.update {
                            it.copy(
                                error = cause.message,
                                loading = false
                            )
                        }
                    })
                    .collect { result ->
                        when (result) {
                            is NetworkResult.Error -> {
                                _uiState.update {
                                    it.copy(
                                        error = result.message,
                                        loading = false
                                    )
                                }
                            }

                            is NetworkResult.Success -> {
                                result.data?.let { places ->
                                    _uiState.update {
                                        it.copy(
                                            vivacPlaces = places,
                                            loading = false
                                        )
                                    }
                                }
                            }

                            is NetworkResult.Loading -> {
                                _uiState.update {
                                    it.copy(
                                        loading = true
                                    )
                                }
                            }
                        }
                    }
            }
        }
    }

    private fun getVivacPlaceNearby(latLong: LatLng){
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            getVivacPlacesNearbyUseCase(latLong, _uiState.value.username)
                .catch(action = { cause ->
                    _uiState.update {
                        it.copy(
                            error = cause.message,
                            loading = false
                        )
                    }
                })
                .collect { result ->
                    when (result) {
                        is NetworkResult.Error -> {
                            _uiState.update {
                                it.copy(
                                    error = result.message,
                                    loading = false
                                )
                            }
                        }

                        is NetworkResult.Success -> {
                            result.data?.let { places ->
                                _uiState.update {
                                    it.copy(
                                        vivacPlaces = places,
                                        loading = false
                                    )
                                }
                            }
                        }

                        is NetworkResult.Loading -> {
                            _uiState.update {
                                it.copy(
                                    loading = true
                                )
                            }
                        }
                    }
                }
        }
    }

    private var job: Job? = null

    fun searchPlaces(query: String) {
        job?.cancel()
        locationAutofill.clear()
        job = viewModelScope.launch {
            val request = FindAutocompletePredictionsRequest
                .builder()
                .setQuery(query)
                .build()
            placesClient
                .findAutocompletePredictions(request)
                .addOnSuccessListener { response ->
                    locationAutofill += response.autocompletePredictions.map {
                        AutocompleteResult(
                            it.getFullText(null).toString(),
                            it.placeId
                        )
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                    println(it.cause)
                    println(it.message)
                }
        }
    }

    fun getCoordinates(result: AutocompleteResult) {
        val placeFields = listOf(Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.newInstance(result.placeId, placeFields)
        placesClient.fetchPlace(request)
            .addOnSuccessListener {
                if (it != null) {
                    getVivacPlaceNearby(it.place.latLng!!)
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }
}