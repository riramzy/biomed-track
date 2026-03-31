package com.riramzy.biomedtrack.ui.screens.scheduler

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.components.BioMedNavBar
import com.riramzy.biomedtrack.ui.components.BioMedScheduleMaintenanceCard
import com.riramzy.biomedtrack.ui.components.BioMedTopAppBar
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun ScheduleMaintenanceScreen() {
    Scaffold(
        topBar = {
            BioMedTopAppBar(
                modifier = Modifier.padding(
                    top = 10.dp
                )
            )
        },
        bottomBar = {
            BioMedNavBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                withActionButton = true,
                actionButtonIcon = R.drawable.activity_online,
                selectedPage = "Scheduler"
            )
        },
        modifier = Modifier.statusBarsPadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            BioMedScheduleMaintenanceCard(
                modifier = Modifier.padding(top = 20.dp)
            )
        }
    }
}

@Preview(device = "id:pixel_9", showBackground = true)
@Composable
fun ScheduleMaintenanceScreenPreview() {
    BioMedTheme {
        ScheduleMaintenanceScreen()
    }
}

@Preview(device = "id:pixel_9", showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun ScheduleMaintenanceScreenDarkPreview() {
    BioMedTheme {
        ScheduleMaintenanceScreen()
    }
}