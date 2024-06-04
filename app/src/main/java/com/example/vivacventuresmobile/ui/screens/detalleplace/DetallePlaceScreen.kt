package com.example.vivacventuresmobile.ui.screens.detalleplace

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.domain.modelo.Valoration
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun DetallePlaceScreen(
    viewModel: DetallePlaceViewModel = hiltViewModel(),
    placeId: Int,
    username: String,
    bottomNavigationBar: @Composable () -> Unit = {},
    onBack: () -> Unit,
    onUpdatePlace: (Int) -> Unit
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.handleEvent(DetallePlaceEvent.GetDetalle(placeId))
    }

    if (state.value.vivacPlace == null) {
        viewModel.handleEvent(DetallePlaceEvent.SaveUsernameAndId(username, placeId))
    }

    LaunchedEffect(state.value.deleted) {
        if (state.value.deleted) {
            onBack()
        }

    }

    DetallePlace(
        state.value,
        { viewModel.handleEvent(DetallePlaceEvent.ErrorVisto) },
        { viewModel.handleEvent(DetallePlaceEvent.GetDetalle(it)) },
        bottomNavigationBar,
        { viewModel.handleEvent(DetallePlaceEvent.AddFavourite(it)) },
        { viewModel.handleEvent(DetallePlaceEvent.DeleteFavourite(it)) },
        { viewModel.handleEvent(DetallePlaceEvent.DeletePlace()) },
        { id -> viewModel.handleEvent(DetallePlaceEvent.DeleteValoration(id)) },
        { description ->
            viewModel.handleEvent(
                DetallePlaceEvent.OnDescriptionReportChange(
                    description
                )
            )
        },
        { review -> viewModel.handleEvent(DetallePlaceEvent.OnReviewValorationChange(review)) },
        { score -> viewModel.handleEvent(DetallePlaceEvent.OnScoreChange(score)) },
        { viewModel.handleEvent(DetallePlaceEvent.AddValoration()) },
        { viewModel.handleEvent(DetallePlaceEvent.AddReport()) },
        onBack,
        onUpdatePlace
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallePlace(
    state: DetallePlaceState,
    errorVisto: () -> Unit,
    getDetalle: (Int) -> Unit,
    bottomNavigationBar: @Composable () -> Unit,
    favourite: (Int) -> Unit,
    unfavourite: (Int) -> Unit,
    delete: () -> Unit,
    deleteValoration: (Int) -> Unit,
    onDescriptionReportChange: (String) -> Unit,
    onReviewValorationChange: (String) -> Unit,
    onScoreChange: (Int) -> Unit,
    onAddValoration: () -> Unit,
    onAddReport: () -> Unit,
    onBack: () -> Unit,
    onUpdatePlace: (Int) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var reportDialogOpen by remember { mutableStateOf(false) }
    var favsDialogOpen by remember { mutableStateOf(false) }
    if (favsDialogOpen) {
        AlertDialog(
            onDismissRequest = { favsDialogOpen = false },
            title = { Text(stringResource(R.string.favourites)) },
            text = {
                if (state.listsUser.isEmpty()) {
                    Text(stringResource(R.string.you_have_no_lists_yet))
                } else {
                    LazyColumn {
                        items(state.listsUser) { list ->
                            val isFavourite = state.listsVivacPlace.any { it.id == list.id }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = list.name,
                                    modifier = Modifier.weight(1f)
                                )
                                if (isFavourite) {
                                    IconButton(onClick = { unfavourite(list.id) }) {
                                        Icon(
                                            Icons.Filled.Favorite,
                                            contentDescription = stringResource(R.string.unfavorite)
                                        )
                                    }
                                } else {
                                    IconButton(onClick = { favourite(list.id) }) {
                                        Icon(
                                            Icons.Filled.FavoriteBorder,
                                            contentDescription = stringResource(R.string.favorite)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

            },
            confirmButton = {
                Button(
                    onClick = { favsDialogOpen = false }
                ) {
                    Text(stringResource(R.string.done))
                }
            },
            dismissButton = {
                Button(
                    onClick = { favsDialogOpen = false }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )

    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                ),
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                title = { Text(text = state.vivacPlace?.type?.uppercase() ?: "") },
                actions = {
                    if (state.vivacPlace?.username?.lowercase() == state.username.lowercase()) {
                        IconButton(onClick = { delete() }) {
                            Icon(
                                Icons.Filled.Delete,
                                contentDescription = stringResource(R.string.delete)
                            )
                        }
                        IconButton(onClick = { onUpdatePlace(state.vivacPlace.id) }) {
                            Icon(
                                Icons.Filled.Edit,
                                contentDescription = stringResource(R.string.edit)
                            )
                        }
                    } else {
                        IconButton(onClick = { reportDialogOpen = true }) {
                            Icon(
                                Icons.Filled.Report,
                                contentDescription = stringResource(R.string.report)
                            )
                        }
                    }
                    if (state.vivacPlace?.favorite == true) {
                        IconButton(onClick = { favsDialogOpen = true }) {
                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = stringResource(R.string.unfavorite)
                            )
                        }
                    } else {
                        IconButton(onClick = { favsDialogOpen = true }) {
                            Icon(
                                Icons.Filled.FavoriteBorder,
                                contentDescription = stringResource(R.string.favorite)
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = bottomNavigationBar
    ) { innerPadding ->
        val dismissError = stringResource(R.string.dismiss)
        LaunchedEffect(state.error) {
            state.error?.let {
                snackbarHostState.showSnackbar(
                    message = state.error.toString(),
                    actionLabel = dismissError,
                    duration = SnackbarDuration.Short,
                )
                errorVisto()
            }
        }
        val swipeRefreshState = rememberSwipeRefreshState(state.loading)
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { getDetalle(state.vivacPlace?.id ?: 0) }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(id = R.dimen.medium_padding))
                        .verticalScroll(rememberScrollState())
                ) {
                    TextTitle(title = state.vivacPlace?.name ?: "")
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    ImageCarousel(images = state.vivacPlace?.images ?: emptyList())
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    TextDescription(description = state.vivacPlace?.description ?: "")
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    MapLocation(
                        lat = state.vivacPlace?.lat ?: 0.0,
                        lon = state.vivacPlace?.lon ?: 0.0
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    InfoTable(
                        price = state.vivacPlace?.price ?: 0.0,
                        capacity = state.vivacPlace?.capacity ?: 0,
                        date = state.vivacPlace?.date.toString(),
                        valorations = state.vivacPlace?.valorations ?: emptyList()
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    ValorationsList(
                        valoration = state.vivacPlace?.valorations ?: emptyList(),
                        username = state.username,
                        deleteValoration = deleteValoration,
                        onAddValoration = onAddValoration,
                        onScoreChange = onScoreChange,
                        onReviewValorationChange = onReviewValorationChange,
                        score = state.score,
                        review = state.reviewValoration
                    )
                    if (reportDialogOpen) {
                        AlertDialog(
                            onDismissRequest = { reportDialogOpen = false },
                            title = { Text(stringResource(R.string.report)) },
                            text = {
                                TextField(
                                    value = state.descriptionReport,
                                    onValueChange = onDescriptionReportChange,
                                    label = { Text(stringResource(R.string.report_description)) }
                                )
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        onAddReport()
                                        reportDialogOpen = false
                                    }
                                ) {
                                    Text(stringResource(R.string.send))
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = { reportDialogOpen = false }
                                ) {
                                    Text(stringResource(R.string.cancel))
                                }
                            }
                        )
                    }
                }
            }
        }

    }

}

@Composable
fun InfoTable(price: Double, capacity: Int, date: String, valorations: List<Valoration>) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.AttachMoney, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text(
                    text = stringResource(R.string.price_),
                    modifier = Modifier.padding(start = dimensionResource(R.dimen.small_padding))
                )
            }
            Text(text = "$price")
        }
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.People, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text(
                    text = stringResource(R.string.capacity_),
                    modifier = Modifier.padding(start = dimensionResource(R.dimen.small_padding))
                )
            }
            Text(text = "$capacity")
        }
        Divider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text(
                    text = stringResource(R.string.date_),
                    modifier = Modifier.padding(start = dimensionResource(R.dimen.small_padding))
                )
            }
            Text(text = date)
        }
        if (valorations.isNotEmpty()) {
            Divider()
            val averageRating = valorations.map { it.score }.average()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Text(
                        text = stringResource(R.string.average_rating),
                        modifier = Modifier.padding(start = dimensionResource(R.dimen.small_padding))
                    )
                }
                Text(text = "$averageRating")
            }
        }
    }
}

