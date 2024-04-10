package com.example.vivacventuresmobile.ui.screens.listplaces

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.domain.modelo.VivacPlace
import com.example.vivacventuresmobile.ui.screens.map.LoadingAnimation
import java.time.LocalDate

@Composable
fun ListPlacesScreen(
    viewModel: ListPlacesViewModel = hiltViewModel(),
    onViewDetalle: (Int) -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {},
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    ListPlaces(
        state.value,
        { viewModel.handleEvent(ListPlacesEvent.ErrorVisto) },
        onViewDetalle,
        bottomNavigationBar
    )
}

@Composable
fun ListPlaces(
    state: ListPlacesState,
    errorVisto: () -> Unit,
    onViewDetalle: (Int) -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {},
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
        if (state.loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingAnimation(state.loading)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                items(
                    items = state.vivacPlaces,
                    key = { vivacPlace -> vivacPlace.id }) { vivacPlace ->
                    VivacPlaceItem(
                        vivacPlace = vivacPlace,
                        onViewDetalle = onViewDetalle
                    )
                }
            }
        }

    }

}

@Composable
fun VivacPlaceItem(
    vivacPlace: VivacPlace,
    onViewDetalle: (Int) -> Unit,
    modifier: Modifier = Modifier.background(MaterialTheme.colorScheme.background)
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.smallmedium_padding))
            .clickable { onViewDetalle(vivacPlace.id) }
    ) {
        Row(modifier = Modifier.padding(dimensionResource(id = R.dimen.small_padding))) {
            Text(
                text = vivacPlace.name,
                modifier = Modifier.weight(weight = 0.5F)
            )
            Text(
                text = vivacPlace.type,
                modifier = Modifier.weight(weight = 0.5F)
            )

        }
        Row(modifier = Modifier.padding(dimensionResource(id = R.dimen.smallmedium_padding))) {
            Text(
                text = vivacPlace.description,
                modifier = Modifier.weight(weight = 0.8F)
            )
        }
    }
}

@Preview
@Composable
fun PreviewVivacPlaceItem() {
    VivacPlaceItem(
        vivacPlace = VivacPlace(
            id = 1,
            name = "Vivac Place",
            type = "Type",
            description = "Description",
            lat = 0.0,
            lon = 0.0,
            capacity = 0,
            date = LocalDate.now(),
            username = "Username",
        ),
        onViewDetalle = {}
    )
}
