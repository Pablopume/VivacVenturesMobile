package com.example.vivacventuresmobile.ui.screens.addLocation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.location.Geocoder

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vivacventuresmobile.ui.screens.map.MapState
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

sealed class LocationState {
    object NoPermission : LocationState()
    object LocationDisabled : LocationState()
    object LocationLoading : LocationState()
    data class LocationAvailable(val cameraLatLang: LatLng) : LocationState()
    object Error : LocationState()
}

data class AutocompleteResult(
    val address: String,
    val placeId: String,
)

@HiltViewModel
class LocationViewModel @Inject constructor() : ViewModel() {

    private val _uiState: MutableStateFlow<MapState> by lazy {
        MutableStateFlow(MapState())
    }
    val uiState: MutableStateFlow<MapState> = _uiState

    fun handleEvent(event: AddLocationEvent) {
        when (event) {
            AddLocationEvent.ErrorVisto -> _uiState.value = _uiState.value.copy(error = null)
            is AddLocationEvent.SendError -> {
                _uiState.value = _uiState.value.copy(error = event.error)
            }
        }
    }

    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var placesClient: PlacesClient
    lateinit var geoCoder: Geocoder

    var locationState by mutableStateOf<LocationState>(LocationState.NoPermission)
    val locationAutofill = mutableStateListOf<AutocompleteResult>()

    var currentLatLong by mutableStateOf(LatLng(0.0, 0.0))

    private var job: Job? = null

    fun searchPlaces(query: String) {
        job?.cancel()
        locationAutofill.clear()
        job = viewModelScope.launch {
            val request = FindAutocompletePredictionsRequest.builder().setQuery(query).build()
            placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
                locationAutofill += response.autocompletePredictions.map {
                    AutocompleteResult(
                        it.getFullText(null).toString(), it.placeId
                    )
                }
            }.addOnFailureListener {
                it.printStackTrace()
                println(it.cause)
                println(it.message)
            }
        }
    }

    fun getCoordinates(result: AutocompleteResult) {
        val placeFields = listOf(Place.Field.LAT_LNG)
        val request = FetchPlaceRequest.newInstance(result.placeId, placeFields)
        placesClient.fetchPlace(request).addOnSuccessListener {
            if (it != null) {
                currentLatLong = it.place.latLng!!
            }
        }.addOnFailureListener {
            it.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        locationState = LocationState.LocationLoading
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                locationState =
                    if (location == null && locationState !is LocationState.LocationAvailable) {
                        LocationState.Error
                    } else {
                        currentLatLong = LatLng(location.latitude, location.longitude)
                        LocationState.LocationAvailable(
                            LatLng(
                                location.latitude,
                                location.longitude
                            )
                        )
                    }
            }
    }

    var text by mutableStateOf("")

    fun getAddress(latLng: LatLng) {
        viewModelScope.launch {
            val address = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            text = address?.get(0)?.getAddressLine(0).toString()
        }
    }

    fun requestLocationEnable(activity: Activity) {
        val locationRequest = LocationRequest.create()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        LocationServices.getSettingsClient(activity).checkLocationSettings(builder.build())
            .addOnSuccessListener {
                if (it.locationSettingsStates?.isLocationPresent == true) {
                    getCurrentLocation()
                }
            }.addOnFailureListener {
                if (it is ResolvableApiException) {
                    try {
                        it.startResolutionForResult(activity, 999)
                    } catch (e: IntentSender.SendIntentException) {
                        e.printStackTrace()
                    }
                }
            }
    }
}