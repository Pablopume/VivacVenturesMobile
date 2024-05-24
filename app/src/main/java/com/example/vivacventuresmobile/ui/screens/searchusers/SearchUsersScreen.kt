package com.example.vivacventuresmobile.ui.screens.searchusers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.ui.screens.map.LoadingAnimation
import com.example.vivacventuresmobile.ui.screens.myfriends.MyFriendsListItem

@Composable
fun SearchUsersScreen(
    viewModel: SearchUsersViewModel = hiltViewModel(),
    username: String,
    bottomNavigationBar: @Composable () -> Unit = {},
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    if (state.value.username.isEmpty()) {
        viewModel.handleEvent(SearchUsersEvent.SaveUsername(username))
    }

    PantallaFavourites(
        state.value,
        { viewModel.handleEvent(SearchUsersEvent.ErrorVisto) },
        { viewModel.handleEvent(SearchUsersEvent.DoSearch) },
        { viewModel.handleEvent(SearchUsersEvent.OnSearchChange(it)) },
        bottomNavigationBar
    )
}

@Composable
fun PantallaFavourites(
    state: SearchUsersState,
    errorVisto: () -> Unit,
    doSearch: () -> Unit,
    onSearchChange: (String) -> Unit,
    bottomNavigationBar: @Composable () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = bottomNavigationBar
    ) { innerPadding ->
        LaunchedEffect(state.error) {
            state.error?.let {
                snackbarHostState.showSnackbar(
                    message = state.error.toString(),
                    actionLabel = Constantes.DISMISS,
                    duration = SnackbarDuration.Short,
                )
                errorVisto()
            }
        }
        Column {
            if (state.loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingAnimation(state.loading)
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = state.search,
                        onValueChange = onSearchChange,
                        label = { Text("Buscar usuario") },
                        modifier = Modifier.weight(1f),
                        trailingIcon = {
                            if (state.search.isNotEmpty()) {
                                IconButton(onClick = { onSearchChange("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Limpiar")
                                }
                            }
                        }
                    )
                    IconButton(onClick = doSearch) {
                        Icon(Icons.Default.Search, contentDescription = "Buscar")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (state.friend.username.isNotEmpty()) {
                    MyFriendsListItem(friend = state.friend)
                    Button(
                        onClick = { /* Handle send request */ },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Enviar solicitud")
                    }
                }
            }
        }
    }
}