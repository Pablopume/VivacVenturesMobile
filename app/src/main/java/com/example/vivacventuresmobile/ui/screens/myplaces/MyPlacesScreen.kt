package com.example.vivacventuresmobile.ui.screens.myplaces

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.ui.screens.map.LoadingAnimation
import com.example.vivacventuresmobile.ui.screens.myfavourites.VivacPlaceListItem

@Composable
fun MyPlacesScreen(
    viewModel: MyPlacesViewModel = hiltViewModel(),
    username: String,
    onViewDetalle: (Int) -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {},
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    if (state.value.username.isEmpty()) {
        viewModel.handleEvent(MyPlacesEvent.SaveUsername(username))
    }

    PantallaMyPlaces(
        state.value,
        { viewModel.handleEvent(MyPlacesEvent.ErrorVisto) },
        onViewDetalle,
        bottomNavigationBar
    )
}

@Composable
fun PantallaMyPlaces(
    state: MyPlacesState,
    errorVisto: () -> Unit,
    onViewDetalle: (Int) -> Unit,
    bottomNavigationBar: @Composable () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = bottomNavigationBar
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
        Column {
            if (state.loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.8f),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingAnimation(state.loading)
                }
            } else {
                if (state.vivacPlaces.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Empieza a crear lugares!",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                } else{
                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        items(
                            items = state.vivacPlaces,
                            key = { vivacPlace -> vivacPlace.id }
                        ) { vivacPlace ->
                            VivacPlaceListItem(
                                vivacPlace = vivacPlace,
                                onViewDetalle = onViewDetalle
                            )
                        }
                    }
                }
            }
        }
    }
}

