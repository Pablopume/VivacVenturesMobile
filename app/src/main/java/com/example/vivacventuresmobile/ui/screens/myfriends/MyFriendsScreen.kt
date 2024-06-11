package com.example.vivacventuresmobile.ui.screens.myfriends

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.domain.modelo.FriendRequest
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun MyFriendsScreen(
    viewModel: MyFriendsViewModel = hiltViewModel(),
    username: String,
    toSearchFriendsScreen: () -> Unit,
    onBack: () -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {},
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    if (state.value.username.isEmpty()) {
        viewModel.handleEvent(MyFriendsEvent.SaveUsername(username))
    }

    PantallaMyFriends(
        state.value,
        { viewModel.handleEvent(MyFriendsEvent.GetFriends) },
        { viewModel.handleEvent(MyFriendsEvent.ErrorVisto) },
        { viewModel.handleEvent(MyFriendsEvent.AcceptFriendRequest(it)) },
        { viewModel.handleEvent(MyFriendsEvent.RejectFriendRequest(it)) },
        { viewModel.handleEvent(MyFriendsEvent.DeleteFriendRequest(it)) },
        toSearchFriendsScreen,
        onBack,
        bottomNavigationBar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMyFriends(
    state: MyFriendsState,
    getFriends: () -> Unit,
    errorVisto: () -> Unit,
    onAccept: (FriendRequest) -> Unit,
    onReject: (FriendRequest) -> Unit,
    onDeleteFriend: (FriendRequest) -> Unit,
    toSearchFriendsScreen: () -> Unit,
    onBack: () -> Unit,
    bottomNavigationBar: @Composable () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val seePendingRequests = remember { mutableStateOf(false) }
    if (state.pendingFriends.isEmpty()) {
        seePendingRequests.value = false
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    if (seePendingRequests.value) Text(stringResource(R.string.friend_requests)) else Text(
                        stringResource(R.string.my_friends)
                    )
                },
                navigationIcon = {
                    if (seePendingRequests.value) {
                        IconButton(onClick = { seePendingRequests.value = false }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    } else {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    }
                },
                actions = {
                    if (!seePendingRequests.value && state.pendingFriends.isNotEmpty()) {
                        IconButton(onClick = { seePendingRequests.value = true }) {
                            Icon(
                                Icons.Default.NotificationsActive,
                                contentDescription = stringResource(R.string.to_notifications)
                            )
                        }
                    }
                }
            )
        },
        bottomBar = bottomNavigationBar,
        floatingActionButton = {
            FloatingActionButton(onClick = { toSearchFriendsScreen() }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.search)
                )
            }
        }
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
            if (state.friends.isEmpty() && state.pendingFriends.isEmpty() && !state.firstTime) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.start_adding_friends),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                val swipeRefreshState = rememberSwipeRefreshState(state.loading)
                if (seePendingRequests.value) {
                    SwipeRefresh(state = swipeRefreshState, onRefresh = { getFriends() }) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                        ) {
                            items(
                                items = state.pendingFriends,
                                key = { friend -> friend.id }
                            ) { friend ->
                                PendingFriendRequestListItem(
                                    friend,
                                    state.username,
                                    onAccept,
                                    onReject
                                )
                            }
                        }
                    }
                } else {
                    SwipeRefresh(state = swipeRefreshState, onRefresh = { getFriends() }) {
                        LazyColumn(
                            modifier = Modifier
                                .padding(innerPadding)
                        ) {
                            items(
                                items = state.friends,
                                key = { friend -> friend.id }
                            ) { friend ->
                                MyFriendRequestListItem(friend, state.username, onDeleteFriend)
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun MyFriendRequestListItem(
    friend: FriendRequest,
    username: String,
    onDeleteFriend: (FriendRequest) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.smallmedium_padding))
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.medium_padding))),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (friend.requester == username) friend.requested else friend.requester,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.options)
                    )
                }
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        onClick = {
                            onDeleteFriend(friend)
                            showMenu = false
                        },
                        text = {
                            Text(stringResource(R.string.delete_friend))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PendingFriendRequestListItem(
    friend: FriendRequest,
    username: String,
    onAccept: (FriendRequest) -> Unit,
    onReject: (FriendRequest) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.smallmedium_padding))
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.medium_padding))),
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.smallmedium_padding)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (friend.requester == username) friend.requested else friend.requester,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.weight(1f)
            )
            Button(onClick = { onAccept(friend) }) {
                Text(stringResource(R.string.accept))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onReject(friend) }) {
                Text(stringResource(R.string.reject))
            }
        }
    }
}

@Preview
@Composable
fun PreviewFriendCard() {
    MyFriendRequestListItem(
        friend = FriendRequest(
            0,
            "username",
            "username",
            false
        ),
        onDeleteFriend = {},
        username = "username"
    )
}

@Preview
@Composable
fun PreviewPendingFriendCard() {
    PendingFriendRequestListItem(
        friend = FriendRequest(
            0,
            "juan",
            "julian",
            false
        ),
        onAccept = {},
        onReject = {},
        username = "juan"
    )
}