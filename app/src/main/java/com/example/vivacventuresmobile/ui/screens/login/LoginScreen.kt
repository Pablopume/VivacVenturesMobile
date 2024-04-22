package com.example.vivacventuresmobile.ui.screens.login

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.datastore.core.DataStore
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.data.preferences.AppPreferences
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginDone: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPassword: () -> Unit,
    dataStore: DataStore<AppPreferences>
) {
    PantallaLogin(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onLoginDone = onLoginDone,
        { viewModel.handleEvent(LoginEvent.PasswordChange(it)) },
        { viewModel.handleEvent(LoginEvent.NameChanged(it)) },
        { viewModel.handleEvent(LoginEvent.OnLoginEvent()) },
        onRegisterClick,
        onForgotPassword,
        dataStore
    )
}

@Composable
fun PantallaLogin(
    state: LoginState,
    onLoginDone: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onNombreChanged: (String) -> Unit,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onForgotPassword: () -> Unit,
    dataStore: DataStore<AppPreferences>

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

        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(state.loginSuccess) {
            if (state.loginSuccess) {
                coroutineScope.launch {
                    dataStore.updateData {
                        it.copy(
                            username = state.user ?: "",
                            password = state.password ?: ""
                        )
                    }
                }
                onLoginDone(state.user ?: "")
            }
        }


        Box(modifier = Modifier.fillMaxSize()) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Box(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.medium_padding))) {
                        Nombre(state.user ?: "", onNombreChanged)
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))

                        Password(state.password ?: "", onPasswordChanged)
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                        BotonLogin(onLogin)
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                        BotonRegister(onRegister)
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                        BotonForgotPassword(onForgotPassword)

                    }
                }
            }
        }


    }
}

@Composable
fun BotonForgotPassword(onForgotPassword: () -> Unit) {
    TextButton(
        onClick = onForgotPassword,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = stringResource(id = R.string.forgot_password))
    }
}

@Composable
fun BotonRegister(onRegister: () -> Unit) {
    Button(
        onClick = onRegister,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = stringResource(id = R.string.register))
    }
}

@Composable
fun BotonLogin(onLogin: () -> Unit) {
    Button(
        onClick = onLogin,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = stringResource(id = R.string.login))
    }
}

@Composable
fun Password(password: String, onPasswordChanged: (String) -> Unit) {
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
        }
    )
}

@Composable
fun Nombre(username: String, onNombreChanged: (String) -> Unit) {
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

