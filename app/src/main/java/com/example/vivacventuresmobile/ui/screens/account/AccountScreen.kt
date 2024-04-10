package com.example.vivacventuresmobile.ui.screens.account

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AccountScreen(
    bottomNavigationBar: @Composable () -> Unit = {},
) {
    Account(bottomNavigationBar)
}

@Composable
fun Account(
//    state: AccountState,
//    onErrorVisto: () -> Unit,
    bottomNavigationBar: @Composable () -> Unit = {},
) {
    Scaffold(
        bottomBar = bottomNavigationBar
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row {
                Text(text = "[PRÓXIMAMENTE]")
            }

        }

    }
}
