package com.riramzy.biomedtrack.ui.screens.dashboard

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import com.riramzy.biomedtrack.ui.components.BioMedActivityCard
import com.riramzy.biomedtrack.ui.components.BioMedDepartmentInsightCard
import com.riramzy.biomedtrack.ui.components.BioMedEquipmentStatusCard
import com.riramzy.biomedtrack.ui.components.BioMedInsightCard
import com.riramzy.biomedtrack.ui.components.BioMedNavBar
import com.riramzy.biomedtrack.ui.components.BioMedTopAppBar
import com.riramzy.biomedtrack.ui.components.BioMedUserCard
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun DashboardScreen(navController: NavHostController) {
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                BioMedUserCard(
                    username = "Fadwa",
                    role = "Supervisor",
                    modifier = Modifier.padding(
                        bottom = 15.dp,
                        top = 20.dp
                    )
                )
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 15.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 10.dp)
                    ) {
                        BioMedInsightCard(
                            value = "275",
                            status = ""
                        )

                        BioMedInsightCard(
                            value = "220",
                            status = "Online"
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BioMedInsightCard(
                            value = "23",
                            status = "Service"
                        )

                        BioMedInsightCard(
                            value = "17",
                            status = "Down"
                        )
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 10.dp,
                            end = 10.dp,
                            bottom = 15.dp
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Recent Activities",
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Text(
                            text = "View All",
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    BioMedActivityCard(modifier = Modifier.padding(bottom = 10.dp))
                    BioMedActivityCard(status = "Log", modifier = Modifier.padding(bottom = 10.dp))
                    BioMedActivityCard(status = "Down", modifier = Modifier.padding(bottom = 10.dp))
                    BioMedActivityCard(status = "Service")
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 10.dp,
                            end = 10.dp,
                            bottom = 15.dp
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Upcoming Maintenance",
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Text(
                            text = "View All",
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    BioMedEquipmentStatusCard(status = "Service", modifier = Modifier.padding(bottom = 10.dp))
                    BioMedEquipmentStatusCard(status = "Service", modifier = Modifier.padding(bottom = 10.dp))
                    BioMedEquipmentStatusCard(status = "Down")
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 10.dp,
                            end = 10.dp,
                            bottom = 15.dp
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Departments Overview",
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Text(
                            text = "View All",
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    BioMedDepartmentInsightCard(modifier = Modifier.padding(bottom = 10.dp))
                    BioMedDepartmentInsightCard()
                }
            }
        }
    }
}

@Preview(device = "id:pixel_9")
@Composable
fun DashboardScreenPreview() {
    BioMedTheme {
        DashboardScreen(navController = NavHostController(LocalContext.current))
    }
}

@Preview(device = "id:pixel_9",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun DashboardScreenDarkPreview() {
    BioMedTheme {
        DashboardScreen(navController = NavHostController(LocalContext.current))
    }
}