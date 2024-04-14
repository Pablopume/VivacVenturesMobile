package com.example.vivacventuresmobile.ui

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.core.content.ContextCompat
import androidx.datastore.dataStore
import com.example.vivacventuresmobile.data.preferences.AppPreferencesSerialize
import com.example.vivacventuresmobile.ui.navigation.Navigation
import com.example.vivacventuresmobile.ui.theme.VivacVenturesMobileTheme
import dagger.hilt.android.AndroidEntryPoint

val Context.dataStore by dataStore("app.settings.json", AppPreferencesSerialize)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
