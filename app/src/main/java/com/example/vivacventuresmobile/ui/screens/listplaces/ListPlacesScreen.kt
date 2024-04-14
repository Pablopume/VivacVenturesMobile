package com.example.vivacventuresmobile.ui.screens.listplaces

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.domain.modelo.VivacPlace
import com.example.vivacventuresmobile.ui.screens.map.LoadingAnimation
import com.example.vivacventuresmobile.ui.theme.BlueRefugee
import com.example.vivacventuresmobile.ui.theme.GreenVivac
import com.example.vivacventuresmobile.ui.theme.RedAlbergue
import com.example.vivacventuresmobile.ui.theme.YellowRefugee
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
        { viewModel.handleEvent(ListPlacesEvent.GetVivacPlacesByType(it)) },
        bottomNavigationBar
    )
}


@Composable
fun ListPlaces(
    state: ListPlacesState,
    errorVisto: () -> Unit,
    onViewDetalle: (Int) -> Unit,
    onGetVivacPlacesByType: (String) -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {},
) {
    var isExpanded by remember { mutableStateOf(false) }
    var typeSelect by remember {
        mutableStateOf("")
    }
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
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Barra de búsqueda
                Text(
                    text = "Buscador",
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterStart)
                        .padding(horizontal = 8.dp),
                )

                Box(modifier = Modifier.align(Alignment.TopEnd)) {
                    DropDown(onGetVivacPlacesByType = onGetVivacPlacesByType)
                }
            }
//            DropDown(onGetVivacPlacesByType)
            if (state.loading) {
                Box(modifier =  Modifier.fillMaxWidth().weight(0.8f), contentAlignment = Alignment.Center) {
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
                modifier = Modifier.width(100.dp).menuAnchor()
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
            .clickable { onViewDetalle(vivacPlace.id) },
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
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
        bottomNavigationBar = {}
    )
}
