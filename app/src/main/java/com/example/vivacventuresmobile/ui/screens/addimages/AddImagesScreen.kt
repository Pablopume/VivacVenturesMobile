package com.example.vivacventuresmobile.ui.screens.addimages

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.ui.screens.addplace.AddPlaceState
import com.example.vivacventuresmobile.ui.screens.map.LoadingAnimation

//@Composable
//fun AddImagesScreen(
//    viewModel: AddImagesViewModel = hiltViewModel(),
//    bottomNavigationBar: @Composable () -> Unit = {},
//    onAddDone: () -> Unit,
//    dataStore: DataStore<AppPreferences>
//) {
//    val state = viewModel.uiState.collectAsStateWithLifecycle()
//
//    val appPreferences = dataStore.data.collectAsState(initial = AppPreferences()).value
//    val username = appPreferences.username
//    viewModel.handleEvent(AddImagesEvent.AddUsername(username))
//
//
//    AddImages(
//        state.value,
//        { viewModel.handleEvent(AddImagesEvent.ErrorVisto) },
//        bottomNavigationBar,
//        onAddDone,
//        { viewModel.handleEvent(AddImagesEvent.AddUri(it)) },
//        { viewModel.handleEvent(AddImagesEvent.DeleteUri(it)) },
//    )
//}

//@Composable
//fun AddImages(
//    value: AddPlaceState,
//    function: () -> Unit,
//    bottomNavigationBar: @Composable () -> Unit,
//    onAddDone: () -> Unit,
//    AddUri: (List<Uri>) -> Unit,
//    DeleteUri: (Int) -> Unit
//) {
//    Column {
//        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
//        ImageN(
//            index = 0,
//            onDelete = { DeleteUri(it) },
//            images = value.uris
//        )
//        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
//        ImageN(
//            index = 1,
//            onDelete = { DeleteUri(it) },
//            images = value.uris
//        )
//        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
//        ImageN(
//            index = 3,
//            onDelete = { DeleteUri(it) },
//            images = value.uris
//        )
//        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
//        ImagesPicker(
//            value.images,
//            AddUri
//        )
//    }
//}

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
                    state.imagesToDelete,
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


//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(innerPadding),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.SpaceBetween // Distributes space evenly
//        ) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                ImagesPicker(
//                    state.images,
//                    AddUri
//                )
//                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
//                ImageN(
//                    index = 0,
//                    onDelete = { DeleteUri(it) },
//                    images = state.uris
//                )
//                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
//                ImageN(
//                    index = 1,
//                    onDelete = { DeleteUri(it) },
//                    images = state.uris
//                )
//                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
//                ImageN(
//                    index = 2,
//                    onDelete = { DeleteUri(it) },
//                    images = state.uris
//                )
////                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
////                ImagesPicker(
////                    state.images,
////                    AddUri
////                )
//            }
//            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                AddButton(AddPlace)
//                Button(onClick = { Vuelta() }) {
//                    Text(text = stringResource(id = R.string.volver))
//                }
//            }
//        }
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