@Composable
fun ValorationsList(
    valoration: List<Valoration>,
    username: String,
    deleteValoration: (Int) -> Unit,
    onAddValoration: () -> Unit,
    onScoreChange: (Int) -> Unit,
    onReviewValorationChange: (String) -> Unit,
    score: Int,
    review: String
) {
    var valorationDialogOpen by remember { mutableStateOf(false) }
    if (valorationDialogOpen) {
        AlertDialog(
            onDismissRequest = { valorationDialogOpen = false },
            title = { Text(stringResource(R.string.add_valoration)) },
            text = {
                Column {
                    Column {
                        Slider(
                            value = score.toFloat(),
                            onValueChange = {
                                onScoreChange(it.toInt())
                            },
                            valueRange = 1f..5f,
                            steps = 5
                        )
                        Text(text = stringResource(R.string.score, score))
                    }
                    TextField(
                        value = review,
                        onValueChange = {
                            onReviewValorationChange(it)
                        },
                        label = { Text(stringResource(R.string.review)) }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAddValoration()
                        valorationDialogOpen = false
                    }
                ) {
                    Text(stringResource(R.string.add))
                }
            },
            dismissButton = {
                Button(
                    onClick = { valorationDialogOpen = false }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
    if (valoration.isNotEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = dimensionResource(R.dimen.medium_padding)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.weight(1f))

            ExtendedFloatingActionButton(
                onClick = { valorationDialogOpen = true },
                icon = { Icon(Icons.Default.Edit, stringResource(R.string.write_review)) },
                text = { Text(text = stringResource(R.string.write_review)) },
            )
        }
        Column {
            val sortedValorations = valoration.sortedByDescending { it.date }
            sortedValorations.forEach { valoration ->
                var showMenu by remember { mutableStateOf(false) }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Filled.Person, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = valoration.username,
                                )
                            }
                            Box {
                                if (valoration.username.lowercase() == username.lowercase()) {
                                    IconButton(onClick = { showMenu = true }) {
                                        Icon(Icons.Filled.MoreVert, contentDescription = null)
                                    }
                                    DropdownMenu(
                                        expanded = showMenu,
                                        onDismissRequest = { showMenu = false }
                                    ) {
                                        DropdownMenuItem(
                                            onClick = {
                                                deleteValoration(valoration.id)
                                                showMenu = false
                                            },
                                            text = {
                                                Text(stringResource(R.string.delete))
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        Text(
                            text = valoration.date.toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                        RatingBar(
                            rating = valoration.score.toFloat(),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        Text(
                            text = valoration.review,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    } else {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.no_valorations),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.weight(1f))

            ExtendedFloatingActionButton(
                onClick = { valorationDialogOpen = true },
                icon = { Icon(Icons.Default.Edit, stringResource(R.string.write_review)) },
                text = { Text(text = stringResource(R.string.write_review)) },
            )
        }
    }
}


@Composable
fun RatingBar(rating: Float, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        for (i in 1..5) {
            Icon(
                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Filled.StarBorder,
                contentDescription = null,
                tint = Color.Yellow
            )
        }
    }
}

@Composable
fun ImageCarousel(images: List<String>) {
    val showDialog = remember { mutableStateOf(false) }
    val currentImageIndex = remember { mutableStateOf(0) }
    val scrollState = rememberScrollState()
    Row(Modifier.horizontalScroll(scrollState)) {
        images.forEachIndexed { index, imageUrl ->
            val image: Painter = rememberAsyncImagePainter(imageUrl)
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .clickable {
                        currentImageIndex.value = index
                        showDialog.value = true
                    },
                alignment = Alignment.CenterStart,
                contentScale = ContentScale.Crop
            )
        }
    }
    if (showDialog.value) {
        Dialog(onDismissRequest = { showDialog.value = false }) {
            Box {
                val image: Painter = rememberAsyncImagePainter(images[currentImageIndex.value])
                Image(
                    painter = image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(600.dp),
                    contentScale = ContentScale.Crop
                )

                if (currentImageIndex.value > 0) {
                    Surface(
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.5f),
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(16.dp)
                    ) {
                        IconButton(onClick = { currentImageIndex.value-- }) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.previous_image),
                                tint = Color.White
                            )
                        }
                    }
                }

                if (currentImageIndex.value < images.size - 1) {
                    Surface(
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.5f),
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(16.dp)
                    ) {
                        IconButton(onClick = { currentImageIndex.value++ }) {
                            Icon(
                                Icons.Filled.ArrowForward,
                                contentDescription = stringResource(R.string.next_image),
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TextDescription(description: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = stringResource(R.string.description),
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
        )
    }
}

@Composable
fun TextTitle(title: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(dimensionResource(R.dimen.medium_padding)),
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 30.sp,
                fontWeight = FontWeight.W200
            )
        )
    }
}

@Composable
fun MapLocation(lat: Double, lon: Double) {
    val uiSettings = remember {
        MapUiSettings(
            myLocationButtonEnabled = false,
            zoomControlsEnabled = false,
            compassEnabled = false,
            scrollGesturesEnabled = false
        )
    }
    val cameraPosition = remember(lat, lon) {
        mutableStateOf(CameraPosition(LatLng(lat, lon), 13f, 0f, 0f))
    }
    val context = LocalContext.current
    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(R.dimen.map_height))
            .clip(RoundedCornerShape(dimensionResource(R.dimen.medium_padding))),
        uiSettings = uiSettings,
        cameraPositionState = CameraPositionState(cameraPosition.value),
        onMapClick = {
            val gmmIntentUri = Uri.parse("geo:$lat,$lon?q=$lat,$lon")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            if (mapIntent.resolveActivity(context.packageManager) != null) {
                context.startActivity(mapIntent)
            }
        }
    ) {
        Marker(
            state = MarkerState(position = LatLng(lat, lon)),
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
        )
    }
}