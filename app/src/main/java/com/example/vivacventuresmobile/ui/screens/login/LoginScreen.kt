package com.example.vivacventuresmobile.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.data.preferences.AppPreferences
import kotlinx.coroutines.launch

@Composable
fun LoginScreen (
    viewModel: LoginViewModel = hiltViewModel(),
    onViewDetalle: (String) -> Unit,
    dataStore: DataStore<AppPreferences>
) {
    PantallaLogin(
        state =  viewModel.state.collectAsStateWithLifecycle().value,
        onViewDetalle = onViewDetalle,
        { viewModel.handleEvent(LoginEvent.EmailChanged(it))},
        { viewModel.handleEvent(LoginEvent.PasswordChange(it))},
        { viewModel.handleEvent(LoginEvent.NameChanged(it))},
        { viewModel.handleEvent(LoginEvent.OnLoginEvent())},
        { viewModel.handleEvent(LoginEvent.OnRegisterEvent())},
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
                            username = state.user?:"",
                            password = state.password?:""
                        )
                    }
                }
                onViewDetalle(state.user?:"")
            }
        }


        Box(modifier = Modifier.fillMaxSize()) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary),
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Spacer(modifier = Modifier.weight(0.1f))
                    Nombre(state, state.user?:"", onNombreChanged)
                    Spacer(modifier = Modifier.weight(0.01f))

                    Password(state, state.password?:"", onPasswordChanged)
                    Spacer(modifier = Modifier.weight(0.01f))

                    BotonLogin( Modifier.padding(16.dp).fillMaxWidth(), onLogin, dataStore)
                    Spacer(modifier = Modifier.weight(0.01f))

                    BotonRegister( Modifier.padding(16.dp).fillMaxWidth(), onRegister)
                    Spacer(modifier = Modifier.weight(0.1f))
                }
            }
        }


    }
}
@Composable
fun BotonRegister( modifier: Modifier, onRegister: () -> Unit) {
    Button(
        onClick = {
            onRegister()
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Gray,
            Color.Cyan
        )
    ) {
        Text(Constantes.REGISTER)
    }
}

@Composable
fun BotonLogin(modifier: Modifier, onLogin: () -> Unit, dataStore: DataStore<AppPreferences>) {

    Button(
        onClick = {
            onLogin()
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Gray,
            Color.Cyan
        )
    ) {
        Text(Constantes.LOGIN)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Password(state: LoginState, password: String, onPasswordChanged: (String) -> Unit) {
    TextField(
    value = password, onValueChange = { onPasswordChanged(it) },
    modifier = Modifier
    .fillMaxWidth(),
    placeholder = { Text(Constantes.PASSWORD) },
    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
    singleLine = true,
    maxLines = 1,
    colors = TextFieldDefaults.textFieldColors(
    focusedTextColor = Color.Black,
    unfocusedTextColor = MaterialTheme.colorScheme.primary,
    )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Nombre(state: LoginState, s: String, onNombreChanged: (String) -> Unit) {
    TextField(
        value = s, onValueChange = { onNombreChanged(it) },
        modifier = Modifier
            .fillMaxWidth(),
        placeholder = { Text(Constantes.USER) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.textFieldColors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = MaterialTheme.colorScheme.primary,
        )
    )
}

