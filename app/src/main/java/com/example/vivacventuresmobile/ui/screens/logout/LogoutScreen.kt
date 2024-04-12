package com.example.vivacventuresmobile.ui.screens.logout

import android.graphics.Paint.Align
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import com.example.vivacventuresmobile.data.preferences.AppPreferences
import kotlinx.coroutines.launch

@Composable
fun LogoutScreen (
    dataStore: DataStore<AppPreferences>,
    onViewDetalle: (String) -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {},
    ) {
    PantallaLogout(
        dataStore = dataStore,
        onViewDetalle = onViewDetalle,
        bottomNavigationBar = bottomNavigationBar
    )
}

@Composable
fun PantallaLogout(
    dataStore: DataStore<AppPreferences>,
    onViewDetalle: (String) -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = bottomNavigationBar
    ) {
        contentPadding  ->
        Column (
            modifier = Modifier.fillMaxSize().padding(contentPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.1f))
            Text(
                text = dataStore.data.collectAsState(initial = AppPreferences()).value.username + " seguro que quieres cerrar sesión?"
            )
            Spacer(modifier = Modifier.weight(0.01f))
            Button(onClick = {
                coroutineScope.launch {
                    dataStore.updateData {
                        it.copy(
                            username = "",
                            password = ""
                        )
                    }
                }
                onViewDetalle
            }) {
                Text(text = "Cerrar sesión")
            }
            Spacer(modifier = Modifier.weight(0.1f))
        }
    }

}

/*@Preview
@Composable
fun PantallaLogoutPreview() {
    PantallaLogout(
        dataStore = dataStore(),
        onViewDetalle = {},
        bottomNavigationBar = {}
    )
}*/
