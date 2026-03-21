package com.riramzy.biomedtrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.riramzy.biomedtrack.ui.components.BioMedInsightCard
import com.riramzy.biomedtrack.ui.components.BioMedNavBar
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BioMedTheme {
                Scaffold(
                    floatingActionButton = {
                        BioMedNavBar()
                    },

                    floatingActionButtonPosition = FabPosition.Center
                ) { innerPadding ->
                    BioMedInsightCard(
                        modifier = Modifier.padding(innerPadding),
                        icon = R.drawable.insight_online,
                        title = "Healthy",
                        isHealthy = true,
                        value = "220",
                        description = "Operating normally"
                    )
                }
            }
        }
    }
}