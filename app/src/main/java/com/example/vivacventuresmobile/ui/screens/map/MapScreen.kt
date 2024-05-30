package com.example.vivacventuresmobile.ui.screens.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.ModeNight
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.data.preferences.CryptoManager
import com.example.vivacventuresmobile.ui.MainActivity
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerInfoWindow
import com.google.maps.android.compose.MarkerState

private var locationRequired: Boolean = false
private val permissions = arrayOf(
    android.Manifest.permission.ACCESS_FINE_LOCATION,
    android.Manifest.permission.ACCESS_COARSE_LOCATION,
)

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
    onViewDetalle: (Int) -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {},
    username: String,
    password: String,
    cryptoManager: CryptoManager
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.handleEvent(MapEvent.GetAll)
    }

    viewModel.handleEvent(
        MapEvent.StartLocationUpdates(
            LocationServices.getFusedLocationProviderClient(
                LocalContext.current
            )
        )
    )

    LaunchedEffect(state.value.relogin) {
        if (state.value.relogin) {
            val passworddecrypted = cryptoManager.desencriptar(password)
            if (username != "" && passworddecrypted != "") {
                viewModel.handleEvent(
                    MapEvent.reLogin(
                        username,
                        passworddecrypted
                    )
                )
            }
        }
    }

    Maps(
        state.value,
        { viewModel.handleEvent(MapEvent.ErrorVisto) },
        onViewDetalle,
        bottomNavigationBar,
        { viewModel.handleEvent(MapEvent.ToggleDarkMap) },
        { viewModel.handleEvent(MapEvent.UpdateCameraPosition(it)) },
        { viewModel.handleEvent(MapEvent.LocationOff) },
        { viewModel.handleEvent(MapEvent.LocationOn) },
        { viewModel.handleEvent(MapEvent.SendError(it)) },
    )

}

@Composable
fun Maps(
    state: MapState,
    errorVisto: () -> Unit,
    onViewDetalle: (Int) -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {},
    toggleDarkMap: () -> Unit,
    updateCameraPosition: (LatLng) -> Unit,
    stopLocationUpdates: () -> Unit,
    startLocationUpdates: () -> Unit,
    sendError: (String) -> Unit,
    permissionsGranted: Boolean = MainActivity.checkPermissions(LocalContext.current),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val uiSettings = remember {
        MapUiSettings(
            myLocationButtonEnabled = true,
            zoomControlsEnabled = false,
            compassEnabled = true
        )
    }

    val messageGranted = stringResource(R.string.location_permission_granted)
    val messageDenied = stringResource(R.string.location_permission_denied)
    val launcherMultiplePermissions = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionMaps ->
        val areGranted = permissionMaps.values.reduce { acc, next -> acc && next }
        if (areGranted) {
            locationRequired = true
            startLocationUpdates()
            sendError(messageGranted)
        } else {
            sendError(messageDenied)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = bottomNavigationBar,
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            Box(
                Modifier.fillMaxWidth()
            ) {
                if (state.isLocationEnabled) {
                    FloatingActionButton(
                        onClick = {
                            stopLocationUpdates()
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = stringResource(R.string.toggle_location)
                        )
                    }
                } else {
                    FloatingActionButton(
                        onClick = {
                            if (permissionsGranted) {
                                locationRequired = true
                                startLocationUpdates()
                            } else {
                                launcherMultiplePermissions.launch(permissions)
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = stringResource(R.string.toggle_location)
                        )
                    }
                }
                FloatingActionButton(
                    onClick = { toggleDarkMap() },
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = if (state.isDarkMap) Icons.Default.ModeNight else Icons.Default.LightMode,
                        contentDescription = stringResource(R.string.toggle_dark_map)
                    )
                }
            }
        }
    ) { innerPadding ->

        val messageDismiss = stringResource(id = R.string.dismiss)
        LaunchedEffect(state.error) {
            state.error?.let {
                snackbarHostState.showSnackbar(
                    message = state.error.toString(),
                    actionLabel = messageDismiss,
                    duration = SnackbarDuration.Short,
                )
                errorVisto()
            }
        }
        if (state.loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingAnimation(state.loading)
            }
        } else {
            val cameraPositionState = remember { CameraPositionState() }
            LaunchedEffect(state.currentLatLng) {
                if (!state.isLocationEnabled && state.currentLocation != state.currentLatLng) {
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(
                            state.currentLatLng,
                            10f
                        )
                    )
                } else if (state.isLocationEnabled && state.currentLocation == state.currentLatLng) {
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(
                            state.currentLatLng,
                            15f
                        )
                    )
                } else if (state.isLocationEnabled) {
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(
                            state.currentLatLng,
                            10f
                        )
                    )
                }
                if (state.currentLatLng == LatLng(40.42966863252524, -3.6797065289867783)) {
                    cameraPositionState.animate(
                        CameraUpdateFactory.newLatLngZoom(
                            state.currentLatLng,
                            5.5f
                        )
                    )
                }
            }
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                properties = state.properties,
                cameraPositionState = cameraPositionState,
                uiSettings = uiSettings,
            ) {
                state.vivacPlaces.forEach { place ->

                    if (state.isLocationEnabled && state.currentLocation != null) {
                        Marker(
                            state = MarkerState(position = state.currentLocation),
                            title = stringResource(R.string.current_location),
                            snippet = stringResource(R.string.this_is_current_location),
                            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                        )
                    }
                    Marker(
                        state = MarkerState(position = LatLng(place.lat, place.lon)),
                        title = place.name,
                        snippet = stringResource(R.string.click_to_details),
                        onInfoWindowClick = {
                            onViewDetalle(place.id)
                        },
                        onClick = {
                            it.showInfoWindow()
                            updateCameraPosition(LatLng(place.lat, place.lon))
                            true
                        },
                        icon = when (place.type) {
                            stringResource(R.string.vivac) -> bitmapDescriptorFromVector(
                                LocalContext.current,
                                R.drawable.pin1
                            )

                            stringResource(R.string.refuge) -> bitmapDescriptorFromVector(
                                LocalContext.current,
                                R.drawable.pin2
                            )

                            stringResource(R.string.private_refuge) -> bitmapDescriptorFromVector(
                                LocalContext.current,
                                R.drawable.pin3
                            )

                            stringResource(R.string.hostel) -> bitmapDescriptorFromVector(
                                LocalContext.current,
                                R.drawable.pin4
                            )

                            else -> bitmapDescriptorFromVector(
                                LocalContext.current,
                                R.drawable.pin5
                            ) // default color
                        }
                    )
                }
            }

        }
    }
}

@Composable
fun LoadingAnimation(visible: Boolean) {
    val enterExit =
        rememberInfiniteTransition(label = stringResource(id = R.string.loadinganimation))
    val alpha by enterExit.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = stringResource(id = R.string.loadinganimationalpha)
    )

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = alpha)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
    val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
    vectorDrawable?.setBounds(0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight)
    val bitmap = Bitmap.createBitmap(
        vectorDrawable!!.intrinsicWidth,
        vectorDrawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}
