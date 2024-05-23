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
import com.example.vivacventuresmobile.ui.screens.addplace.AddPlaceScreen
import com.example.vivacventuresmobile.ui.screens.detalleplace.DetallePlaceScreen
import com.example.vivacventuresmobile.ui.screens.forgotpassword.ForgotPasswordScreen
import com.example.vivacventuresmobile.ui.screens.listplaces.ListPlacesScreen
import com.example.vivacventuresmobile.ui.screens.login.LoginScreen
import com.example.vivacventuresmobile.ui.screens.map.MapScreen
import com.example.vivacventuresmobile.ui.screens.myfavourites.MyFavouritesScreen
import com.example.vivacventuresmobile.ui.screens.myfriends.MyFriendsScreen
import com.example.vivacventuresmobile.ui.screens.myplaces.MyPlacesScreen
import com.example.vivacventuresmobile.ui.screens.register.RegisterScreen


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
                onLoginDone = {
                    navController.navigate(ConstantesPantallas.MAP) {
                        popUpTo(ConstantesPantallas.LOGIN) {
                            inclusive = true
                        }
                    }
                },
                onRegisterClick = {
                    navController.navigate(ConstantesPantallas.REGISTER)
                },
                onForgotPassword = {
                    navController.navigate(ConstantesPantallas.FORGOTPASSWORD)
                },
                dataStore = dataStore
            )
        }
        composable(
            ConstantesPantallas.REGISTER
        ) {
            RegisterScreen(
                onRegisterDone = {
                    navController.navigate(ConstantesPantallas.LOGIN) {
                        popUpTo(ConstantesPantallas.REGISTER) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(
            ConstantesPantallas.FORGOTPASSWORD
        ) {
            ForgotPasswordScreen(
                onPasswordReset = {
                    navController.navigate(ConstantesPantallas.LOGIN) {
                        popUpTo(ConstantesPantallas.FORGOTPASSWORD) {
                            inclusive = true
                        }
                    }
                }
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
            ListPlacesScreen(
                onViewDetalle = { vivacPlaceId ->
                    navController.navigate(ConstantesPantallas.DETALLELUGAR + "${vivacPlaceId}")
                },
                bottomNavigationBar = {
                    BottomBar(
                        navController = navController, screens = screensBottomBar
                    )
                },
                onAddPlace = {
                    navController.navigate(ConstantesPantallas.ADDLUGAR + "${it}")
                },
                username = dataStore.data.collectAsState(initial = AppPreferences()).value.username
            )
        }
        composable(
            ConstantesPantallas.DETALLELUGAR_LUGARID,
            arguments = listOf(navArgument(name = ConstantesPantallas.LUGAR_ID) {
                type = NavType.IntType
                defaultValue = 0
            })
        ) {
            DetallePlaceScreen(
                placeId = it.arguments?.getInt(ConstantesPantallas.LUGAR_ID) ?: 0,
                bottomNavigationBar = {
                    BottomBar(
                        navController = navController, screens = screensBottomBar
                    )
                },
                username = dataStore.data.collectAsState(initial = AppPreferences()).value.username,
                onUpdatePlace = {
                    navController.navigate(ConstantesPantallas.ADDLUGAR + "${it}")
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            ConstantesPantallas.FAVOURITES_USER,
            arguments = listOf(navArgument(name = ConstantesPantallas.USERNAME) {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {
            MyFavouritesScreen(
                username = it.arguments?.getString(ConstantesPantallas.USERNAME) ?: "",
                onViewDetalle = {
                    navController.navigate(ConstantesPantallas.DETALLELUGAR + "${it}")
                },
                bottomNavigationBar = {
                    BottomBar(
                        navController = navController, screens = screensBottomBar
                    )
                }
            )
        }
        composable(
            ConstantesPantallas.MYPLACES_USER,
            arguments = listOf(navArgument(name = ConstantesPantallas.USERNAME) {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {
            MyPlacesScreen(
                username = it.arguments?.getString(ConstantesPantallas.USERNAME) ?: "",
                onViewDetalle = {
                    navController.navigate(ConstantesPantallas.DETALLELUGAR + "${it}")
                },
                bottomNavigationBar = {
                    BottomBar(
                        navController = navController, screens = screensBottomBar
                    )
                }
            )
        }
        composable(
            ConstantesPantallas.MYFRIENDS_USER,
            arguments = listOf(navArgument(name = ConstantesPantallas.USERNAME) {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {
            MyFriendsScreen(
                username = it.arguments?.getString(ConstantesPantallas.USERNAME) ?: "",
                onViewDetalle = {
                    navController.navigate(ConstantesPantallas.DETALLELUGAR + "${it}")
                },
                toSearchFriendsScreen = {
                    navController.navigate(ConstantesPantallas.SEARCHFRIENDS)
                },
                bottomNavigationBar = {
                    BottomBar(
                        navController = navController, screens = screensBottomBar
                    )
                }
            )
        }
        composable(
            ConstantesPantallas.CUENTA
        ) {
            AccountScreen(
                toLoginScreen = {
                    navController.navigate(ConstantesPantallas.LOGIN)
                },
                toFavourites = {
                    navController.navigate(ConstantesPantallas.FAVOURITES + "${it}")
                },
                toMyPlaces = {
                    navController.navigate(ConstantesPantallas.MYPLACES + "${it}")
                },
                toMyFriends = {
                    navController.navigate(ConstantesPantallas.MYFRIENDS + "${it}")
                },
                dataStore = dataStore,
                bottomNavigationBar = {
                    BottomBar(
                        navController = navController, screens = screensBottomBar
                    )
                }
            )
        }
        composable(
            ConstantesPantallas.ADDLUGAR_EXISTS,
            arguments = listOf(navArgument(name = ConstantesPantallas.EXISTS) {
                type = NavType.IntType
                defaultValue = 0
            })
        ) {
            AddPlaceScreen(
                vivacPlace = it.arguments?.getInt(ConstantesPantallas.EXISTS) ?: 0,
                bottomNavigationBar = {
                    BottomBar(
                        navController = navController, screens = screensBottomBar
                    )
                },
                onAddDone = {
                    navController.navigate(ConstantesPantallas.LUGARES) {
                        popUpTo(ConstantesPantallas.ADDLUGAR) {
                            inclusive = true
                        }
                    }
                },
                onUpdateDone = {
                    navController.navigate(ConstantesPantallas.CUENTA) {
                        popUpTo(ConstantesPantallas.ADDLUGAR) {
                            inclusive = true
                        }
                    }
                },
                username = dataStore.data.collectAsState(initial = AppPreferences()).value.username
            )
        }


    }
}
