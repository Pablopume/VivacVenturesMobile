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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.common.Constantes
import com.example.vivacventuresmobile.domain.modelo.VivacPlace
import com.example.vivacventuresmobile.domain.modelo.VivacPlaceList
import com.example.vivacventuresmobile.ui.screens.listplaces.FavouriteTag
import com.example.vivacventuresmobile.ui.screens.map.LoadingAnimation
import com.example.vivacventuresmobile.ui.theme.BlueRefugee
import com.example.vivacventuresmobile.ui.theme.GreenVivac
import com.example.vivacventuresmobile.ui.theme.RedAlbergue
import com.example.vivacventuresmobile.ui.theme.YellowRefugee

@Composable
fun MyFavouritesScreen(
    viewModel: MyFavouritesViewModel = hiltViewModel(),
    username: String,
    onViewDetalle: (Int) -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {},
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    if (state.value.username.isEmpty()) {
        viewModel.handleEvent(MyFavouritesEvent.SaveUsername(username))
    }

    PantallaFavourites(
        state.value,
        { viewModel.handleEvent(MyFavouritesEvent.ErrorVisto) },
        onViewDetalle,
        bottomNavigationBar
    )
}

@Composable
fun PantallaFavourites(
    state: MyFavouritesState,
    errorVisto: () -> Unit,
    onViewDetalle: (Int) -> Unit,
    bottomNavigationBar: @Composable () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
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
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    items(
                        items = state.vivacPlaces,
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

@Composable
fun VivacPlaceListItem(
    vivacPlace: VivacPlaceList,
    onViewDetalle: (Int) -> Unit,
    modifier: Modifier = Modifier.background(MaterialTheme.colorScheme.background)
) {
    val backgroundColor = when (vivacPlace.type) {
        "Vivac" -> GreenVivac
        "Refugio" -> BlueRefugee
        "Albergue" -> RedAlbergue
        "Refugio Privado" -> YellowRefugee
        else -> Color.Gray
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.smallmedium_padding))
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.medium_padding)))
            .clickable { onViewDetalle(vivacPlace.id) },
        elevation = CardDefaults.cardElevation(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurface
        ),
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

            Column(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(8.dp)
            ) {
                Text(
                    text = vivacPlace.name,
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = MaterialTheme.colorScheme.surface,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = vivacPlace.type,
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp),
                    color = MaterialTheme.colorScheme.surface,
                    style = MaterialTheme.typography.labelMedium
                )

            }

            if (vivacPlace.favorite) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    FavouriteTag()
                }
            }
        }
    }
}
