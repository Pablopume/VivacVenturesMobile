package com.example.vivacventuresmobile.ui.screens.account

import androidx.compose.foundation.Image
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.data.preferences.AppPreferences
import kotlinx.coroutines.launch

@Composable
fun AccountScreen(
    dataStore: DataStore<AppPreferences>,
    toLoginScreen: () -> Unit,
    toMyPlaces: () -> Unit,
    toMyFriends: () -> Unit,
    toLists: () -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {},
) {
    PantallaAccount(
        dataStore = dataStore,
        toLoginScreen = toLoginScreen,
        toMyPlaces = toMyPlaces,
        toLists = toLists,
        toMyFriends = toMyFriends,
        bottomNavigationBar = bottomNavigationBar
    )
}

@Composable
fun PantallaAccount(
    dataStore: DataStore<AppPreferences>,
    toLoginScreen: () -> Unit,
    toLists: () -> Unit,
    toMyPlaces: () -> Unit,
    toMyFriends: () -> Unit,
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
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HeaderImageName(Modifier.align(Alignment.CenterHorizontally))
                    Text(
                        text = stringResource(R.string.welcome_user, username),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    ExtendedFloatingActionButton(
                        onClick = { toMyPlaces() },
                        icon = { Icon(Icons.Filled.Place, stringResource(R.string.place)) },
                        text = { Text(text = stringResource(R.string.my_places)) },
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    ExtendedFloatingActionButton(
                        onClick = { toLists() },
                        icon = { Icon(Icons.Filled.Favorite, stringResource(R.string.favourites)) },
                        text = { Text(text = stringResource(R.string.my_lists)) },
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    ExtendedFloatingActionButton(
                        onClick = { toMyFriends() },
                        icon = {
                            Icon(
                                Icons.Filled.SupervisorAccount,
                                stringResource(R.string.friends)
                            )
                        },
                        text = { Text(text = stringResource(R.string.my_friends)) },
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    ExtendedFloatingActionButton(
                        onClick = { showDialog.value = true },
                        icon = { Icon(Icons.Filled.Logout, stringResource(R.string.logout)) },
                        text = { Text(text = stringResource(R.string.logout)) },
                    )
                }
                if (showDialog.value) {
                    AlertDialog(
                        onDismissRequest = { showDialog.value = false },
                        title = { Text(text = stringResource(R.string.confirm)) },
                        text = { Text(text = stringResource(R.string.sure_to_logout)) },
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
                                Text(stringResource(R.string.yes), color = Color.White)
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { showDialog.value = false },
                            ) {
                                Text(stringResource(R.string.no), color = Color.White)
                            }
                        }
                    )
                }
            }
        }
    }

}

@Composable
fun HeaderImageName(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.letras_logo),
        contentDescription = "Header",
        modifier = modifier
    )
}