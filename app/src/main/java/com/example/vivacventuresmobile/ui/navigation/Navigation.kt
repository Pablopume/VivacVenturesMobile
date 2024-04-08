package com.example.vivacventuresmobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vivacventuresmobile.ui.common.BottomBar
import com.example.vivacventuresmobile.ui.common.ConstantesPantallas
import com.example.vivacventuresmobile.ui.screens.map.MapScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ConstantesPantallas.MAP,
    ) {
        composable(
            ConstantesPantallas.LOGIN
        ) {
//            LoginScreen(onLoginDone = {
//                navController.navigate(ConstantesPantallas.PELICULAS) {
//                    popUpTo(ConstantesPantallas.LOGIN) {
//                        inclusive = true
//                    }
//                }
//            })
        }
        composable(
            ConstantesPantallas.MAP
        ) {
            MapScreen(onViewDetalle = { peliculaId ->
                navController.navigate(ConstantesPantallas.DETALLEPELICULAS_ + "${peliculaId}")
            }, bottomNavigationBar = {
                BottomBar(
                    navController = navController, screens = screensBottomBar
                )
            })
        }
    }
}
