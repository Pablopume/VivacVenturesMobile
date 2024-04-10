package com.example.vivacventuresmobile.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.vivacventuresmobile.ui.common.ConstantesPantallas


val screensBottomBar = listOf(
    Screens(ConstantesPantallas.LUGARES, Icons.Filled.Terrain),
    Screens(ConstantesPantallas.MAP, Icons.Filled.Map),
    Screens(ConstantesPantallas.CUENTA, Icons.Filled.Person),
)

data class Screens(val route: String, val icon: ImageVector) {

}
