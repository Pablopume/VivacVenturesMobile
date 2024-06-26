package com.example.vivacventuresmobile.ui.screens.addplace

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.ui.screens.addLocation.AddLocationScreen
import com.example.vivacventuresmobile.ui.screens.addimages.AddImages
import com.example.vivacventuresmobile.ui.screens.map.LoadingAnimation
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun AddPlaceScreen(
    viewModel: AddPlaceViewModel = hiltViewModel(),
    vivacPlace: Int,
    bottomNavigationBar: @Composable () -> Unit = {},
    onAddDone: () -> Unit,
    onUpdateDone: () -> Unit,
    username: String
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    if (state.value.place.username == "") {
        viewModel.handleEvent(AddPlaceEvent.AddUsername(username, vivacPlace))
    }


    Column {
        if (state.value.cambioPantalla == 0) {
            AddPlace(
                state.value,
                { viewModel.handleEvent(AddPlaceEvent.ErrorVisto) },
                bottomNavigationBar,
                { viewModel.handleEvent(AddPlaceEvent.DetailsCompleted()) },
                { viewModel.handleEvent(AddPlaceEvent.OnNameChange(it)) },
                { viewModel.handleEvent(AddPlaceEvent.OnDescriptionChange(it)) },
                { viewModel.handleEvent(AddPlaceEvent.OnTypeChange(it)) },
                { viewModel.handleEvent(AddPlaceEvent.OnDateChange(it)) },
                { viewModel.handleEvent(AddPlaceEvent.OnCapacityChange(it)) },
                { viewModel.handleEvent(AddPlaceEvent.OnPriceChange(it)) },
            )

        } else if (state.value.cambioPantalla == 1) {
            AddLocationScreen(
                addplacestate = state.value,
                vuelta = { viewModel.handleEvent(AddPlaceEvent.VueltaLocation()) },
                toImages = { viewModel.handleEvent(AddPlaceEvent.LocationCompleted()) },
                onLocationChange = { viewModel.handleEvent(AddPlaceEvent.OnLocationChange(it)) },
                bottomNavigationBar = bottomNavigationBar,
                exists = state.value.exists
            )

        } else {
            AddImages(
                state.value,
                { viewModel.handleEvent(AddPlaceEvent.ErrorVisto) },
                bottomNavigationBar,
                onAddDone,
                onUpdateDone,
                { viewModel.handleEvent(AddPlaceEvent.AddUri(it)) },
                { index, isImage ->
                    viewModel.handleEvent(
                        AddPlaceEvent.DeleteUri(
                            index,
                            isImage
                        )
                    )
                },
                { viewModel.handleEvent(AddPlaceEvent.AddPlace()) },
                { viewModel.handleEvent(AddPlaceEvent.UpdatePlace()) },
                { viewModel.handleEvent(AddPlaceEvent.Vuelta()) },
                state.value.exists
            )
        }
    }
}