@Composable
fun ImageN(index: Int, onDelete: (Int, Boolean) -> Unit, uris: List<Uri>, images: List<String>) {
    val imageUrl = if (images.size > index) {
        images[index]
    } else {
        if (uris.size > index) {
            uris[index].toString()
        } else {
            "https://firebasestorage.googleapis.com/v0/b/vivacventures-b3fae.appspot.com/o/images%2Fdefault.jpg?alt=media&token=5ef9d6e8-c7b8-47ac-87a3-419d22857a70"
        }
    }

    val image: Painter = rememberAsyncImagePainter(imageUrl)

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            modifier = Modifier
                .size(130.dp, 130.dp)
                .clip(RoundedCornerShape(8.dp)), // Added rounded corners
            painter = image,
            alignment = Alignment.CenterStart,
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.small_padding)))
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
fun ImagesPicker2(
    images: List<String>,
    onPicturesChange: (List<Uri>) -> Unit,
    DeleteUri: (Int, Boolean) -> Unit,
    Vuelta: () -> Unit,
    AddPlace: () -> Unit,
    UpdatePlace: () -> Unit,
    state: AddPlaceState,
    exists: Boolean
) {
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> uri?.let { onPicturesChange(listOf(it)) } }
    )
    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(),
        onResult = { uris -> onPicturesChange(uris) }
    )

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }) {
                Text(text = stringResource(R.string.pick_one_photo))
            }
            Button(onClick = {
                multiplePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            }) {
                Text(text = stringResource(R.string.pick_multiple_photo))
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
//                .padding(dimensionResource(id = R.dimen.medium_padding))
        ) {
            items(images) { imageUrl ->
                val painter = rememberAsyncImagePainter(imageUrl)

                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(id = R.dimen.small_padding)),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(9f)
        ) {
            ImageN(
                index = 0,
                onDelete = { index, isImage -> DeleteUri(index, isImage) },
                uris = state.uris,
                images = state.place.images
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
            ImageN(
                index = 1,
                onDelete = { index, isImage -> DeleteUri(index, isImage) },
                uris = state.uris,
                images = state.place.images
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
            ImageN(
                index = 2,
                onDelete = { index, isImage -> DeleteUri(index, isImage) },
                uris = state.uris,
                images = state.place.images
            )
            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                AddButton(AddPlace, UpdatePlace, exists)
                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
                Button(onClick = { Vuelta() }) {
                    Text(text = stringResource(id = R.string.back))
                }

            }
//        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Column( // Use Row for horizontal centering
//                modifier = Modifier.fillMaxWidth(),
//                horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//                AddButton(AddPlace)
//                Button(onClick = { Vuelta() }) {
//                    Text(text = stringResource(id = R.string.volver))
//                }
//            }
//            AddButton(AddPlace)
//            Button(onClick = { Vuelta() }) {
//                Text(text = stringResource(id = R.string.volver))
//            }
        }

    }
}

//@Composable
//fun ImagesPicker(images: List<String>, onPicturesChange: (List<Uri>) -> Unit) {
//    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.PickVisualMedia(),
//        onResult = { uri -> uri?.let { onPicturesChange(listOf(it)) } }
//    )
//    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.PickMultipleVisualMedia(),
//        onResult = { uris -> onPicturesChange(uris) }
//    )
//
//    Column {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceAround
//        ) {
//            Button(onClick = {
//                singlePhotoPickerLauncher.launch(
//                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
//                )
//            }) {
//                Text(text = "Pick one photo")
//            }
//            Button(onClick = {
//                multiplePhotoPickerLauncher.launch(
//                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
//                )
//            }) {
//                Text(text = "Pick multiple photos")
//            }
//        }
//
//        LazyColumn(
//            modifier = Modifier
//                .weight(1f)
////                .padding(dimensionResource(id = R.dimen.medium_padding))
//        ) {
//            items(images) { imageUrl ->
//                val painter = rememberAsyncImagePainter(imageUrl)
//
//                Image(
//                    painter = painter,
//                    contentDescription = null,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = dimensionResource(id = R.dimen.small_padding)),
//                    contentScale = ContentScale.Crop
//                )
//            }
//        }
//
//        Column(
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier.weight(9f)
//        ) {
//            ImageN(
//                index = 0,
//                onDelete = { },
//                images = images.map { Uri.parse(it) }
//            )
//            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
//            ImageN(
//                index = 0,
//                onDelete = { },
//                images = images.map { Uri.parse(it) }
//            )
//            Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
//            ImageN(
//                index = 0,
//                onDelete = { },
//                images = images.map { Uri.parse(it) }
//            )
//
////                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.medium_padding)))
////                ImagesPicker(
////                    state.images,
////                    AddUri
////                )
//        }
//
//    }
//}

