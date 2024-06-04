package com.example.vivacventuresmobile.ui.screens.forgotpassword

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
import com.example.vivacventuresmobile.ui.screens.login.HeaderImage
import com.example.vivacventuresmobile.ui.screens.register.EmailField

@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
    onPasswordReset: () -> Unit
) {
    PantallaForgotPassword(
        state = viewModel.uiState.collectAsStateWithLifecycle().value,
        { viewModel.handleEvent(ForgotPasswordEvent.OnTempPasswordChange(it)) },
        { viewModel.handleEvent(ForgotPasswordEvent.OnPasswordChange(it)) },
        { viewModel.handleEvent(ForgotPasswordEvent.OnEmailChange(it)) },
        { viewModel.handleEvent(ForgotPasswordEvent.SendEmail()) },
        { viewModel.handleEvent(ForgotPasswordEvent.ForgotPassword()) },
        onPasswordReset
    )
}

@Composable
fun PantallaForgotPassword(
    state: ForgotPasswordState,
    onTempPasswordChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    OnEmailChange: (String) -> Unit,
    onSendEmail: () -> Unit,
    onChangePassword: () -> Unit,
    onPasswordReset: () -> Unit
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

        LaunchedEffect(state.passwordchanged) {
            if (state.passwordchanged) {
                onPasswordReset()
            }
        }


        Box(modifier = Modifier.fillMaxSize()) {
            if (state.loading) {
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
                        HeaderImage(Modifier.align(Alignment.CenterHorizontally))
                        if (!state.emailsend) {
                            EmailField(state.correoElectronico, OnEmailChange)
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                            BotonSendPassword(onSendEmail)
                        } else {
                            TempPasswordField(state.temppassword, onTempPasswordChanged)
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                            NewPasswordField(state.password, onPasswordChanged)
                            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                            BotonChangePassword(onChangePassword)
                        }
                    }

                }
            }
        }


    }
}

@Composable
fun BotonSendPassword(onForgotPassword: () -> Unit) {
    Button(
        onClick = onForgotPassword,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = stringResource(id = R.string.send_password))
    }
}

@Composable
fun BotonChangePassword(onChangePassword: () -> Unit) {
    Button(
        onClick = onChangePassword,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = stringResource(id = R.string.change_password))
    }
}


@Composable
fun TempPasswordField(password: String, onPasswordChanged: (String) -> Unit) {
    var passwordVisibility by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChanged,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(id = R.string.temppassword)) },
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
fun NewPasswordField(password: String, onPasswordChanged: (String) -> Unit) {
    var passwordVisibility by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChanged,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(id = R.string.newpassword)) },
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

