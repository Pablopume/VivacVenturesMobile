package com.example.vivacventuresmobile.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
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
    onViewDetalle: (String) -> Unit,
    dataStore: DataStore<AppPreferences>
) {
    PantallaLogin(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onViewDetalle = onViewDetalle,
        { viewModel.handleEvent(LoginEvent.EmailChanged(it)) },
        { viewModel.handleEvent(LoginEvent.PasswordChange(it)) },
        { viewModel.handleEvent(LoginEvent.NameChanged(it)) },
        { viewModel.handleEvent(LoginEvent.OnLoginEvent()) },
        { viewModel.handleEvent(LoginEvent.OnRegisterEvent()) },
        dataStore
    )
}

@Composable
fun PantallaLogin(
    state: LoginState,
    onViewDetalle: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onNombreChanged: (String) -> Unit,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
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
                onViewDetalle(state.user ?: "")
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
                    }
                    Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                        BotonLogin(
                            Modifier
                                .padding(16.dp)
                                .fillMaxWidth(), onLogin, dataStore)
                        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))

                        BotonRegister(
                            Modifier
                                .padding(16.dp)
                                .fillMaxWidth(), onRegister)
                    }

                }
            }
        }


    }
}

@Composable
fun BotonRegister(modifier: Modifier, onRegister: () -> Unit) {
    Button(
        onClick = onRegister,
        modifier = modifier
    ) {
        Text(Constantes.REGISTER)
    }
}

@Composable
fun BotonLogin(modifier: Modifier, onLogin: () -> Unit, dataStore: DataStore<AppPreferences>) {

    Button(
        onClick = onLogin,
        modifier = modifier
    ) {
        Text(Constantes.LOGIN)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Password(password: String, onPasswordChanged: (String) -> Unit) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChanged,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(id = R.string.password)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1,
        visualTransformation = PasswordVisualTransformation(),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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

