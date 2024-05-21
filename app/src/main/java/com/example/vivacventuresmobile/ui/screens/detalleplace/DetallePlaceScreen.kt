package com.example.vivacventuresmobile.ui.screens.detalleplace

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Report
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    onUpdatePlace: (String) -> Unit
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

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
    onBack: () -> Unit,
    onUpdatePlace: (String) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

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
                        IconButton(onClick = { onUpdatePlace("true") }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit")
                        }
                    } else {
                        IconButton(onClick = { /*TODO*/ }) {
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
                    ValorationsList(valoration = state.vivacPlace?.valorations ?: emptyList())
                }
            }
        }

    }

}

@Composable
fun ValorationsList(valoration: List<Valoration>) {
    if (valoration.isNotEmpty()) {
        val mediatotal = valoration.map { it.score }.average()
        Text(
            text = "Valoración media: $mediatotal",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )
        Column {
            val sortedValorations = valoration.sortedByDescending { it.date }
            sortedValorations.forEach {
                Text(
                    text = "${it.user}: ${it.review}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
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
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge
        )
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
                    IconButton(onClick = { currentImageIndex.value-- },
                        modifier = Modifier.align(Alignment.CenterStart)) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Previous image")
                    }
                }

                if (currentImageIndex.value < images.size - 1) {
                    IconButton(onClick = { currentImageIndex.value++ },
                        modifier = Modifier.align(Alignment.CenterEnd)) {
                        Icon(Icons.Filled.ArrowForward, contentDescription = "Next image")
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
        mutableStateOf(CameraPosition(LatLng(lat, lon),13f, 0f, 0f))
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