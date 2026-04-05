package com.riramzy.biomedtrack.ui.screens.importing

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.ui.components.custom.BioMedNavBar
import com.riramzy.biomedtrack.ui.components.custom.BioMedProgressIndicator
import com.riramzy.biomedtrack.ui.components.custom.BioMedTopAppBar
import com.riramzy.biomedtrack.ui.components.importing.BioMedImportingProgressCard
import com.riramzy.biomedtrack.ui.components.importing.ImportLog
import com.riramzy.biomedtrack.ui.components.importing.LogType
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun ImportEquipmentProgressScreen() {
    Scaffold(
        topBar = {
            BioMedTopAppBar(
                modifier = Modifier.padding(
                    top = 10.dp
                )
            )
        },
        floatingActionButton = {
            BioMedNavBar(
                selectedPage = "None",
                withActionButton = true,
                isActionButtonText = true,
                actionButtonText = "Confirm",
                modifier = Modifier.padding(horizontal = 15.dp)
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        modifier = Modifier.statusBarsPadding()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                bottom = innerPadding.calculateBottomPadding() + 100.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = 15.dp,
                            top = 20.dp,
                            start = 15.dp,
                            end = 15.dp
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = "Import Equipment",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )

                    Text(
                        text = "Bulk import equipment from an excel file",
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            item {
                BioMedProgressIndicator(
                    steps = listOf("Select File", "Preview", "Import"),
                    currentStep = 2,
                    modifier = Modifier
                        .padding(bottom = 15.dp)
                )
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(
                            bottom = 15.dp,
                            start = 15.dp,
                            end = 15.dp
                        )
                ) {
                    Text(
                        text = "Step 3: Importing",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }

            item {
                BioMedImportingProgressCard(
                    progress = 0.2f,
                    logs = listOf(
                        ImportLog("Imported Fresenius 4008S - 4545885N70", LogType.SUCCESS),
                        ImportLog("4 rows skipped successfully", LogType.WARNING),
                        ImportLog("Failed to import 2 rows", LogType.ERROR)
                    )
                )
            }
        }
    }
}

@Preview(device = "id:pixel_9")
@Composable
fun ImportEquipmentProgressScreenPreview() {
    BioMedTheme {
        ImportEquipmentProgressScreen()
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun ImportEquipmentProgressScreenDarkPreview() {
    BioMedTheme {
        ImportEquipmentProgressScreen()
    }
}