package com.example.vivacventuresmobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vivacventuresmobile.ui.common.BottomBar
import com.example.vivacventuresmobile.ui.common.ConstantesPantallas

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ConstantesPantallas.LOGIN,
    ) {
        composable(
            ConstantesPantallas.LOGIN
        ) {
            LoginScreen(onLoginDone = {
                navController.navigate(ConstantesPantallas.PELICULAS) {
                    popUpTo(ConstantesPantallas.LOGIN) {
                        inclusive = true
                    }
                }
            })
        }
        composable(
            ConstantesPantallas.MAP
        ) {
            PeliculasScreen(onViewDetalle = { peliculaId ->
                navController.navigate(ConstantesPantallas.DETALLEPELICULAS_ + "${peliculaId}")
            }, bottomNavigationBar = {
                BottomBar(
                    navController = navController, screens = screensBottomBar
                )
            })
        }
        composable(ConstantesPantallas.DETALLEPELICULAS_PELICULA_ID_,
            arguments = listOf(navArgument(name = ConstantesPantallas.PELICULA_ID) {
                type = NavType.IntType
                defaultValue = 0
            })) {
            DetallePeliculaScreen(peliculaId = it.arguments?.getInt(ConstantesPantallas.PELICULA_ID)
                ?: 0,
                bottomNavigationBar = {
                    BottomBar(
                        navController = navController, screens = screensBottomBar
                    )
                })
        }
        composable(
            ConstantesPantallas.PREMIOS,
        ) {
            PremiosScreen(onPremioClick = { premioId ->
                navController.navigate(ConstantesPantallas.UPDATEPREMIO_ + "${premioId}")
            }, addPremioClick = {
                navController.navigate(ConstantesPantallas.ADDPREMIO)
            }, bottomNavigationBar = {
                BottomBar(
                    navController = navController, screens = screensBottomBar
                )
            })
        }
        composable(
            ConstantesPantallas.ADDPREMIO
        ) {
            AddPremioScreen(bottomNavigationBar = {
                BottomBar(
                    navController = navController, screens = screensBottomBar
                )
            }, onAddDone = {
                navController.navigate(ConstantesPantallas.PREMIOS) {
                    popUpTo(ConstantesPantallas.ADDPREMIO) {
                        inclusive = true
                    }
                }
            })
        }
        composable(ConstantesPantallas.UPDATEPREMIO_PREMIOID,
            arguments = listOf(navArgument(name = ConstantesPantallas.PREMIOID) {
                type = NavType.IntType
                defaultValue = 0
            })) {
            UpdatePremioScreen(
                onPremioUpdated = {
                    navController.navigate(ConstantesPantallas.PREMIOS) {
                        popUpTo(ConstantesPantallas.UPDATEPREMIO) {
                            inclusive = true
                        }
                    }
                },
                premioId = it.arguments?.getInt(ConstantesPantallas.PREMIOID) ?: 0,
                bottomNavigationBar = {
                    BottomBar(
                        navController = navController, screens = screensBottomBar
                    )
                },

                )
        }
    }
}
