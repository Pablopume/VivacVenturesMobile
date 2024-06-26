package com.example.vivacventuresmobile.ui.screens.listplaces

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.ui.screens.myfavourites.VivacPlaceListItem
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.libraries.places.api.Places

@Composable
fun ListPlacesScreen(
    viewModel: ListPlacesViewModel = hiltViewModel(),
    onViewDetalle: (Int) -> Unit,
    username: String,
    bottomNavigationBar: @Composable () -> Unit = {},
    onAddPlace: (Int) -> Unit
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    viewModel.placesClient = Places.createClient(LocalContext.current)

    if (state.value.username.isEmpty()) {
        viewModel.handleEvent(ListPlacesEvent.SaveUsername(username))
    }

    LaunchedEffect(Unit) {
        viewModel.handleEvent(ListPlacesEvent.GetVivacPlaces())
    }

    ListPlaces(
        state.value,
        viewModel,
        { viewModel.handleEvent(ListPlacesEvent.ErrorVisto) },
        onViewDetalle,
        { viewModel.handleEvent(ListPlacesEvent.GetVivacPlacesByType(it)) },
        bottomNavigationBar,
        onAddPlace,
        { viewModel.handleEvent(ListPlacesEvent.SearchPlaces(it)) },
    )
}


@Composable
fun ListPlaces(
    state: ListPlacesState,
    viewModel: ListPlacesViewModel,
    errorVisto: () -> Unit,
    onViewDetalle: (Int) -> Unit,
    onGetVivacPlacesByType: (String) -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {},
    onAddPlace: (Int) -> Unit,
    searchPlaces: (String) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = bottomNavigationBar,
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddPlace(0) }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add)
                )
            }
        }
    ) { innerPadding ->
        val messageDismiss = stringResource(R.string.dismiss)
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

        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {

                var searchText by remember { mutableStateOf("") }
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = {
                            searchText = it
                            searchPlaces(it)
                        },
                        label = { Text(stringResource(R.string.searcher)) },
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .weight(0.68f),
                    )
                    Box(modifier = Modifier.align(Alignment.CenterVertically).weight(0.32f)) {
                        DropDown(onGetVivacPlacesByType = onGetVivacPlacesByType)
                    }
                }
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
                                    searchText = it.address
                                    viewModel.locationAutofill.clear()
                                    viewModel.getCoordinates(it)
                                }) {
                                Text(it.address)
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }
            val swipeRefreshState = rememberSwipeRefreshState(state.loading)
            SwipeRefresh(
                state = swipeRefreshState,
                onRefresh = {}
            ) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDown(onGetVivacPlacesByType: (String) -> Unit) {

    var isExpanded by remember { mutableStateOf(false) }
    var typeSelect by remember {
        mutableStateOf("")
    }

    val list = listOf(
        stringResource(R.string.vivac),
        stringResource(R.string.refuge),
        stringResource(R.string.hostel),
        stringResource(R.string.private_refuge),
        stringResource(R.string.all)
    )

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
                    Icon(
                        Icons.Filled.FilterAlt,
                        contentDescription = stringResource(R.string.place)
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondary,
                ),
                modifier = Modifier
                    .width(100.dp)
                    .menuAnchor()
            )

            val messageAll = stringResource(R.string.all)
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
                            typeSelect = if (type == messageAll) {
                                ""
                            } else {
                                list[index]
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

@Preview
@Composable
fun previewDropDown() {
    DropDown(onGetVivacPlacesByType = {})
}