package com.example.vivacventuresmobile.ui.screens.detalleplace

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.ModeNight
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.ui.screens.map.LoadingAnimation

@Composable
fun DetallePlaceScreen(
    viewModel: DetallePlaceViewModel = hiltViewModel(),
    placeId: Int,
//    onBack: () -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {},
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    if (state.value.vivacPlace == null) {
        viewModel.handleEvent(DetallePlaceEvent.GetDetalle(placeId))
    }

    DetallePlace(
        state.value,
        { viewModel.handleEvent(DetallePlaceEvent.ErrorVisto) },
        bottomNavigationBar
    )
}

@Composable
fun DetallePlace(
    state: DetallePlaceState,
    errorVisto: () -> Unit,
    bottomNavigationBar: @Composable () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = bottomNavigationBar
    ) {innerPadding ->
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
            LoadingAnimation(state.loading)
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.medium_padding))
            ) {
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                state.vivacPlace?.let { Text(text = it.id.toString()) }

            }
        }

    }

}

