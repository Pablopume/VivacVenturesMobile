package com.example.vivacventuresmobile.ui.screens.detalleplace

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.domain.modelo.VivacPlace
import com.example.vivacventuresmobile.ui.screens.map.LoadingAnimation

@Composable
fun DetallePlaceScreen(
    viewModel: DetallePlaceViewModel = hiltViewModel(),
    placeId: Int,
    username: String,
    bottomNavigationBar: @Composable () -> Unit = {},
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    if (state.value.vivacPlace == null) {
        viewModel.handleEvent(DetallePlaceEvent.SaveUsernameAndId(username, placeId))
    }

    LaunchedEffect(state.value.deleted) {
        if (state.value.deleted) {
            //onBack()
        }

    }

    DetallePlace(
        state.value,
        { viewModel.handleEvent(DetallePlaceEvent.ErrorVisto) },
        bottomNavigationBar,
        { viewModel.handleEvent(DetallePlaceEvent.AddFavourite()) },
        { viewModel.handleEvent(DetallePlaceEvent.DeleteFavourite()) },
        { viewModel.handleEvent(DetallePlaceEvent.DeletePlace()) }
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
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { /*onBack()*/ }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                title = { Text(text = state.vivacPlace?.type ?: "") },
                actions = {
                    if (state.vivacPlace?.favorite == true) {
                        IconButton(onClick = { unfavourite() }) {
                            Icon(Icons.Filled.Favorite, contentDescription = "Unfavorite")
                        }
                    } else {
                        IconButton(onClick = { favourite() }) {
                            Icon(Icons.Filled.FavoriteBorder, contentDescription = "Favorite")
                        }
                    }
                    if (state.vivacPlace?.username == state.username) {
                        IconButton(onClick = { delete() }) {
                            Icon(Icons.Filled.Delete, contentDescription = "Delete")
                        }
                        IconButton(onClick = { /*onEdit()*/ }) {
                            Icon(Icons.Filled.Edit, contentDescription = "Edit")
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
                ) {
                    TextTitle(title = state.vivacPlace?.name ?: "")
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    ImageCarousel(images = state.vivacPlace?.images ?: emptyList())
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    TextDescription(description = state.vivacPlace?.description ?: "")
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    OpenLocationInMapsButton(
                        lat = state.vivacPlace?.lat ?: 0.0,
                        lon = state.vivacPlace?.lon ?: 0.0
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    CapacidadText(capacidad = state.vivacPlace?.capacity ?: 0)
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    DateText(date = state.vivacPlace?.date.toString())
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                    PriceText(price = state.vivacPlace?.price ?: 0.0)
                }
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
    val scrollState = rememberScrollState()
    Row(Modifier.horizontalScroll(scrollState)) {
        images.forEach { imageUrl ->
            val image: Painter = rememberAsyncImagePainter(imageUrl)
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(6.dp)),
                alignment = Alignment.CenterStart,
                contentScale = ContentScale.Crop
            )
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
fun OpenLocationInMapsButton(lat: Double, lon: Double) {
    val context = LocalContext.current

    Button(onClick = {
        val gmmIntentUri = Uri.parse("geo:$lat,$lon?q=$lat,$lon")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        }
    }) {
        Text("Abrir ubicación en Google Maps")
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
    Text(
        text = "Precio: $price",
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp)
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDetallePlace() {
    DetallePlace(
        state = DetallePlaceState(
            error = null,
            loading = false,
            vivacPlace = VivacPlace(1, "name", "description", 1.0, 1.0, "image")
        ),
        errorVisto = {},
        bottomNavigationBar = {},
        favourite = {},
        unfavourite = {},
        delete = {}
    )
}

