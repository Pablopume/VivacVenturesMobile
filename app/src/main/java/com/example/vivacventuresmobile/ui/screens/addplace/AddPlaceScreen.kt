package com.example.vivacventuresmobile.ui.screens.addplace

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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.ui.screens.map.LoadingAnimation

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
        { viewModel.handleEvent(AddPlaceEvent.OnNameChange(it))},
        { viewModel.handleEvent(AddPlaceEvent.OnDescriptionChange(it))}
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
    onDesciptionChange: (String) -> Unit

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