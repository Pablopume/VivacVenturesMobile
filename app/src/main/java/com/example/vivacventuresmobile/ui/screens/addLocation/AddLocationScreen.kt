package com.example.vivacventuresmobile.ui.screens.addLocation

import android.app.Activity
import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.ui.screens.addplace.AddPlaceState
import com.example.vivacventuresmobile.ui.screens.map.LoadingAnimation
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition

import com.google.android.libraries.places.api.Places
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

private val permissions = arrayOf(
    android.Manifest.permission.ACCESS_FINE_LOCATION,
    android.Manifest.permission.ACCESS_COARSE_LOCATION,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLocationScreen(
    viewModel: LocationViewModel = hiltViewModel(),
    addplacestate: AddPlaceState,
    vuelta: () -> Unit,
    toImages: () -> Unit,
    onLocationChange: (LatLng) -> Unit,
    bottomNavigationBar: @Composable () -> Unit,
) {
    val locationstate = viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(LocalContext.current)
    viewModel.placesClient = Places.createClient(LocalContext.current)
    viewModel.geoCoder = Geocoder(LocalContext.current)


    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionMaps ->
        val areGranted = permissionMaps.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            viewModel.getCurrentLocation()
            viewModel.handleEvent(AddLocationEvent.SendError("Location permission granted"))
        } else {
            viewModel.locationState = LocationState.LocationDisabled
            viewModel.handleEvent(AddLocationEvent.SendError("Location permission denied"))
        }
    }

    val context = LocalContext.current

    AnimatedContent(
        viewModel.locationState
    ) { state ->
        when (state) {
            is LocationState.NoPermission -> {
                Column {
                    Text("We need location permission to continue")
                    Button(onClick = { launcherMultiplePermissions.launch(permissions) }) {
                        Text("Request permission")
                    }
                }
            }

            is LocationState.LocationDisabled -> {
                Column {
                    Text("We need location to continue")
                    Button(onClick = {
                        viewModel.requestLocationEnable(context as Activity)
                    }) {
                        Text("Enable location")
                    }
                }
            }

            is LocationState.LocationLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingAnimation(true)
                }
            }

            is LocationState.Error -> {
                Column {
                    Text("Error fetching your location")
                    Button(onClick = { viewModel.getCurrentLocation() }) {
                        Text("Retry")
                    }
                }
            }

            is LocationState.LocationAvailable -> {
                val cameraPositionState = rememberCameraPositionState {
                    if (addplacestate.place.lat != 0.0) {
                        val defaultLocation = LatLng(addplacestate.place.lat, addplacestate.place.lon)
                        position = CameraPosition.fromLatLngZoom(defaultLocation, 15f)
                    } else {
                        position = CameraPosition.fromLatLngZoom(state.cameraLatLang, 15f)
                    }
                }

                val mapUiSettings by remember { mutableStateOf(MapUiSettings()) }
                val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }
                val scope = rememberCoroutineScope()

                LaunchedEffect(viewModel.currentLatLong) {
                    cameraPositionState.animate(CameraUpdateFactory.newLatLng(viewModel.currentLatLong))
                }

                LaunchedEffect(cameraPositionState.isMoving) {
                    if (!cameraPositionState.isMoving) {
                        viewModel.getAddress(cameraPositionState.position.target)
                    }
                }
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(locationstate.value.error) {
                    locationstate.value.error?.let {
                        snackbarHostState.showSnackbar(
                            message = locationstate.value.error.toString(),
                            actionLabel = Constantes.DISMISS,
                            duration = SnackbarDuration.Short,
                        )
                        viewModel.handleEvent(AddLocationEvent.ErrorVisto)
                    }
                }

                Scaffold(
                    snackbarHost = { snackbarHostState },
                    bottomBar = { bottomNavigationBar() }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        GoogleMap(modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                            cameraPositionState = cameraPositionState,
                            uiSettings = mapUiSettings,
                            properties = mapProperties,
                            onMapClick = {
                                scope.launch {
                                    cameraPositionState.animate(CameraUpdateFactory.newLatLng(it))
                                }
                            })
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.Center)
                        )

                        Surface(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(innerPadding)
                                .fillMaxWidth(),
                            color = Color.White,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                AnimatedVisibility(
                                    viewModel.locationAutofill.isNotEmpty(),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                ) {
                                    LazyColumn(
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        items(viewModel.locationAutofill) {
                                            Row(modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp)
                                                .clickable {
                                                    viewModel.text = it.address
                                                    viewModel.locationAutofill.clear()
                                                    viewModel.getCoordinates(it)
                                                }) {
                                                Text(it.address, color = Color.Black)
                                            }
                                        }
                                    }
                                    Spacer(Modifier.height(16.dp))
                                }
                                OutlinedTextField(
                                    value = viewModel.text, onValueChange = {
                                        viewModel.text = it
                                        viewModel.searchPlaces(it)
                                    }, modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        focusedBorderColor = Color.Black,
                                        unfocusedBorderColor = Color.Black,
                                        focusedTextColor = Color.Black,
                                        unfocusedTextColor = Color.Black,
                                    ),
                                )
                                Row {
                                    Button(onClick = {
                                        vuelta()
                                    }) {
                                        Text("Volver")
                                    }
                                    Button(onClick = {
                                        onLocationChange(cameraPositionState.position.target)
                                        toImages()
                                    }) {
                                        Text("Confirmar ubicación")
                                    }
                                }
                            }
                        }
                    }
                }


            }
        }


    }
}