package com.example.vivacventuresmobile.ui.screens.myfavourites

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PersonAddAlt1
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.domain.modelo.VivacPlaceList
import com.example.vivacventuresmobile.ui.screens.map.LoadingAnimation
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun MyFavouritesScreen(
    viewModel: MyFavouritesViewModel = hiltViewModel(),
    listId: Int,
    username: String,
    onBack: () -> Unit,
    onViewDetalle: (Int) -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {},
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()



    if (state.value.listId == 0) {
        viewModel.handleEvent(MyFavouritesEvent.SaveListId(listId))
    }

    if (state.value.username.isEmpty()) {
        viewModel.handleEvent(MyFavouritesEvent.SaveUsername(username))
    }

    LaunchedEffect(state.value.listDeleted) {
        if (state.value.listDeleted) {
            onBack()
        }
    }

    PantallaFavourites(
        state.value,
        { viewModel.handleEvent(MyFavouritesEvent.GetVivacPlaces()) },
        { viewModel.handleEvent(MyFavouritesEvent.ErrorVisto) },
        { viewModel.handleEvent(MyFavouritesEvent.DeleteList()) },
        username,
        { viewModel.handleEvent(MyFavouritesEvent.ShareList(it)) },
        { viewModel.handleEvent(MyFavouritesEvent.UnShareList(it)) },
        onBack,
        onViewDetalle,
        bottomNavigationBar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFavourites(
    state: MyFavouritesState,
    getVivacPlaces: () -> Unit,
    errorVisto: () -> Unit,
    deleteList: () -> Unit,
    username: String,
    onShare: (String) -> Unit,
    onUnshare: (String) -> Unit,
    onBack: () -> Unit,
    onViewDetalle: (Int) -> Unit,
    bottomNavigationBar: @Composable () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var onShareDialogOpen by remember { mutableStateOf(false) }

    if (onShareDialogOpen) {
        var showFriends by remember { mutableStateOf(false) }
        AlertDialog(
            onDismissRequest = { onShareDialogOpen = false },
            title = { Text(stringResource(R.string.share)) },
            text = {
                if (showFriends) {
                    val friendsWithoutAccess = state.friends.filter { it !in state.sharedWith }
                    if (friendsWithoutAccess.isEmpty()) {
                        Text(text = stringResource(R.string.no_friends_without_access))
                    } else {
                        LazyColumn {
                            items(friendsWithoutAccess) { friend ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = friend)
                                    Button(onClick = { onShare(friend) }) {
                                        Text(text = stringResource(R.string.share))
                                    }
                                }

                            }
                        }
                    }
                } else {
                    if (state.sharedWith.isEmpty() || state.sharedWith.size == 1 && state.sharedWith[0] == username) {
                        Text(text = stringResource(R.string.shared_with_noone))
                    } else {
                        LazyColumn {
                            items(state.sharedWith) { sharedWith ->
                                if (sharedWith != username) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = sharedWith)
                                        Button(onClick = { onUnshare(sharedWith) }) {
                                            Text(text = stringResource(R.string.unshare))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },

            confirmButton = {
                Button(
                    onClick = { showFriends = !showFriends }
                ) {
                    Text(
                        text = if (showFriends) stringResource(R.string.shared_with) else stringResource(
                            R.string.friends
                        )
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = { onShareDialogOpen = false }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(state.list.name) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onShareDialogOpen = true }) {
                        Icon(
                            Icons.Default.PersonAddAlt1,
                            contentDescription = stringResource(R.string.share)
                        )

                    }
                    if (state.list.username == username) {
                        IconButton(onClick = { deleteList() }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = stringResource(R.string.delete)
                            )
                        }
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
            if (state.list.favoritos.isEmpty() && !state.firstTime) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_places_fav),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                val swipeRefreshState = rememberSwipeRefreshState(state.loading)
                SwipeRefresh(state = swipeRefreshState, onRefresh = { getVivacPlaces() }) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        items(
                            items = state.list.favoritos,
                            key = { vivacPlace -> vivacPlace.id }
                        ) { vivacPlace ->
                            VivacPlaceListItem(
                                vivacPlace = vivacPlace,
                                onViewDetalle = onViewDetalle
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun VivacPlaceListItem(
    vivacPlace: VivacPlaceList,
    onViewDetalle: (Int) -> Unit,
    modifier: Modifier = Modifier.background(MaterialTheme.colorScheme.background)
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.smallmedium_padding))
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.medium_padding)))
            .clickable { onViewDetalle(vivacPlace.id) },
        elevation = CardDefaults.cardElevation(0.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.medium_padding))
                .fillMaxWidth()
        ) {
            val imageUrl = vivacPlace.images.ifEmpty {
                "https://firebasestorage.googleapis.com/v0/b/vivacventures-b3fae.appspot.com/o/images%2Fdefault.jpg?alt=media&token=5ef9d6e8-c7b8-47ac-87a3-419d22857a70"
            }
            val image: Painter = rememberAsyncImagePainter(imageUrl)
            Image(
                modifier = Modifier
                    .size(80.dp, 80.dp)
                    .clip(RoundedCornerShape(16.dp)),
                painter = image,
                alignment = Alignment.CenterStart,
                contentDescription = "",
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))

            val columnWeight = if (vivacPlace.favorite) 0.78f else 1f

            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(8.dp)
                    .weight(columnWeight)
            ) {
                Text(
                    text = vivacPlace.name,
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = vivacPlace.type,
                        modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelMedium
                    )
                    if (vivacPlace.valorations != -1.0) {
                        Text(
                            text = vivacPlace.valorations.toString(),
                            modifier = Modifier.padding(0.dp, 0.dp, 4.dp, 0.dp),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelMedium
                        )

                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = stringResource(R.string.valorations),
                            tint = Color.Yellow
                        )
                    }
                }

            }

            if (vivacPlace.favorite) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.29f),
                    horizontalArrangement = Arrangement.End
                ) {
                    FavouriteTag()
                }
            }
        }
    }
}


@Composable
fun FavouriteTag() {
    ChipView(isFavourite = stringResource(R.string.favorite), colorResource = Color.Green)
}

@Composable
fun ChipView(isFavourite: String, colorResource: Color) {
    Box(
        modifier = Modifier
            .wrapContentWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource.copy(.08f))
    ) {
        Text(
            text = isFavourite, modifier = Modifier.padding(12.dp, 6.dp, 12.dp, 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = colorResource
        )
    }
}

@Preview
@Composable
fun previewvivacPlaceCard() {
    VivacPlaceListItem(
        vivacPlace = VivacPlaceList(
            id = 1,
            name = "Refugio de la montaña",
            type = "Refugio",
            favorite = true,
            valorations = 4.5,
            images = "https://firebasestorage.googleapis.com/v0/b/vivacventures-b3fae.appspot.com/o/images%2Fdefault.jpg?alt=media&token=5ef9d6e8-c7b8-47ac-87a3-419d22857a70"
        ),
        onViewDetalle = {}
    )
}
