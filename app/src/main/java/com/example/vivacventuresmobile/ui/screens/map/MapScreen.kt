package com.example.vivacventuresmobile.ui.screens.map

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
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ToggleOff
import androidx.compose.material.icons.filled.ToggleOn
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.common.Constantes
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel(),
    onViewDetalle: (Int) -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {},
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    Maps(
        state.value,
        { viewModel.handleEvent(MapEvent.ErrorVisto) },
        onViewDetalle,
        bottomNavigationBar,
        { viewModel.handleEvent(MapEvent.ToggleDarkMap) },
        { viewModel.handleEvent(MapEvent.UpdateCameraPosition(it)) }
    )

}

@Composable
fun Maps(
    state: MapState,
    errorVisto: () -> Unit,
    onViewDetalle: (Int) -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {},
    toggleDarkMap: () -> Unit,
    updateCameraPosition: (LatLng) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val uiSettings = remember {
        MapUiSettings(zoomControlsEnabled = false)
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = bottomNavigationBar,
        floatingActionButton = {
            FloatingActionButton(onClick = {
                toggleDarkMap()
            }) {
                Icon(
                    imageVector = if (state.isDarkMap) Icons.Default.ToggleOff else Icons.Default.ToggleOn,
                    contentDescription = "Toggle Dark map"
                )
            }
        }
    ) { innerPadding ->
        LaunchedEffect(state.error) {
            state.error?.let {
                snackbarHostState.showSnackbar(
                    message = state.error.toString(),
                    actionLabel = Constantes.DISMISS,
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
            //TODO animar la camara
//            val cameraPositionState = rememberCameraPositionState {
//                position = state.cameraPositionState.position
//            }
//
//            LaunchedEffect(cameraPositionState.position) {
//                cameraPositionState.animate(
//                    update = CameraUpdateFactory.newCameraPosition(
//                        CameraPosition(cameraPositionState.position.target, 15f, 0f, 0f)
//                    ),
//                    durationMs = 1000
//                )
//            }
            GoogleMap(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                properties = state.properties,
                cameraPositionState = state.cameraPositionState,
                uiSettings = uiSettings,
            ) {
                state.vivacPlaces.forEach { place ->
                    Marker(
                        state = MarkerState(position = LatLng(place.lat, place.lon)),
                        title = place.name,
                        snippet = "Click to see details",
                        onInfoWindowClick = {
                            onViewDetalle(place.id)
                        },
                        onClick = {
                            it.showInfoWindow()
                            updateCameraPosition(LatLng(place.lat, place.lon))
                            true
                        },
                        icon = when (place.type) {
                            "Vivac" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                            "Refugio" -> BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_BLUE
                            )

                            "Refugio abierto" -> BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_MAGENTA
                            )

                            else -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW) // default color
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
