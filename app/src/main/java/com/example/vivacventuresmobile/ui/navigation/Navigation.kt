package com.example.vivacventuresmobile.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.vivacventuresmobile.data.preferences.AppPreferences
import com.example.vivacventuresmobile.ui.common.BottomBar
import com.example.vivacventuresmobile.ui.common.ConstantesPantallas
import com.example.vivacventuresmobile.ui.screens.account.AccountScreen
import com.example.vivacventuresmobile.ui.screens.detalleplace.DetallePlaceScreen
import com.example.vivacventuresmobile.ui.screens.listplaces.ListPlacesScreen
import com.example.vivacventuresmobile.ui.screens.login.LoginScreen
import com.example.vivacventuresmobile.ui.screens.logout.LogoutScreen
import com.example.vivacventuresmobile.ui.screens.map.MapScreen

@Composable
fun Navigation(
    dataStore: DataStore<AppPreferences>
) {
    val navController = rememberNavController()

    val appPreferences = dataStore.data.collectAsState(
        initial = AppPreferences()
    ).value

    val userName = appPreferences.username

    NavHost(
        navController = navController,
        startDestination = if (userName.isNotBlank() && !userName.equals("")) ConstantesPantallas.MAP else ConstantesPantallas.LOGIN,
    ) {
        composable(
            ConstantesPantallas.LOGIN
        ) {
            LoginScreen(
                onViewDetalle = {
                    navController.navigate(ConstantesPantallas.MAP) {
                        popUpTo(ConstantesPantallas.LOGIN) {
                            inclusive = true
                            }
                        }
                },
                dataStore = dataStore
            )
        }
        composable(
            ConstantesPantallas.MAP
        ) {
            MapScreen(onViewDetalle = { vivacPlaceId ->
                navController.navigate(ConstantesPantallas.DETALLELUGAR + "${vivacPlaceId}")
            }, bottomNavigationBar = {
                BottomBar(
                    navController = navController, screens = screensBottomBar
                )
            })
        }
        composable(
            ConstantesPantallas.LUGARES
        ) {
            ListPlacesScreen(onViewDetalle = { vivacPlaceId ->
                navController.navigate(ConstantesPantallas.DETALLELUGAR + "${vivacPlaceId}")
            }, bottomNavigationBar = {
                BottomBar(
                    navController = navController, screens = screensBottomBar
                )
            })
        }
        composable(
            ConstantesPantallas.CUENTA
        ) {
            AccountScreen(bottomNavigationBar = {
                BottomBar(
                    navController = navController, screens = screensBottomBar
                )
            })
        }
        composable(
            ConstantesPantallas.DETALLELUGAR_LUGARID,
            arguments = listOf(navArgument(name = ConstantesPantallas.LUGAR_ID) {
                type = NavType.IntType
                defaultValue = 0
            })
        ) {
            DetallePlaceScreen(placeId = it.arguments?.getInt(ConstantesPantallas.LUGAR_ID) ?: 0,
                bottomNavigationBar = {
                    BottomBar(
                        navController = navController, screens = screensBottomBar
                    )
                })
        }
        composable(
            ConstantesPantallas.LOGOUT
        ) {
            LogoutScreen(
                onViewDetalle = {
                    navController.navigate(ConstantesPantallas.LOGIN)
                },
                dataStore = dataStore,
                bottomNavigationBar = {
                    BottomBar(
                        navController = navController, screens = screensBottomBar
                    )
                }
            )
        }
    }
}
