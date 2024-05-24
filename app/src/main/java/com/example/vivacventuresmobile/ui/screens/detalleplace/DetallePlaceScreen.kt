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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.domain.modelo.Valoration
import com.example.vivacventuresmobile.ui.screens.listplaces.ListPlacesEvent
import com.example.vivacventuresmobile.ui.screens.map.LoadingAnimation
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
        bottomNavigationBar,
        { viewModel.handleEvent(DetallePlaceEvent.AddFavourite()) },
        { viewModel.handleEvent(DetallePlaceEvent.DeleteFavourite()) },
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
    bottomNavigationBar: @Composable () -> Unit,
    favourite: () -> Unit,
    unfavourite: () -> Unit,
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

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = { Text(text = state.vivacPlace?.type ?: "") },
                actions = {
                    if (state.vivacPlace?.username == state.username) {
                        IconButton(onClick = { delete() }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete")
                        }
                        IconButton(onClick = { onUpdatePlace(state.vivacPlace.id) }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit")
                        }
                    } else {
                        IconButton(onClick = { reportDialogOpen = true }) {
                            Icon(Icons.Filled.Report, contentDescription = "Report")
                        }
                    }
                    if (state.vivacPlace?.favorite == true) {
                        IconButton(onClick = { unfavourite() }) {
                            Icon(Icons.Filled.Favorite, contentDescription = "Unfavorite")
                        }
                    } else {
                        IconButton(onClick = { favourite() }) {
                            Icon(Icons.Filled.FavoriteBorder, contentDescription = "Favorite")
                        }
                    }
                }
            )
        },
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
        if (state.loading) {
            LoadingAnimation(state.loading)
        } else {
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
                    CapacidadText(capacidad = state.vivacPlace?.capacity ?: 0)
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    DateText(date = state.vivacPlace?.date.toString())
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    PriceText(price = state.vivacPlace?.price ?: 0.0)
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
                            title = { Text("Report") },
                            text = {
                                TextField(
                                    value = state.descriptionReport,
                                    onValueChange = onDescriptionReportChange,
                                    label = { Text("Report Description") }
                                )
                            },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        onAddReport()
                                        reportDialogOpen = false
                                    }
                                ) {
                                    Text("Send")
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = { reportDialogOpen = false }
                                ) {
                                    Text("Cancel")
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
    if (valoration.isNotEmpty()) {
        val mediatotal = valoration.map { it.score }.average()
        var valorationDialogOpen by remember { mutableStateOf(false) }
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Valoración media: $mediatotal",
                style = MaterialTheme.typography.titleMedium
            )
            Button(onClick = { valorationDialogOpen = true }) {
                Text("Add")
            }
        }

        if (valorationDialogOpen) {
            AlertDialog(
                onDismissRequest = { valorationDialogOpen = false },
                title = { Text("Add Valoration") },
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
                            Text(text = "Score: $score")
                        }
                        TextField(
                            value = review,
                            onValueChange = {
                                onReviewValorationChange(it)
                            },
                            label = { Text("Review") }
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
                        Text("Add")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { valorationDialogOpen = false }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
        Column {
            val sortedValorations = valoration.sortedByDescending { it.date }
            sortedValorations.forEach { valoration ->
                var showMenu by remember { mutableStateOf(false) }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = valoration.username,
                                style = MaterialTheme.typography.labelMedium
                            )
                            Text(
                                text = valoration.date.toString(),
                                style = MaterialTheme.typography.bodySmall
                            )
                            Box {
                                if (valoration.username == username) {
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
                                                Text("Delete")
                                            }
                                        )
                                    }
                                }
                            }
                        }
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
                                contentDescription = "Previous image",
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
                                contentDescription = "Next image",
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
    Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun TextTitle(title: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge
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
    val cameraPosition = remember {
        mutableStateOf(CameraPosition(LatLng(lat, lon), 13f, 0f, 0f))
    }
    val context = LocalContext.current
    GoogleMap(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
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

@Composable
fun CapacidadText(capacidad: Int) {
    Text(
        text = "Capacidad: $capacidad",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun DateText(date: String) {
    Text(
        text = "Fecha: $date",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp)
    )
}

@Composable
fun PriceText(price: Double) {
    if (price != 0.0) {
        Text(
            text = "Precio: $price",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )
    }
}