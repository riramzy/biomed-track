package com.riramzy.biomedtrack.ui.screens.inventory

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.components.custom.BioMedHorizontalSelector
import com.riramzy.biomedtrack.ui.components.custom.BioMedNavBar
import com.riramzy.biomedtrack.ui.components.custom.BioMedSearchBar
import com.riramzy.biomedtrack.ui.components.custom.BioMedTopAppBar
import com.riramzy.biomedtrack.ui.components.equipment.BioMedEquipmentOverviewCard
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.Screen

@Composable
fun InventoryScreen(
    navController: NavHostController,
    inventoryVm: InventoryVm = hiltViewModel()
) {
    val state by inventoryVm.uiState.collectAsStateWithLifecycle()

    InventoryScreenContent(
        navController,
        state = state,
        onSearchQueryChange = { inventoryVm.searchInventory(it) },
        onDepartmentSelected = { inventoryVm.filterByDepartment(it) },
        onRetryClick = {  }
    )
}

@Composable
fun InventoryScreenContent(
    navController: NavHostController,
    state: InventoryUiState = InventoryUiState.Loading,
    onSearchQueryChange: (String) -> Unit = {},
    onDepartmentSelected: (Department?) -> Unit = {},
    onRetryClick: () -> Unit = {}
) {
    when(state) {
        is InventoryUiState.Error -> {
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
                        selectedPage = "Inventory",
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
                                text = state.message,
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
        is InventoryUiState.Loading -> {
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
                        selectedPage = "Inventory",
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
                InventoryShimmer(modifier = Modifier.padding(innerPadding))
            }
        }
        is InventoryUiState.Success -> {
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
                        selectedPage = "Inventory",
                        withActionButton = state.canAddEquipment,
                        actionButtonIcon = R.drawable.add,
                        isActionButtonText = false,
                        onDashboardClick = { navController.navigate(Screen.Dashboard.route) },
                        onSchedulerClick = { navController.navigate(Screen.Scheduler.route) },
                        onInventoryClick = { navController.navigate(Screen.Inventory.route) },
                        onReportsClick = { navController.navigate(Screen.Reports.route) },
                        onActionButtonClick = { navController.navigate(Screen.AddEquipment.route) }
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
                                text = "Inventory",
                                style = MaterialTheme.typography.titleLarge,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                            )

                            Text(
                                text = "Manage and track all medical equipment",
                                style = MaterialTheme.typography.labelLarge,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }

                    item {
                        Column(
                            modifier = Modifier
                                .padding(
                                    bottom = 15.dp,
                                    start = 15.dp,
                                    end = 15.dp
                                ),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Row(
                                modifier = Modifier.padding(bottom = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                            ) {
                                BioMedSearchBar(
                                    modifier = Modifier
                                        .height(35.dp)
                                        .weight(3f),
                                    value = state.searchQuery,
                                    onValueChange = { onSearchQueryChange(it) }
                                )

                                BioMedButton(
                                    withIcon = true,
                                    icon = R.drawable.filter,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            BioMedHorizontalSelector(
                                items = listOf("All") + state.departments.map { it.name },
                                selectedItem = state.selectedDepartment?.name ?: "All",
                                onItemSelected = { selectedDepartment ->
                                    val department = if (selectedDepartment == "All") {
                                        null
                                    } else {
                                        state.departments.find { it.name == selectedDepartment }
                                    }
                                    onDepartmentSelected(department)
                                }
                            )
                        }
                    }

                    item {
                        if (state.equipment.isEmpty()) {
                            Column(
                                modifier = Modifier
                                    .padding(15.dp)
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No equipment found",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )

                                Text(
                                    text = "Try changing the department filter or search query",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    items(
                        items = state.equipment,
                        key = { it.id }
                    ) { equipment ->
                        BioMedEquipmentOverviewCard(
                            modifier = Modifier
                                .padding(
                                    bottom = 10.dp,
                                    start = 15.dp,
                                    end = 15.dp
                                ),
                            name = equipment.name,
                            status = equipment.status,
                            model = equipment.model,
                            serialNumber = equipment.serialNumber,
                            category = equipment.category,
                            department = equipment.department,
                            nextServiceDate = equipment.nextMaintenanceDate,
                            onCardClick = { navController.navigate(Screen.EquipmentDetail.createRoute(equipment.id)) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true)
@Composable
fun InventoryScreenPreview() {
    BioMedTheme {
        InventoryScreenContent(
            navController = NavHostController(LocalContext.current),
            state = InventoryUiState.Success(
                equipment = emptyList(),
                departments = emptyList(),
                selectedDepartment = null,
                searchQuery = "",
                canAddEquipment = false,
                canDeleteEquipment = false
            )
        )
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun InventoryScreenDarkPreview() {
    BioMedTheme {
        InventoryScreenContent(
            navController = NavHostController(LocalContext.current),
            state = InventoryUiState.Success(
                equipment = emptyList(),
                departments = emptyList(),
                selectedDepartment = null,
                searchQuery = "",
                canAddEquipment = false,
                canDeleteEquipment = false
            )
        )
    }
}