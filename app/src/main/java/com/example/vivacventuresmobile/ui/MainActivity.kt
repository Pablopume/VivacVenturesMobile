package com.example.vivacventuresmobile.ui

import android.animation.ObjectAnimator
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.platform.LocalContext
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.dataStore
import com.example.vivacventuresmobile.data.preferences.AppPreferencesSerialize
import com.example.vivacventuresmobile.ui.navigation.Navigation
import com.example.vivacventuresmobile.ui.theme.VivacVenturesMobileTheme
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.AndroidEntryPoint

val Context.dataStore by dataStore("app.settings.json", AppPreferencesSerialize)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel> ()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !viewModel.isReady.value
            }
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.4f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 500L
                zoomX.doOnEnd { screen.remove() }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.4f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 500L
                zoomY.doOnEnd { screen.remove() }

                zoomX.start()
                zoomY.start()
            }
        }
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyAJhTuHWdTmBCIsJkZ-_QrwxmfPvw3Qx5I")
        }
        setContent {
            VivacVenturesMobileTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(dataStore)
                }
            }
        }
    }

    companion object {
        fun checkPermissions(context: Context): Boolean {
            val permissions = arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
            )

            return permissions.all {
                ContextCompat.checkSelfPermission(
                    context,
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }
        }
    }
}
