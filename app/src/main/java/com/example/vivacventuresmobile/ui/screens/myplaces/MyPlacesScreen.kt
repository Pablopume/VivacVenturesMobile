package com.example.vivacventuresmobile.ui.screens.myplaces

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.ui.screens.map.LoadingAnimation
import com.example.vivacventuresmobile.ui.screens.myfavourites.VivacPlaceListItem
import kotlinx.coroutines.delay

@Composable
fun MyPlacesScreen(
    viewModel: MyPlacesViewModel = hiltViewModel(),
    username: String,
    onBack: () -> Unit,
    onViewDetalle: (Int) -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {},
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    if (state.value.username.isEmpty()) {
        viewModel.handleEvent(MyPlacesEvent.SaveUsername(username))
    }

    PantallaMyPlaces(
        state.value,
        { viewModel.handleEvent(MyPlacesEvent.ErrorVisto) },
        onBack,
        onViewDetalle,
        bottomNavigationBar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMyPlaces(
    state: MyPlacesState,
    errorVisto: () -> Unit,
    onBack: () -> Unit,
    onViewDetalle: (Int) -> Unit,
    bottomNavigationBar: @Composable () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.my_places)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
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
                        .weight(0.8f),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingAnimation(state.loading)
                }
            } else {
                if (state.vivacPlaces.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.start_creating_places),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        items(
                            items = state.vivacPlaces,
                            key = { vivacPlace -> vivacPlace.id }
                        ) { vivacPlace ->
                            SwipeToDeleteContainer(
                                item = vivacPlace,
                                onDelete = {}
                            ) {
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
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit
) {
    var isRemoved by remember {
        mutableStateOf(false)
    }
    val state = rememberDismissState(
        confirmValueChange = { value ->
            if (value == DismissValue.DismissedToStart) {
                isRemoved = true
                true
            } else {
                false
            }
        }
    )

    LaunchedEffect(key1 = isRemoved) {
        if (isRemoved) {
            delay(animationDuration.toLong())
            onDelete(item)
        }
    }

    AnimatedVisibility(
        visible = !isRemoved,
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismiss(
            state = state,
            background = {
                DeleteBackground(swipeDismissState = state)
            },
            dismissContent = { content(item) },
            directions = setOf(DismissDirection.EndToStart)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteBackground(
    swipeDismissState: DismissState
) {
    val color = if (swipeDismissState.dismissDirection == DismissDirection.EndToStart) {
        Color.Red
    } else Color.Transparent

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White
        )
    }
}
