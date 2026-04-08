package com.riramzy.biomedtrack.ui.screens.dashboard

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.riramzy.biomedtrack.ui.components.BioMedActivityCard
import com.riramzy.biomedtrack.ui.components.BioMedInsightCard
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.components.custom.BioMedNavBar
import com.riramzy.biomedtrack.ui.components.custom.BioMedTopAppBar
import com.riramzy.biomedtrack.ui.components.department.BioMedDepartmentInsightCard
import com.riramzy.biomedtrack.ui.components.equipment.BioMedEquipmentStatusCard
import com.riramzy.biomedtrack.ui.components.user.BioMedProfileSheet
import com.riramzy.biomedtrack.ui.components.user.BioMedUserCard
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavHostController,
    dashboardVm: DashboardVm = hiltViewModel()
) {
    val uiState by dashboardVm.uiState.collectAsStateWithLifecycle()

    DashboardScreenContent(
        navController = navController,
        uiState = uiState,
        onRetryClick = { dashboardVm.refresh() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreenContent(
    navController: NavHostController,
    uiState: DashboardUiState = DashboardUiState.Loading,
    onRetryClick: () -> Unit = {}
) {
    when(uiState) {
        is DashboardUiState.Error -> {
            Scaffold(
                topBar = {
                    BioMedTopAppBar(
                        modifier = Modifier.padding(
                            top = 10.dp
                        ),
                        onProfileClick = {  }
                    )
                },
                floatingActionButton = {
                    BioMedNavBar(
                        selectedPage = "Dashboard",
                        withActionButton = false,
                        onDashboardClick = { navController.navigate(Screen.Dashboard.route) },
                        onSchedulerClick = { navController.navigate(Screen.Scheduler.route) },
                        onInventoryClick = { navController.navigate(Screen.Inventory.route) },
                        onReportsClick = { navController.navigate(Screen.Reports.route) }
                    )
                },
                floatingActionButtonPosition = FabPosition.Center,
                modifier = Modifier.statusBarsPadding()
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier
                            .padding(10.dp)
                            .height(130.dp)
                            .width(350.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = uiState.message,
                                style = MaterialTheme.typography.titleLarge,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.error
                            )

                            BioMedButton(
                                text = "Retry",
                                onClick = { onRetryClick() },
                                customColor = MaterialTheme.colorScheme.error,
                                customTextColor = MaterialTheme.colorScheme.onError,
                                customTextSize = 16,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                }
            }
        }
        is DashboardUiState.Loading -> {
            Scaffold(
                topBar = {
                    BioMedTopAppBar(
                        modifier = Modifier.padding(
                            top = 10.dp
                        ),
                        onProfileClick = {  }
                    )
                },
                floatingActionButton = {
                    BioMedNavBar(
                        selectedPage = "Dashboard",
                        withActionButton = false,
                        onDashboardClick = { navController.navigate(Screen.Dashboard.route) },
                        onSchedulerClick = { navController.navigate(Screen.Scheduler.route) },
                        onInventoryClick = { navController.navigate(Screen.Inventory.route) },
                        onReportsClick = { navController.navigate(Screen.Reports.route) }
                    )
                },
                floatingActionButtonPosition = FabPosition.Center,
                modifier = Modifier.statusBarsPadding()
            ) { innerPadding ->
                DashboardShimmer(modifier = Modifier.padding(innerPadding))
            }

        }
        is DashboardUiState.Success -> {
            val sheetState = rememberModalBottomSheetState()
            var showBottomSheet by remember { mutableStateOf(false) }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState,
                    dragHandle = { BottomSheetDefaults.DragHandle() },
                    containerColor = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.onSecondary
                    } else {
                        Color.White
                    }
                ) {
                    BioMedProfileSheet(role = uiState.currentUser.role.name)
                }
            }

            Scaffold(
                topBar = {
                    BioMedTopAppBar(
                        modifier = Modifier.padding(
                            top = 10.dp
                        ),
                        onProfileClick = { showBottomSheet = true }
                    )
                },
                floatingActionButton = {
                    BioMedNavBar(
                        selectedPage = "Dashboard",
                        withActionButton = false,
                        onDashboardClick = { navController.navigate(Screen.Dashboard.route) },
                        onSchedulerClick = { navController.navigate(Screen.Scheduler.route) },
                        onInventoryClick = { navController.navigate(Screen.Inventory.route) },
                        onReportsClick = { navController.navigate(Screen.Reports.route) }
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
                        BioMedUserCard(
                            username = uiState.currentUser.name,
                            role = uiState.currentUser.role.name,
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
                                    value = uiState.stats.total.toString(),
                                    status = ""
                                )

                                BioMedInsightCard(
                                    value = uiState.stats.online.toString(),
                                    status = "Online"
                                )
                            }

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                BioMedInsightCard(
                                    value = uiState.stats.dueService.toString(),
                                    status = "Service"
                                )

                                BioMedInsightCard(
                                    value = uiState.stats.down.toString(),
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
                                ),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
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

                            uiState.recentActivities.forEach { log ->
                                BioMedActivityCard(
                                    status = log.newStatus.name,
                                    name = log.equipmentName,
                                    serialNumber = log.equipmentSerial,
                                    model = log.equipmentModel,
                                    department = log.department.name,
                                    changedBy = log.changedBy,
                                    modifier = Modifier.padding(bottom = 10.dp)
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
                                ),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
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

                            uiState.upcomingMaintenance.forEach { upcomingMaintenance ->
                                BioMedEquipmentStatusCard(
                                    status = upcomingMaintenance.status.name,
                                    modifier = Modifier.padding(bottom = 10.dp)
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
                                ),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
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

                            uiState.currentUser.assignedDepartments.forEach { department ->
                                BioMedDepartmentInsightCard(
                                    department = department.name,
                                    departmentTotalEquipment = department.totalEquipment.toString(),
                                    modifier = Modifier.padding(bottom = 10.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(device = "id:pixel_9")
@Composable
fun DashboardScreenPreview() {
    BioMedTheme {
        DashboardScreenContent(
            navController = NavHostController(LocalContext.current)
        )
    }
}

@Preview(device = "id:pixel_9",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = true, backgroundColor = 0xFF000000, showSystemUi = false
)
@Composable
fun DashboardScreenDarkPreview() {
    BioMedTheme {
        DashboardScreenContent(
            navController = NavHostController(LocalContext.current)
        )
    }
}