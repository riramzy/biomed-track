package com.riramzy.biomedtrack.ui.screens.equipment

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.riramzy.biomedtrack.ui.components.BioMedEquipmentDetailsCard
import com.riramzy.biomedtrack.ui.components.BioMedMaintenanceCard
import com.riramzy.biomedtrack.ui.components.BioMedNavBar
import com.riramzy.biomedtrack.ui.components.BioMedTopAppBar
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun EquipmentDetailScreen(navController: NavHostController, equipmentId: String?) {
    Scaffold(
        topBar = {
            BioMedTopAppBar(
                modifier = Modifier.padding(
                    top = 10.dp
                )
            )
        },
        floatingActionButton = { BioMedNavBar() },
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
            //verticalArrangement = Arrangement.Center
        ) {
            item {
                BioMedEquipmentDetailsCard(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(
                            top = 20.dp,
                            bottom = 15.dp
                        )
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Maintenance History",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                bottom = 15.dp,
                                start = 15.dp
                            )
                    )

                    BioMedMaintenanceCard(modifier = Modifier.padding(bottom = 10.dp))
                    BioMedMaintenanceCard(modifier = Modifier.padding(bottom = 10.dp))
                    BioMedMaintenanceCard()
                }
            }
        }
    }
}

@Preview(device = "id:pixel_9", showBackground = true)
@Composable
fun EquipmentDetailsScreenPreview() {
    BioMedTheme {
        EquipmentDetailScreen(navController = NavHostController(LocalContext.current), equipmentId = null)
    }
}

@Preview(device = "id:pixel_9", showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun EquipmentDetailsScreenDarkPreview() {
    BioMedTheme {
        EquipmentDetailScreen(NavHostController(LocalContext.current), equipmentId = null)
    }
}