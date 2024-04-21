package com.example.vivacventuresmobile.ui.screens.addplace

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
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
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberImagePainter
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.ui.screens.map.LoadingAnimation
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun AddPlaceScreen(
    viewModel: AddPlaceViewModel = hiltViewModel(),
    bottomNavigationBar: @Composable () -> Unit = {},
    onAddDone: () -> Unit
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    AddPlace(
        state.value,
        { viewModel.handleEvent(AddPlaceEvent.ErrorVisto) },
        bottomNavigationBar,
        onAddDone,
        { viewModel.handleEvent(AddPlaceEvent.AddPlace()) },
        { viewModel.handleEvent(AddPlaceEvent.OnNameChange(it)) },
        { viewModel.handleEvent(AddPlaceEvent.OnDescriptionChange(it)) },
        { viewModel.handleEvent(AddPlaceEvent.OnPicturesChange(it)) },
        { viewModel.handleEvent(AddPlaceEvent.OnTypeChange(it)) },
        { viewModel.handleEvent(AddPlaceEvent.OnDateChange(it)) },
        { viewModel.handleEvent(AddPlaceEvent.OnCapacityChange(it)) },
    )


}

@Composable
fun AddPlace(
    state: AddPlaceState,
    errorVisto: () -> Unit,
    bottomNavigationBar: @Composable () -> Unit,
    onAddDone: () -> Unit,
    onAddPlaceClick: () -> Unit,
    onNameChange: (String) -> Unit,
    onDesciptionChange: (String) -> Unit,
    onPicturesChange: (List<Uri>) -> Unit,
    onTypeChange: (String) -> Unit,
    onDateChange: (LocalDate) -> Unit,
    onCapacityChange: (Int) -> Unit,

    ) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(state.addPlaceDone) {
        if (state.addPlaceDone) {
            onAddDone()
        }
    }

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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.medium_padding))) {
                    Text(text = stringResource(id = R.string.nombre))
                    NameField(state.place.name, onNameChange)
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    Text(text = stringResource(id = R.string.descripcion))
                    DescriptionField(state.place.description, onDesciptionChange)
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    Text(text = stringResource(id = R.string.tipo))
                    TipoPicker(state.place.type, onTypeChange)
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    Text(text = stringResource(id = R.string.capacidad))
                    CapacityField(state.place.capacity, onCapacityChange)
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    Text(text = stringResource(id = R.string.fecha))
                    DatePickerField(state.place.date, onDateChange)
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    Text(text = stringResource(id = R.string.fotos))
                    PicturePicker(state.place.images, onPicturesChange)
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = dimensionResource(id = R.dimen.medium_padding))
                ) {
                    AddButton(onAddPlaceClick)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(date: LocalDate, onDateChange: (LocalDate) -> Unit) {
    val openDialog = remember { mutableStateOf(false) }
    //Text con on click que pone el openDialog a true
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
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun CapacityField(capacity: Int, onCapacityChange: (Int) -> Unit) {
    TextField(
        value = capacity.toString(),
        onValueChange = { onCapacityChange(it.toInt()) },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(id = R.string.capacidad)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
    )
}

@Composable
fun TipoPicker(type: String, onTypeChange: (String) -> Unit) {
    val options = listOf("Vivac", "Refugio", "Refugio abierto", "Albergue", "Other")

    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = null
        )
        Text(text = type, modifier = Modifier.clickable { expanded = true })
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
                    onTypeChange(selectionOption)
                    expanded = false
                },
                text = { Text(text = selectionOption) }
            )
        }
    }
}

@Composable
fun PicturePicker(images: List<String>, onPicturesChange: (List<Uri>) -> Unit) {
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> onPicturesChange(listOf(uri) as List<Uri>) }
    )
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris -> onPicturesChange(uris)}
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = {
                    singlePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) {
                    Text(text = "Pick one photo")
                }
                Button(onClick = {
                    multiplePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }) {
                    Text(text = "Pick multiple photo")
                }
            }
        }

        items(images) { imageUrl ->
            val painter = rememberImagePainter(data = imageUrl)

            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
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
        placeholder = { Text(stringResource(id = R.string.descripcion_)) },
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
        placeholder = { Text(stringResource(id = R.string.nombre_)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        singleLine = true,
        maxLines = 1,
    )
}

@Composable
fun AddButton(onAddPlaceClick: () -> Unit) {
    FloatingActionButton(onClick = { onAddPlaceClick() }) {
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