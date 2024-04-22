package com.example.vivacventuresmobile.ui.screens.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.ui.screens.map.LoadingAnimation

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onRegisterDone: () -> Unit,
) {
    PantallaRegister(
        state = viewModel.uiState.collectAsStateWithLifecycle().value,
        onRegisterDone = onRegisterDone,
        { viewModel.handleEvent(RegisterEvent.OnEmailChange(it)) },
        { viewModel.handleEvent(RegisterEvent.OnPasswordChange(it)) },
        { viewModel.handleEvent(RegisterEvent.OnUserNameChange(it)) },
        { viewModel.handleEvent(RegisterEvent.Register()) },
    )
}

@Composable
fun PantallaRegister(
    state: RegisterState,
    onRegisterDone: () -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onNombreChanged: (String) -> Unit,
    onRegister: () -> Unit,
    ) {

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        LaunchedEffect(state.error) {
            state.error?.let {
                snackbarHostState.showSnackbar(
                    message = state.error.toString(),
                    duration = SnackbarDuration.Short,
                )
            }
        }
        LaunchedEffect(state.registered) {
            if (state.registered) {
                onRegisterDone()
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            if (state.loading) {
                LoadingAnimation(visible = state.loading)
            } else {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.medium_padding))) {
                        EmailField(state.correoElectronico, onEmailChanged)
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                        NombreField(state.user, onNombreChanged)
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                        PasswordField(state.password, onPasswordChanged)
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                        BotonRegister(onRegister)

                    }
                }
            }
        }


    }
}

@Composable
fun BotonRegister(onRegister: () -> Unit) {
    Button(
        onClick = onRegister,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(Constantes.REGISTER)
    }
}

@Composable
fun PasswordField(password: String, onPasswordChanged: (String) -> Unit) {
    var passwordVisibility by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChanged,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(id = R.string.password)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1,
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(
                    imageVector = if (passwordVisibility) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                    contentDescription = stringResource(id = R.string.password_visibility_toggle)
                )
            }
        },
    )
}

@Composable
fun NombreField(username: String, onNombreChanged: (String) -> Unit) {
    OutlinedTextField(
        value = username,
        onValueChange = onNombreChanged,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(id = R.string.username)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        singleLine = true,
        maxLines = 1,
    )
}

@Composable
fun EmailField(username: String, onNombreChanged: (String) -> Unit) {
    OutlinedTextField(
        value = username,
        onValueChange = onNombreChanged,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(id = R.string.email)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        singleLine = true,
        maxLines = 1,
    )
}

