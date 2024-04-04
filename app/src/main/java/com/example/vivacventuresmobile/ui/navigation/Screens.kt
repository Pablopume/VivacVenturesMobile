package com.example.vivacventuresmobile.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Movie
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.vivacventuresmobile.ui.common.ConstantesPantallas


val screensBottomBar = listOf(
    Screens(ConstantesPantallas.MAP, Icons.Filled.Movie),
    Screens(ConstantesPantallas.LUGARES, Icons.Filled.Celebration),
)

data class Screens(val route: String, val icon: ImageVector) {

}
