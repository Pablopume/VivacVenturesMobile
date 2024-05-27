package com.example.vivacventuresmobile.ui.screens.searchusers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.domain.modelo.Friend
import com.example.vivacventuresmobile.ui.screens.map.LoadingAnimation

@Composable
fun SearchUsersScreen(
    viewModel: SearchUsersViewModel = hiltViewModel(),
    username: String,
    onBack: () -> Unit,
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
        { viewModel.handleEvent(SearchUsersEvent.SendFriendRequest) },
        onBack,
        bottomNavigationBar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFavourites(
    state: SearchUsersState,
    errorVisto: () -> Unit,
    doSearch: () -> Unit,
    onSearchChange: (String) -> Unit,
    sendFriendRequest: () -> Unit,
    onBack: () -> Unit,
    bottomNavigationBar: @Composable () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.search_users)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        },
        bottomBar = bottomNavigationBar
    ) { innerPadding ->
        val messageDismiss = stringResource(R.string.dismiss)
        LaunchedEffect(state.error) {
            state.error?.let {
                snackbarHostState.showSnackbar(
                    message = state.error.toString(),
                    actionLabel = messageDismiss,
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
                        label = { Text(stringResource(R.string.search_user)) },
                        modifier = Modifier.weight(1f),
                        trailingIcon = {
                            if (state.search.isNotEmpty()) {
                                IconButton(onClick = { onSearchChange("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = stringResource(R.string.clear))
                                }
                            }
                        }
                    )
                    IconButton(onClick = doSearch) {
                        Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search))
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                if (state.friend.username.isNotEmpty()) {
                    MyFriendsListItem(friend = state.friend)
                    Button(
                        onClick = { sendFriendRequest() },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(stringResource(R.string.send_request))
                    }
                }
            }
        }
    }
}

@Composable
fun MyFriendsListItem(
    friend: Friend,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.smallmedium_padding))
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.medium_padding))),
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurface
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.smallmedium_padding))
        ) {
            Text(
                text = friend.username,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
                color = Color.Black
            )
            Text(
                text = if (friend.count == 1) {
                    stringResource(R.string.one_place_created)
                } else {
                    stringResource(R.string.places_created, friend.count)
                },
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.End,
                color = Color.Black
            )
        }
    }
}


@Preview
@Composable
fun PreviewFriendCard() {
    MyFriendsListItem(
        friend = Friend(
            username = "username",
            count = 1
        )
    )
}