@Composable
fun AddPlace(
    state: AddPlaceState,
    errorVisto: () -> Unit,
    bottomNavigationBar: @Composable () -> Unit,
    onDetailsCompleted: () -> Unit,
    onNameChange: (String) -> Unit,
    onDesciptionChange: (String) -> Unit,
    onTypeChange: (String) -> Unit,
    onDateChange: (LocalDate) -> Unit,
    onCapacityChange: (Int) -> Unit,
    onPriceChange: (String) -> Unit,
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.medium_padding))) {
                    Text(text = stringResource(id = R.string.name))
                    NameField(state.place.name, onNameChange)
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    Text(text = stringResource(id = R.string.description))
                    DescriptionField(state.place.description, onDesciptionChange)
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))

                    Text(text = stringResource(id = R.string.type))
                    TipoPicker(state.place.type, onTypeChange)
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    Text(text = stringResource(id = R.string.date))
                    DatePickerField(state.place.date, onDateChange)
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    Text(text = stringResource(id = R.string.capacity))
                    CapacityField(state.place.capacity, onCapacityChange)
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    if (state.place.type != stringResource(R.string.vivac) && state.place.type != stringResource(R.string.refuge) && state.place.type.isNotEmpty()){
                        Text(text = stringResource(id = R.string.price))
                        PriceField(state.place.price, onPriceChange)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = dimensionResource(id = R.dimen.medium_padding))
                ) {
                    ContinueButton(onDetailsCompleted)
                }
            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(date: LocalDate, onDateChange: (LocalDate) -> Unit) {
    val openDialog = remember { mutableStateOf(false) }
    Text(
        text = if (date == LocalDate.MIN) stringResource(id = R.string.select_date) else date.toString(),
        modifier = Modifier.clickable { openDialog.value = true }
    )
    if (openDialog.value) {
        val datePickerState = rememberDatePickerState()
        val confirmEnabled = remember {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }
        DatePickerDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        val instant = datePickerState.selectedDateMillis?.let {
                            Instant.ofEpochMilli(
                                it
                            )
                        }
                        val localDate = instant?.atZone(ZoneId.systemDefault())?.toLocalDate()
                        if (localDate != null) {
                            onDateChange(localDate)
                        }
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun PriceField(price: Double, onPriceChange: (String) -> Unit) {
    val priceStr = if (price == 0.0) "" else price.toString()
    OutlinedTextField(
        value = priceStr,
        onValueChange = {
            if (it.toDoubleOrNull() != null) {
                onPriceChange(it)
            } else if (it.isEmpty()) {
                onPriceChange("0")
            }
        },
        placeholder = { Text(stringResource(id = R.string.price_)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
    )
}

@Composable
fun CapacityField(capacity: Int, onCapacityChange: (Int) -> Unit) {
    val capacityStr = if (capacity == 0) "" else capacity.toString()
    OutlinedTextField(
        value = capacityStr,
        onValueChange = {
            if (it.isNotEmpty()) {
                onCapacityChange(it.toInt())
            } else {
                onCapacityChange(0)
            }
        },
        placeholder = { Text(stringResource(id = R.string.capacity_)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
    )
}

@Composable
fun TipoPicker(type: String, onTypeChange: (String) -> Unit, defaultValue: String = stringResource(R.string.select_type)) {
    val options = listOf(stringResource(R.string.vivac), stringResource(R.string.refuge), stringResource(R.string.private_refuge), stringResource(R.string.hostel), stringResource(R.string.other))

    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(type.ifEmpty { defaultValue }) }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = null
        )
        Text(text = selectedType, modifier = Modifier.clickable { expanded = true })
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = {
            expanded = false
        },
    ) {
        options.forEach { selectionOption ->
            DropdownMenuItem(
                onClick = {
                    selectedType = selectionOption
                    onTypeChange(selectionOption)
                    expanded = false
                },
                text = { Text(text = selectionOption) }
            )
        }
    }
}

@Composable
fun DescriptionField(description: String, onDesciptionChange: (String) -> Unit) {
    OutlinedTextField(
        value = description,
        onValueChange = onDesciptionChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        placeholder = { Text(stringResource(id = R.string.description_)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        singleLine = false,
        maxLines = 5,
    )
}

@Composable
fun NameField(name: String, onNameChange: (String) -> Unit) {
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(id = R.string.name_)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        singleLine = true,
        maxLines = 1,
    )
}

@Composable
fun ContinueButton(
    onAddPlaceClick: () -> Unit,

    ) {
    FloatingActionButton(onClick = {
        onAddPlaceClick()
    }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.small_padding))
        ) {
            Text(text = stringResource(id = R.string.continuar))
        }

    }
}

@Composable
fun AddButton(
    onAddPlaceClick: () -> Unit,
) {
    FloatingActionButton(onClick = {
        onAddPlaceClick()
    }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.small_padding))
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.add)
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.smallmedium_padding)))
            Text(text = stringResource(id = R.string.addplace))
        }

    }
}

@Preview
@Composable
fun AddPlacePreview() {
    AddPlace(
        state = AddPlaceState(),
        errorVisto = {},
        bottomNavigationBar = {},
        onDetailsCompleted = {},
        onNameChange = {},
        onDesciptionChange = {},
        onTypeChange = {},
        onDateChange = {},
        onCapacityChange = {},
        onPriceChange = {},
    )
}