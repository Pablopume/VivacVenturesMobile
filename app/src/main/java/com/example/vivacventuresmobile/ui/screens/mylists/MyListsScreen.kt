package com.example.vivacventuresmobile.ui.screens.mylists

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.domain.modelo.ListFavs
import com.example.vivacventuresmobile.ui.screens.listplaces.ListPlacesEvent
import com.example.vivacventuresmobile.ui.screens.map.LoadingAnimation
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun MyListsScreen(
    viewModel: MyListsViewModel = hiltViewModel(),
    username: String,
    onListSelected: (Int) -> Unit,
    onBack: () -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {},
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    if (state.value.username.isEmpty()) {
        viewModel.handleEvent(MyListsEvent.SaveUsername(username))
    }

    LaunchedEffect(Unit) {
        viewModel.handleEvent(MyListsEvent.GetLists())
    }

    MyListsContent(
        state = state.value,
        onNameChange = { viewModel.handleEvent(MyListsEvent.OnNameChanged(it)) },
        getLists = { viewModel.handleEvent(MyListsEvent.GetLists()) },
        onCreateList = { viewModel.handleEvent(MyListsEvent.CreateList()) },
        errorVisto = { viewModel.handleEvent(MyListsEvent.ErrorVisto) },
        onListSelected = onListSelected,
        onBack = onBack,
        bottomNavigationBar = bottomNavigationBar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MyListsContent(
    state: MyListsState,
    onNameChange: (String) -> Unit,
    getLists: () -> Unit,
    onCreateList: () -> Unit,
    errorVisto: () -> Unit,
    onListSelected: (Int) -> Unit,
    onBack: () -> Unit,
    bottomNavigationBar: @Composable () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var createListDialogOpen by remember { mutableStateOf(false) }

    if (createListDialogOpen) {
        AlertDialog(
            onDismissRequest = { createListDialogOpen = false },
            title = { Text(stringResource(R.string.create_list)) },
            text = {
                TextField(
                    value = state.nameList,
                    onValueChange = onNameChange,
                    label = { Text(stringResource(R.string.name_list)) }
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onCreateList()
                        createListDialogOpen = false
                    }
                ) {
                    Text(stringResource(R.string.create))
                }
            },
            dismissButton = {
                Button(
                    onClick = { createListDialogOpen = false }
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
                title = { Text(stringResource(R.string.my_lists)) },
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
        bottomBar = bottomNavigationBar,
        floatingActionButton = {
            FloatingActionButton(onClick = { createListDialogOpen = true}) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add)
                )
            }
        }
    ) { innerPadding ->
        LaunchedEffect(state.error) {
            state.error?.let {
                snackbarHostState.showSnackbar(
                    message = state.error.toString(),
                    actionLabel = "X",
                    duration = SnackbarDuration.Short,
                )
                errorVisto()
            }
        }
        Column {
                if (state.list.isEmpty() && !state.firstTime) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.you_have_no_lists_yet),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                } else {
                    val swipeRefreshState = rememberSwipeRefreshState(state.loading)
                    SwipeRefresh(state = swipeRefreshState, onRefresh = { getLists() }) {
                        LazyColumn(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                        ) {
                            items(
                                items = state.list,
                                key = { list -> list.id }
                            ) { list ->
                                ListItem(
                                    list = list,
                                    onListSelected = onListSelected
                                )
                            }
                        }
                    }
                }

        }

    }

}

@Composable
fun ListItem(
    list: ListFavs,
    onListSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onListSelected(list.id) }
            .padding(dimensionResource(R.dimen.medium_padding))
    ) {
        Text(
            text = list.name,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.weight(1f)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Creador"
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.smallmedium_padding)))
            Text(
                text = list.username,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.End
            )
        }
    }
    Divider(modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.medium_padding)))
}

@Preview
@Composable
fun previewListItem() {
    ListItem(
        list = ListFavs(
            id = 1,
            name = "List 1",
            username = "User 1"
        ),
        onListSelected = {}
    )
}
