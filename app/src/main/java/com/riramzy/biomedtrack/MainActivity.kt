package com.riramzy.biomedtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.riramzy.biomedtrack.ui.screens.dashboard.DashboardScreen
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BioMedTheme {
                DashboardScreen(
                    navController = rememberNavController()
                )
            }
        }
    }
}