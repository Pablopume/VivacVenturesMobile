package com.example.vivacventuresmobile.ui.common

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.vivacventuresmobile.R
import com.example.vivacventuresmobile.ui.navigation.Screens

@Composable
fun BottomBar(
    navController: NavController,
    screens: List<Screens>,

    ) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.secondary,
    ) {
        val state = navController.currentBackStackEntryAsState()
        val currentDestination = state.value?.destination
        screens.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = {
                    when (screen.route) {
                        ConstantesPantallas.LUGARES -> Text(
                            stringResource(R.string.places)
                        )
                        ConstantesPantallas.MAP -> Text(
                            stringResource(R.string.map)
                        )
                        ConstantesPantallas.CUENTA -> Text(
                            stringResource(id = R.string.account)
                        )
                        else -> Text(
                            stringResource(R.string.app_name)
                        )
                    }
                },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(screen.route) {
                            inclusive = true
                        }
                    }
                },
            )

        }
    }
}