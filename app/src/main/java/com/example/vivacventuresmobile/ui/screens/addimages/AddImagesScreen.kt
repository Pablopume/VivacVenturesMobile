package com.example.vivacventuresmobile.ui.screens.addimages

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.ui.screens.addplace.AddPlaceState
import com.example.vivacventuresmobile.ui.screens.map.LoadingAnimation



@Composable
fun AddImages(
    state: AddPlaceState,
    errorVisto: () -> Unit,
    bottomNavigationBar: @Composable () -> Unit,
    onAddDone: () -> Unit,
    onUpdateDone: () -> Unit,
    AddUri: (List<Uri>) -> Unit,
    DeleteUri: (Int, Boolean) -> Unit,
    AddPlace: () -> Unit,
    UpdatePlace: () -> Unit,
    Vuelta: () -> Unit,
    exists : Boolean
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.addPlaceDone) {
        if (state.addPlaceDone) {
            onAddDone()
        }
    }
    LaunchedEffect(state.updatePlaceDone) {
        if (state.updatePlaceDone) {
            onUpdateDone()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = bottomNavigationBar
    ) { innerPadding ->
        LaunchedEffect(state.error) {
            state.error?.let {
                snackbarHostState.showSnackbar(
                    message = state.error.toString(),
                    duration = SnackbarDuration.Short,
                )
                errorVisto()
            }
        }

        if (state.loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingAnimation(state.loading)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween // Distributes space evenly
            ) {
                ImagesPicker2(
                    AddUri,
                    DeleteUri,
                    Vuelta,
                    AddPlace,
                    UpdatePlace,
                    state,
                    exists
                )
            }
        }
    }
}



@Composable
fun ImagesPicker2(
    onPicturesChange: (List<Uri>) -> Unit,
    DeleteUri: (Int, Boolean) -> Unit,
    Vuelta: () -> Unit,
    AddPlace: () -> Unit,
    UpdatePlace: () -> Unit,
    state: AddPlaceState,
    exists: Boolean
) {
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris -> onPicturesChange(uris) }
    )

    Column(
        modifier = Modifier
            .padding(top = dimensionResource(id = R.dimen.medium_padding))
    )  {
        Text(
            text = "Añade Imágenes del lugar",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .padding(bottom = dimensionResource(id = R.dimen.medium_padding))
                .align(Alignment.CenterHorizontally)
        )
        Column  {
            repeat(3) { index ->
                ImageN(
                    index = index,
                    onDelete = { idx, isImage -> DeleteUri(idx, isImage) },
                    uris = state.uris,
                    images = state.place.images,
                    onImageClick = {
                        multiplePhotoPickerLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AddButton(AddPlace, UpdatePlace, exists)
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
        Button(onClick = { Vuelta() }) {
            Text(text = stringResource(id = R.string.back))
        }

    }

}

@Composable
fun ImageN(
    index: Int,
    onDelete: (Int, Boolean) -> Unit,
    uris: List<Uri>,
    images: List<String>,
    onImageClick: () -> Unit
) {
    val imageUrl = if (images.size > index) {
        images[index]
    } else {
        if (uris.size > index) {
            uris[index].toString()
        } else {
            "https://firebasestorage.googleapis.com/v0/b/vivacventures-b3fae.appspot.com/o/images%2FAddImage2.png?alt=media&token=445adaff-d0d1-4ddd-8d41-aa293b632f5f"
        }
    }

    val image: Painter = rememberAsyncImagePainter(imageUrl)

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onImageClick() } // Añadido clickable para lanzar el selector de imágenes
    ) {
        Image(
            modifier = Modifier
                .size(130.dp, 130.dp)
                .clip(RoundedCornerShape(8.dp)),
            painter = image,
            alignment = Alignment.CenterStart,
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.medium_padding)))
        Button(onClick = {
            if (images.size > index) {
                onDelete(index, true)
            } else {
                onDelete(index, false)
            }
        }) {
            Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete))
        }
    }
}

@Composable
fun AddButton(onAddPlaceClick: () -> Unit, onUpdatePlace:() -> Unit , exists: Boolean) {
    FloatingActionButton(onClick = {
        if (exists) {
            onUpdatePlace()
        } else {
            onAddPlaceClick()
        }
    }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.small_padding))
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.add)
            )
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.smallmedium_padding)))
            Text(text = stringResource(id = if (exists) R.string.updateplace else R.string.addplace))
        }
    }
}