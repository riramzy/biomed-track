package com.riramzy.biomedtrack.ui.screens.inventory

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
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
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Equipment
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.components.custom.BioMedHorizontalSelector
import com.riramzy.biomedtrack.ui.components.custom.BioMedNavBar
import com.riramzy.biomedtrack.ui.components.custom.BioMedSearchBar
import com.riramzy.biomedtrack.ui.components.custom.BioMedTopAppBar
import com.riramzy.biomedtrack.ui.components.equipment.BioMedChangeStatusDialog
import com.riramzy.biomedtrack.ui.components.equipment.BioMedEquipmentOverviewCard
import com.riramzy.biomedtrack.ui.components.user.BioMedChangePasswordDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedLogoutDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedMyProfileDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedNotificationPreferencesDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedProfileSheet
import com.riramzy.biomedtrack.ui.screens.auth.AuthVm
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.utils.Screen
import com.riramzy.biomedtrack.utils.Timestamps.toDateString
import com.riramzy.biomedtrack.utils.UserRole

@Composable
fun InventoryScreen(
    navController: NavHostController,
    inventoryVm: InventoryVm = hiltViewModel(),
    authVm: AuthVm = hiltViewModel()
) {
    val state by inventoryVm.uiState.collectAsStateWithLifecycle()
    val isPasswordUpdating by authVm.isPasswordUpdating.collectAsStateWithLifecycle()

    InventoryScreenContent(
        navController,
        state = state,
        onSearchQueryChange = { inventoryVm.searchInventory(it) },
        onDepartmentSelected = { inventoryVm.filterByDepartment(it) },
        onCategorySelected = { inventoryVm.filterByCategory(it) },
        onStatusSelected = { inventoryVm.filterByStatus(it) },
        onRetryClick = { inventoryVm.refresh() },
        onStatusChangeConfirm = { equipment, status, note ->
            inventoryVm.changeStatus(equipment, status, note)
        },
        isPasswordUpdating = isPasswordUpdating,
        changePassword = authVm::changePassword,
        logout = authVm::logout
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreenContent(
    navController: NavHostController,
    state: InventoryUiState = InventoryUiState.Loading,
    onSearchQueryChange: (String) -> Unit = {},
    onDepartmentSelected: (Department?) -> Unit = {},
    onCategorySelected: (String?) -> Unit = {},
    onStatusSelected: (EquipmentStatus?) -> Unit = {},
    onRetryClick: () -> Unit = {},
    onStatusChangeConfirm: (Equipment, EquipmentStatus, String) -> Unit = { _, _, _ -> },
    isPasswordUpdating: Boolean = false,
    changePassword: (String, String, (Result<Unit>) -> Unit) -> Unit = { _, _, _ -> },
    logout: (() -> Unit) -> Unit = {}
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
            val context = LocalContext.current

            var selectedEquipment by remember { mutableStateOf<Equipment?>(null) }

            val filterSheetState = rememberModalBottomSheetState()
            val sheetState = rememberModalBottomSheetState()

            var showFilterSheet by remember { mutableStateOf(false) }
            var showProfileBottomSheet by remember { mutableStateOf(false) }
            var showMyProfileDialog by remember { mutableStateOf(false) }
            var showNotificationsPreferencesDialog by remember { mutableStateOf(false) }
            var showChangePasswordDialog by remember { mutableStateOf(false) }
            var showLogoutConfirmDialog by remember { mutableStateOf(false) }

            if (showProfileBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showProfileBottomSheet = false },
                    sheetState = sheetState,
                    dragHandle = { BottomSheetDefaults.DragHandle() },
                    containerColor = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.onSecondary
                    } else {
                        Color.White
                    }
                ) {
                    BioMedProfileSheet(
                        user = state.currentUser,
                        onImportEquipmentClick = { navController.navigate(Screen.ImportEquipmentSelectFile.route) },
                        onManageUsersClick = { navController.navigate(Screen.UserManagement.route) },
                        onMyProfileClick = {
                            showMyProfileDialog = true
                            showProfileBottomSheet = false
                        },
                        onNotificationsPreferencesClick = {
                            showNotificationsPreferencesDialog = true
                            showProfileBottomSheet = false
                        },
                        onChangePasswordClick = {
                            showChangePasswordDialog = true
                            showProfileBottomSheet = false
                        },
                        onLogoutClick = {
                            showLogoutConfirmDialog = true
                            showProfileBottomSheet = false
                        }
                    )
                }
            }

            if (showMyProfileDialog) {
                BioMedMyProfileDialog(
                    user = state.currentUser,
                    onDismiss = { showMyProfileDialog = false }
                )
            }

            if (showNotificationsPreferencesDialog) {
                BioMedNotificationPreferencesDialog(
                    onDismiss = { showNotificationsPreferencesDialog = false }
                )
            }

            if (showChangePasswordDialog) {
                BioMedChangePasswordDialog(
                    isLoading = isPasswordUpdating,
                    onConfirm = { current, new ->
                        changePassword(current, new) { result ->
                            when (result) {
                                is Result.Success -> {
                                    Toast.makeText(context, "Password updated successfully!", Toast.LENGTH_SHORT).show()
                                    showChangePasswordDialog = false
                                }
                                is Result.Error -> {
                                    Toast.makeText(context, result.message, Toast.LENGTH_LONG).show()
                                }
                                else -> {}
                            }
                        }
                    },
                    onDismiss = { showChangePasswordDialog = false }
                )
            }

            if (showLogoutConfirmDialog) {
                BioMedLogoutDialog(
                    onDismiss = { showLogoutConfirmDialog = false },
                    onConfirm = { logout { navController.navigate(Screen.Login.route) } }
                )
            }

            if (showFilterSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showFilterSheet = false },
                    sheetState = filterSheetState,
                    dragHandle = { BottomSheetDefaults.DragHandle() },

                ) {
                    Column(
                        modifier = Modifier
                            .padding(15.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        Text(
                            text = "Advanced Filters",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                        )

                        Text(
                            text = "Filter by status",
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                        )

                        BioMedHorizontalSelector(
                            items = listOf("All") + EquipmentStatus.entries.map { it.name },
                            selectedItem = state.selectedStatus?.name ?: "All",
                            onItemSelected = { selected ->
                                if (selected == "All") {
                                    onStatusSelected(null)
                                } else {
                                    onStatusSelected(EquipmentStatus.valueOf(selected))
                                }
                            }
                        )

                        Text(
                            text = "Filter by category",
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                        )

                        BioMedHorizontalSelector(
                            items = listOf("All") + state.categories,
                            selectedItem = state.selectedCategory ?: "All",
                            onItemSelected = { selected ->
                                if (selected == "All") {
                                    onCategorySelected(null)
                                } else {
                                    onCategorySelected(selected)
                                }
                            }
                        )

                        BioMedButton(
                            text = "Apply",
                            onClick = {
                                showFilterSheet = false
                                onStatusSelected(state.selectedStatus)
                                onCategorySelected(state.selectedCategory)
                                      },
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.End),
                            customColor = MaterialTheme.colorScheme.primary,
                            customTextColor = MaterialTheme.colorScheme.onPrimary,
                            customTextSize = 14
                        )

                        BioMedButton(
                            text = "Clear All Filters",
                            onClick = {
                                showFilterSheet = false
                                onStatusSelected(null)
                                onCategorySelected(null)
                                      },
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.End),
                            customTextSize = 14
                        )
                    }
                }
            }

            Scaffold(
                topBar = {
                    BioMedTopAppBar(
                        modifier = Modifier.padding(
                            top = 10.dp
                        ),
                        onProfileClick = { showProfileBottomSheet = true },
                        onNotificationsClick = { navController.navigate(Screen.Notifications.route) }
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
                                        .weight(3f),
                                    value = state.searchQuery,
                                    onValueChange = { onSearchQueryChange(it) }
                                )

                                BioMedButton(
                                    withIcon = true,
                                    icon = R.drawable.filter,
                                    modifier = Modifier.weight(1f),
                                    onClick = { showFilterSheet = true },
                                    text = "Filter"
                                )
                            }

                            BioMedHorizontalSelector(
                                items = listOf("All") + state.departments.map { it.name },
                                selectedItem = state.selectedDepartment?.name ?: "All",
                                onItemSelected = { selectedDepartment ->
                                    val department = if (selectedDepartment == "All") {
                                        null
                                    } else {
                                        state.departments.find { it.name.equals(selectedDepartment, ignoreCase = true) }
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
                            nextServiceDate = equipment.nextMaintenanceDate?.toDateString(),
                            onCardClick = {
                                navController.navigate(Screen.EquipmentDetail.createRoute(equipment.id))
                            },
                            onStatusClick = {
                                selectedEquipment = equipment
                            }
                        )
                    }
                }

                selectedEquipment?.let { equipment ->
                    BioMedChangeStatusDialog(
                        equipmentName = "${equipment.name} ${equipment.model}",
                        onDismiss = { selectedEquipment = null },
                        onConfirm = { newStatus, note ->
                            onStatusChangeConfirm(equipment, newStatus, note)
                            selectedEquipment = null
                        }
                    )
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
                equipment = listOf(
                    Equipment(
                        id = "1",
                        name = "Fresenius",
                        model = "4008S",
                        serialNumber = "4545885N70",
                        category = "Dialysis Machine",
                        department = Department(
                            id = "1",
                            name = "Dialysis Unit",
                            totalEquipment = 10,
                        ),
                        status = EquipmentStatus.SERVICE,
                        nextMaintenanceDate = null,
                        serviceIntervalDays = 180,
                        lastMaintenanceDate = null,
                        manufacturer = "EGMED",
                        agent = "",
                        location = "",
                        installDate = 0L,
                        createdBy = ""
                    )
                ),
                departments = emptyList(),
                categories = emptyList(),
                selectedDepartment = null,
                searchQuery = "",
                selectedCategory = null,
                selectedStatus = null,
                canAddEquipment = false,
                canDeleteEquipment = false,
                currentUser = Technician(
                    id = "1",
                    name = "Ramzy Habel",
                    role = UserRole.ADMIN,
                    email = "",
                    employeeId = "",
                    assignedDepartments = emptyList(),
                    isActive = true,
                )
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
                equipment = listOf(
                    Equipment(
                        id = "1",
                        name = "Fresenius",
                        model = "4008S",
                        serialNumber = "4545885N70",
                        category = "Dialysis Machine",
                        department = Department(
                            id = "1",
                            name = "Dialysis Unit",
                            totalEquipment = 10,
                        ),
                        status = EquipmentStatus.SERVICE,
                        nextMaintenanceDate = null,
                        serviceIntervalDays = 180,
                        lastMaintenanceDate = null,
                        manufacturer = "EGMED",
                        agent = "",
                        location = "",
                        installDate = 0L,
                        createdBy = ""
                    )
                ),
                departments = emptyList(),
                categories = emptyList(),
                selectedDepartment = null,
                searchQuery = "",
                selectedCategory = null,
                selectedStatus = null,
                canAddEquipment = false,
                canDeleteEquipment = false,
                currentUser = Technician(
                    id = "1",
                    name = "Ramzy Habel",
                    role = UserRole.ADMIN,
                    email = "",
                    employeeId = "",
                    assignedDepartments = emptyList(),
                    isActive = true,
                )
            )
        )
    }
}