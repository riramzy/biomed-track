package com.riramzy.biomedtrack.ui.screens.equipment.details

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Equipment
import com.riramzy.biomedtrack.domain.model.MaintenanceLog
import com.riramzy.biomedtrack.domain.model.StatusChangeLog
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.components.custom.BioMedNavBar
import com.riramzy.biomedtrack.ui.components.custom.BioMedTopAppBar
import com.riramzy.biomedtrack.ui.components.equipment.BioMedChangeStatusDialog
import com.riramzy.biomedtrack.ui.components.equipment.BioMedEquipmentDetailsCard
import com.riramzy.biomedtrack.ui.components.equipment.BioMedEquipmentStatusCard
import com.riramzy.biomedtrack.ui.components.maintenance.BioMedMaintenanceCard
import com.riramzy.biomedtrack.ui.components.user.BioMedChangePasswordDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedLogoutDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedMyProfileDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedNotificationPreferencesDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedProfileSheet
import com.riramzy.biomedtrack.ui.screens.auth.AuthVm
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.ui.theme.indicatorColors
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.utils.MaintenanceType
import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.utils.Screen
import com.riramzy.biomedtrack.utils.UserRole

@Composable
fun EquipmentDetailScreen(
    navController: NavHostController,
    equipmentDetailsVm: EquipmentDetailsVm = hiltViewModel(),
    authVm: AuthVm = hiltViewModel()
) {
    val state by equipmentDetailsVm.uiState.collectAsStateWithLifecycle()
    val deleteResult by equipmentDetailsVm.deleteResult.collectAsStateWithLifecycle()
    val isPasswordUpdating by authVm.isPasswordUpdating.collectAsStateWithLifecycle()

    EquipmentDetailsScreenContent(
        navController = navController,
        state = state,
        onDeleteClicked = equipmentDetailsVm::deleteEquipment,
        resetDeleteResult = equipmentDetailsVm::resetDeleteResult,
        deleteResult = deleteResult,
        changeStatus = equipmentDetailsVm::changeEquipmentStatus,
        isPasswordUpdating = isPasswordUpdating,
        changePassword = authVm::changePassword,
        logout = authVm::logout
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentDetailsScreenContent(
    navController: NavHostController,
    state: EquipmentDetailsUiState,
    onRetryClick: () -> Unit = {},
    onDeleteClicked: () -> Unit = {},
    resetDeleteResult: () -> Unit = {},
    deleteResult: Result<Unit>? = null,
    changeStatus: (Equipment, EquipmentStatus, String?) -> Unit = { _, _, _ -> },
    isPasswordUpdating: Boolean = false,
    changePassword: (String, String, (Result<Unit>) -> Unit) -> Unit = { _, _, _ -> },
    logout: (() -> Unit) -> Unit = {}
) {
    when(state) {
        is EquipmentDetailsUiState.Error -> {
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
        is EquipmentDetailsUiState.Loading -> {
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
                EquipmentDetailsShimmer(modifier = Modifier.padding(innerPadding))
            }
        }
        is EquipmentDetailsUiState.Success -> {
            val sheetState = rememberModalBottomSheetState()
            val context = LocalContext.current

            var showDeleteDialog by remember { mutableStateOf(false) }
            var showChangeStatusDialog by remember { mutableStateOf(false) }
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

            if (showChangeStatusDialog) {
                BioMedChangeStatusDialog(
                    equipmentName = "${state.equipment.name} ${state.equipment.model}",
                    onConfirm = { status, note ->
                        changeStatus(state.equipment, status, note.ifBlank { null })
                        showChangeStatusDialog = false
                    },
                    onDismiss = { showChangeStatusDialog = false }
                )
            }

            if (showDeleteDialog) {
                AlertDialog(
                    onDismissRequest = { showDeleteDialog = false },
                    text = { Text("Are you sure you want to delete this equipment? This procedure cannot be undone") },
                    title = { Text("Delete") },
                    confirmButton = {
                        BioMedButton(
                            text = "Delete",
                            customColor = MaterialTheme.indicatorColors.red,
                            customTextColor = Color.White,
                            onClick = {
                                onDeleteClicked()
                                showDeleteDialog = false
                            }
                        )
                    },
                    dismissButton = {
                        BioMedButton(
                            text = "Cancel",
                            onClick = { showDeleteDialog= false }
                        )
                    }
                )
            }

            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect(deleteResult) {
                deleteResult?.let { result ->
                    when(result) {
                        is Result.Success -> {
                            snackbarHostState.showSnackbar(
                                "Equipment deleted successfully",
                                duration = SnackbarDuration.Short
                            )
                            navController.popBackStack(Screen.Inventory.route, false)
                        }
                        is Result.Error -> {
                            snackbarHostState.showSnackbar(
                                "Failed to delete: ${result.message}",
                                duration = SnackbarDuration.Short
                            )
                        }
                        else -> {}
                    }
                    resetDeleteResult()
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
                        withActionButton = true,
                        isActionButtonText = true,
                        actionButtonText = "Log",
                        onActionButtonClick = { navController.navigate(Screen.LogMaintenance.createRoute(state.equipment.id)) },
                        onDashboardClick = { navController.navigate(Screen.Dashboard.route) },
                        onSchedulerClick = { navController.navigate(Screen.Scheduler.route) },
                        onInventoryClick = { navController.navigate(Screen.Inventory.route) },
                        onReportsClick = { navController.navigate(Screen.Reports.route) },
                        modifier = Modifier.padding(horizontal = 15.dp)
                    )
                                       },
                floatingActionButtonPosition = FabPosition.Center,
                snackbarHost = { SnackbarHost(snackbarHostState) },
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
                        BioMedEquipmentDetailsCard(
                            modifier = Modifier
                                .statusBarsPadding()
                                .padding(
                                    top = 20.dp,
                                    bottom = 15.dp
                                ),
                            equipment = state.equipment,
                            canEditEquipment = state.canEditEquipment,
                            canDeleteEquipment = state.canDeleteEquipment,
                            onEditClick = { navController.navigate(Screen.EditEquipment.createRoute(state.equipment.id)) },
                            onDeleteClick = {
                                showDeleteDialog = true
                            },
                            canChangeStatus = state.canChangeStatus,
                            onStatusClick = {
                                showChangeStatusDialog = true
                            }
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

                            state.maintenanceLogs.forEach { log ->
                                BioMedMaintenanceCard(
                                    modifier = Modifier.padding(bottom = 10.dp),
                                    log = log
                                )
                            }
                        }
                    }

                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Status Changes History",
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

                            state.statusChangesLogs.forEach { log ->
                                BioMedEquipmentStatusCard(
                                    modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 10.dp),
                                    equipmentStatus = log.newStatus,
                                    equipmentName = log.equipmentName,
                                    equipmentModel = log.equipmentModel,
                                    equipmentSerialNumber = log.equipmentSerial,
                                    equipmentCategory = log.department.name,
                                    equipmentDepartment = log.department,
                                    equipmentLastServiceDate = log.timestamp,
                                    isAbbreviated = true,
                                    notes = log.notes
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}

@Preview(device = "id:pixel_9", showBackground = true)
@Composable
fun EquipmentDetailsScreenPreview() {
    BioMedTheme {
        EquipmentDetailsScreenContent(
            navController = NavHostController(LocalContext.current),
            state = EquipmentDetailsUiState.Success(
                equipment = Equipment(
                    id = "1",
                    name = "Equipment 1",
                    model = "Model 1",
                    serialNumber = "123456789",
                    category = "Category 1",
                    department = Department(
                        id = "1",
                        name = "Department 1",
                        totalEquipment = 20
                    ),
                    status = EquipmentStatus.SERVICE,
                    nextMaintenanceDate = 1/1/2026,
                    lastMaintenanceDate = 1/1/2026,
                    manufacturer = "Manufacturer 1",
                    agent = "Agent 1",
                    installDate = 1/1/2016,
                    location = "Dialysis Unit",
                    serviceIntervalDays = 90,
                    createdBy = "Ramzy Habel"
                ),
                maintenanceLogs = listOf(
                    MaintenanceLog(
                        id = "1",
                        equipmentId = "1",
                        equipmentName = "Equipment 1",
                        equipmentModel = "Model",
                        equipmentSerial = "123456789",
                        department = Department(
                            id = "1",
                            name = "Department 1",
                            totalEquipment = 20
                        ),
                        type = MaintenanceType.REPAIR,
                        technicianId = "1",
                        technicianName = "Technician 1",
                        date = 14/4/2026,
                        currentStatus = EquipmentStatus.SERVICE,
                        checklist = emptyList(),
                        notes = "Replaced faulty parts with new ones",
                        workDone = "Replaced faulty parts with new ones"
                    )
                ),
                statusChangesLogs = listOf(
                    StatusChangeLog(
                        id = "1",
                        equipmentId = "1",
                        equipmentName = "Equipment 1",
                        equipmentModel = "Model",
                        equipmentSerial = "123456789",
                        department = Department(
                            id = "1",
                            name = "Department 1",
                            totalEquipment = 20
                        ),
                        previousStatus = EquipmentStatus.ONLINE,
                        newStatus = EquipmentStatus.SERVICE,
                        timestamp = 14/4/2026,
                        notes = "Replaced faulty parts with new ones",
                        changedBy = "Supervisor",
                        changedByName = "Ramzy Habel",
                    )
                ),
                canEditEquipment = true,
                canDeleteEquipment = true,
                canChangeStatus = true,
                currentUser = Technician(
                    id = "1",
                    name = "Ramzy Habel",
                    role = UserRole.SUPERVISOR,
                    email = "",
                    employeeId = "",
                    assignedDepartments = emptyList(),
                    isActive = true,
                )
            )
        )
    }
}

@Preview(device = "id:pixel_9", showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun EquipmentDetailsScreenDarkPreview() {
    BioMedTheme {
        EquipmentDetailsScreenContent(
            navController = NavHostController(LocalContext.current),
            state = EquipmentDetailsUiState.Success(
                equipment = Equipment(
                    id = "1",
                    name = "Equipment 1",
                    model = "Model 1",
                    serialNumber = "123456789",
                    category = "Category 1",
                    department = Department(
                        id = "1",
                        name = "Department 1",
                        totalEquipment = 20
                    ),
                    status = EquipmentStatus.SERVICE,
                    nextMaintenanceDate = 1/1/2026,
                    lastMaintenanceDate = 1/1/2026,
                    manufacturer = "Manufacturer 1",
                    agent = "Agent 1",
                    installDate = 1/1/2016,
                    location = "Dialysis Unit",
                    serviceIntervalDays = 90,
                    createdBy = "Ramzy Habel"
                ),
                maintenanceLogs = listOf(
                    MaintenanceLog(
                        id = "1",
                        equipmentId = "1",
                        equipmentName = "Equipment 1",
                        equipmentModel = "Model",
                        equipmentSerial = "123456789",
                        department = Department(
                            id = "1",
                            name = "Department 1",
                            totalEquipment = 20
                        ),
                        type = MaintenanceType.REPAIR,
                        technicianId = "1",
                        technicianName = "Technician 1",
                        date = 14/4/2026,
                        currentStatus = EquipmentStatus.SERVICE,
                        checklist = emptyList(),
                        notes = "Replaced faulty parts with new ones",
                        workDone = "Replaced faulty parts with new ones"
                    )
                ),
                statusChangesLogs = listOf(
                    StatusChangeLog(
                        id = "1",
                        equipmentId = "1",
                        equipmentName = "Equipment 1",
                        equipmentModel = "Model",
                        equipmentSerial = "123456789",
                        department = Department(
                            id = "1",
                            name = "Department 1",
                            totalEquipment = 20
                        ),
                        previousStatus = EquipmentStatus.ONLINE,
                        newStatus = EquipmentStatus.SERVICE,
                        timestamp = 14/4/2026,
                        notes = "Replaced faulty parts with new ones",
                        changedBy = "Supervisor",
                        changedByName = "Ramzy Habel"
                    )
                ),
                canEditEquipment = true,
                canDeleteEquipment = true,
                canChangeStatus = true,
                currentUser = Technician(
                    id = "1",
                    name = "Ramzy Habel",
                    role = UserRole.SUPERVISOR,
                    email = "",
                    employeeId = "",
                    assignedDepartments = emptyList(),
                    isActive = true,
                )
            ),
        )
    }
}