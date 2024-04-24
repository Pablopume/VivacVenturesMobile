package com.example.vivacventuresmobile.ui.screens.listplaces

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.EmojiFoodBeverage
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.SingleBed
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.domain.modelo.VivacPlace
import com.example.vivacventuresmobile.ui.screens.map.LoadingAnimation
import com.example.vivacventuresmobile.ui.theme.BlueRefugee
import com.example.vivacventuresmobile.ui.theme.GreenVivac
import com.example.vivacventuresmobile.ui.theme.RedAlbergue
import com.example.vivacventuresmobile.ui.theme.YellowRefugee
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

@Composable
fun ListPlacesScreen(
    viewModel: ListPlacesViewModel = hiltViewModel(),
    onViewDetalle: (Int) -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {},
    onAddPlace: () -> Unit
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    if (!Places.isInitialized()) {
        Places.initialize(LocalContext.current, "AIzaSyAJhTuHWdTmBCIsJkZ-_QrwxmfPvw3Qx5I")
    }
    val placesClient = Places.createClient(LocalContext.current)
    ListPlaces(
        state.value,
        { viewModel.handleEvent(ListPlacesEvent.ErrorVisto) },
        onViewDetalle,
        { viewModel.handleEvent(ListPlacesEvent.GetVivacPlacesByType(it)) },
        bottomNavigationBar,
        onAddPlace,
        placesClient
    )
}


@Composable
fun ListPlaces(
    state: ListPlacesState,
    errorVisto: () -> Unit,
    onViewDetalle: (Int) -> Unit,
    onGetVivacPlacesByType: (String) -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {},
    onAddPlace: () -> Unit,
    placesClient: PlacesClient,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = bottomNavigationBar,
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddPlace() }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
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

        Column {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Barra de búsqueda
                val coroutineScope = rememberCoroutineScope()

                var searchText by remember { mutableStateOf("") }

                TextField(
                    value = searchText,
                    onValueChange = { value ->
                        searchText = value
                        // Buscar lugares con Google Places
                        coroutineScope.launch {
                            val places = searchPlaces(placesClient, searchText)
                            // Aquí puedes actualizar tu estado con los lugares encontrados
                        }

                    },
                    label = { Text("Buscador") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterStart)
                        .padding(horizontal = 8.dp),
                )

                Box(modifier = Modifier.align(Alignment.TopEnd)) {
                    DropDown(onGetVivacPlacesByType = onGetVivacPlacesByType)
                }
            }
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
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    items(
                        items = state.vivacPlaces,
                        key = { vivacPlace -> vivacPlace.id }
                    ) { vivacPlace ->
                        VivacPlaceItem(
                            vivacPlace = vivacPlace,
                            onViewDetalle = onViewDetalle
                        )
                    }
                }
            }
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDown(onGetVivacPlacesByType: (String) -> Unit) {

    var isExpanded by remember { mutableStateOf(false) }
    var typeSelect by remember {
        mutableStateOf("")
    }

    val list = listOf("Vivac", "Refugio", "Albergue", "Refugio Privado", "All")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.End

    ) {
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded }
        ) {
            TextField(
                value = "",
                onValueChange = {},
                readOnly = true,
                leadingIcon = {
                    Icon(Icons.Filled.FilterAlt, contentDescription = "Place Icon")
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                },
//                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier
                    .width(100.dp)
                    .menuAnchor()
//                modifier = Modifier
//                    .width(200.dp)
//                    .align(AbsoluteAlignment.Right)
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
            ) {
                list.forEachIndexed { index, type ->
                    DropdownMenuItem(
                        text = {
                            Text(text = type)
                        },
                        onClick = {
                            if (type == "All") {
                                typeSelect = ""
                            } else {
                                typeSelect = list[index]
                            }
                            isExpanded = false
                            onGetVivacPlacesByType(typeSelect)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
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
    val backgroundColor = when (vivacPlace.type) {
        "Vivac" -> GreenVivac
        "Refugio" -> BlueRefugee
        "Albergue" -> RedAlbergue
        "Refugio Privado" -> YellowRefugee
        else -> Color.Gray
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.smallmedium_padding))
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.medium_padding)))
            .clickable { onViewDetalle(vivacPlace.id) },
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(
//            containerColor = backgroundColor
            containerColor = MaterialTheme.colorScheme.onSurface
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.medium_padding))
                .fillMaxWidth()
        ) {
            val imageUrl = if (vivacPlace.images.isNotEmpty()) {
                vivacPlace.images[0]
            } else {
                "https://firebasestorage.googleapis.com/v0/b/vivacventures-b3fae.appspot.com/o/images%2Fdefault.jpg?alt=media&token=5ef9d6e8-c7b8-47ac-87a3-419d22857a70"
            }
            val image: Painter = rememberAsyncImagePainter(imageUrl)
            Image(
                modifier = Modifier
                    .size(80.dp, 80.dp)
                    .clip(RoundedCornerShape(16.dp)),
                painter = image,
                alignment = Alignment.CenterStart,
                contentDescription = "",
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))

            Column(modifier = Modifier.align(Alignment.CenterVertically).padding(8.dp)) {
                Text(
                    text = vivacPlace.name,
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = MaterialTheme.colorScheme.surface,
                    fontWeight = FontWeight.Bold,
                    style = typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = vivacPlace.type,
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = MaterialTheme.colorScheme.surface,
                    style = typography.labelMedium
                )

            }
        }
    }
}

suspend fun searchPlaces(placesClient: PlacesClient, query: String): List<AutocompletePrediction> {
    val request = FindAutocompletePredictionsRequest.builder()
        .setQuery(query)
        .build()

    val response = placesClient.findAutocompletePredictions(request).await()

    return response.autocompletePredictions
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

@Preview
@Composable
fun previewDropDown() {
    DropDown(onGetVivacPlacesByType = {})
}

@Preview(
    device = Devices.PIXEL_4_XL,
)
@Composable
fun previewLista() {
    ListPlaces(
        state = ListPlacesState(
            vivacPlaces = listOf(
                VivacPlace(
                    id = 1,
                    name = "Vivac Place",
                    type = "Type",
                    description = "Description",
                    lat = 0.0,
                    lon = 0.0,
                    capacity = 0,
                    date = LocalDate.now(),
                    username = "Username",
                )
            ),
            loading = false,
            error = null
        ),
        errorVisto = {},
        onViewDetalle = {},
        onGetVivacPlacesByType = {},
        bottomNavigationBar = {},
        onAddPlace = {},
        placesClient = Places.createClient(LocalContext.current)
    )
}

