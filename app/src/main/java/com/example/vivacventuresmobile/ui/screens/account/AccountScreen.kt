package com.example.vivacventuresmobile.ui.screens.account

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.datastore.core.DataStore
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.data.preferences.AppPreferences
import kotlinx.coroutines.launch

@Composable
fun AccountScreen(
    dataStore: DataStore<AppPreferences>,
    toLoginScreen: () -> Unit,
    toMyPlaces: (String) -> Unit,
    toMyFriends: (String) -> Unit,
    toFavourites: (String) -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {},
) {
    PantallaAccount(
        dataStore = dataStore,
        toLoginScreen = toLoginScreen,
        toMyPlaces = toMyPlaces,
        toFavourites = toFavourites,
        toMyFriends = toMyFriends,
        bottomNavigationBar = bottomNavigationBar
    )
}

@Composable
fun PantallaAccount(
    dataStore: DataStore<AppPreferences>,
    toLoginScreen: () -> Unit,
    toFavourites: (String) -> Unit,
    toMyPlaces: (String) -> Unit,
    toMyFriends: (String) -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val showDialog = remember { mutableStateOf(false) }

    val username = dataStore.data.collectAsState(initial = AppPreferences()).value.username

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = bottomNavigationBar
    ) { contentPadding ->
        Box(modifier = Modifier.fillMaxSize()){
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "¡Bienvenido " + username +"!"
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    ExtendedFloatingActionButton(
                        onClick = { toMyPlaces(username) },
                        icon = { Icon(Icons.Filled.Place, "Place") },
                        text = { Text(text = "Mis sitios") },
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    ExtendedFloatingActionButton(
                        onClick = { toFavourites(username) },
                        icon = { Icon(Icons.Filled.Favorite, "Favoritos") },
                        text = { Text(text = "Mis listas") },
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    ExtendedFloatingActionButton(
                        onClick = { toMyFriends(username) },
                        icon = { Icon(Icons.Filled.SupervisorAccount, "Amigos") },
                        text = { Text(text = "Mis amigos") },
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    ExtendedFloatingActionButton(
                        onClick = { showDialog.value = true },
                        icon = { Icon(Icons.Filled.Logout, "Logout") },
                        text = { Text(text = "Cerrar sesión") },
                    )
                }
                if (showDialog.value) {
                    AlertDialog(
                        onDismissRequest = { showDialog.value = false },
                        title = { Text(text = "Confirmación") },
                        text = { Text(text = "¿Estás seguro de que quieres cerrar sesión?") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    coroutineScope.launch {
                                        dataStore.updateData {
                                            it.copy(
                                                username = "",
                                                password = ""
                                            )
                                        }
                                    }
                                    toLoginScreen()
                                    showDialog.value = false
                                }
                            ) {
                                Text("Sí", color = Color.White)
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { showDialog.value = false },
                            ) {
                                Text("No", color = Color.White)
                            }
                        }
                    )
                }
            }
        }
    }

}

//@Preview
//@Composable
//fun PantallaLogoutPreview() {
//    PantallaAccount(
//        dataStore = DataStore(),
//        toLoginScreen = { /*TODO*/ },
//        toFavourites = { /*TODO*/ },
//        toMyPlaces = { /*TODO*/ })
//}