//@Composable
//fun ImagesPicker(images: List<String>, onPicturesChange: (List<Uri>) -> Unit) {
//    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.PickVisualMedia(),
//        onResult = { uri -> uri?.let { onPicturesChange(listOf(it)) } }
//    )
//    val multiplePhotoPickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.PickMultipleVisualMedia(),
//        onResult = { uris -> onPicturesChange(uris) }
//    )
//
//    Column {
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.SpaceAround
//        ) {
//            Button(onClick = {
//                singlePhotoPickerLauncher.launch(
//                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
//                )
//            }) {
//                Text(text = "Pick one photo")
//            }
//            Button(onClick = {
//                multiplePhotoPickerLauncher.launch(
//                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
//                )
//            }) {
//                Text(text = "Pick multiple photos")
//            }
//        }
//
//        LazyColumn(
//            modifier = Modifier
//                .weight(1f)
//                .padding(dimensionResource(id = R.dimen.medium_padding))
//        ) {
//            items(images) { imageUrl ->
//                val painter = rememberAsyncImagePainter(imageUrl)
//
//                Image(
//                    painter = painter,
//                    contentDescription = null,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(vertical = dimensionResource(id = R.dimen.small_padding)),
//                    contentScale = ContentScale.Crop
//                )
//            }
//        }
//    }
//}

//@Composable
//fun WeightedImages(onDelete: (Int) -> Unit, images: List<Uri>) {
//    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
//        for (i in images.indices.take(3)) {
//            val imageUrl = images[i].toString()
//            val image: Painter = rememberAsyncImagePainter(imageUrl)
//
//            Column(
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(8.dp)
//            ) {
//                Image(
//                    modifier = Modifier
//                        .size(160.dp, 160.dp)
//                        .clip(RoundedCornerShape(16.dp)),
//                    painter = image,
//                    alignment = Alignment.CenterStart,
//                    contentDescription = "",
//                    contentScale = ContentScale.Crop
//                )
//                Button(onClick = { onDelete(i) }) {
//                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
//                }
//            }
//        }
//    }
//}


//@Composable
//fun WeightedImages(onDelete: (Int) -> Unit, images: List<Uri>) {
//    Column(modifier = Modifier.fillMaxWidth(),
////        horizontalArrangement = Arrangement.SpaceEvenly
//        verticalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        for (i in images.indices.take(3)) {
//            val imageUrl = images[i].toString()
//            val image: Painter = rememberAsyncImagePainter(imageUrl)
//
//            Column(
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(8.dp)
//            ) {
//                Image(
//                    modifier = Modifier
//                        .size(80.dp, 80.dp)
//                        .clip(RoundedCornerShape(16.dp)),
//                    painter = image,
//                    alignment = Alignment.CenterStart,
//                    contentDescription = "",
//                    contentScale = ContentScale.Crop
//                )
//                Button(onClick = { onDelete(i) }) {
//                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
//                }
//            }
//        }
//    }
//}

//@Composable
//fun WeightedImages(onDelete: (Int) -> Unit, images: List<Uri>) {
//    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
//        for (i in images.indices.take(3)) {
//            val imageUrl = images[i].toString()
//            val image: Painter = rememberAsyncImagePainter(imageUrl)
//
//            Column(
//                modifier = Modifier
//                    .weight(1f)
//                    .padding(8.dp)
//            ) {
//                Image(
//                    modifier = Modifier
//                        .size(160.dp, 160.dp)
//                        .clip(RoundedCornerShape(16.dp)),
//                    painter = image,
//                    alignment = Alignment.CenterStart,
//                    contentDescription = "",
//                    contentScale = ContentScale.Crop
//                )
//                Button(onClick = { onDelete(i) }) {
//                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
//                }
//            }
//        }
//    }
//}


//@Preview
//@Composable
//fun AddImagesPreview() {
//    AddImages(
//        value = AddImagesState(),
//        function = {},
//        bottomNavigationBar = {},
//        onAddDone = {},
//        AddUri = {},
//        DeleteUri = {}
//    )
//}
