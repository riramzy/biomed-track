package com.riramzy.biomedtrack.ui.screens.dashboard

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.key
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
import com.riramzy.biomedtrack.domain.model.Task
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.ui.components.BioMedActivityCard
import com.riramzy.biomedtrack.ui.components.BioMedInsightCard
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.components.custom.BioMedNavBar
import com.riramzy.biomedtrack.ui.components.custom.BioMedTopAppBar
import com.riramzy.biomedtrack.ui.components.department.BioMedDepartmentInsightCard
import com.riramzy.biomedtrack.ui.components.schedule.BioMedTaskCard
import com.riramzy.biomedtrack.ui.components.user.BioMedChangePasswordDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedLogoutDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedMyProfileDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedNotificationPreferencesDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedProfileSheet
import com.riramzy.biomedtrack.ui.components.user.BioMedUserOverview
import com.riramzy.biomedtrack.ui.screens.auth.AuthVm
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.ActivityItem
import com.riramzy.biomedtrack.utils.ActivityType
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.utils.Screen
import com.riramzy.biomedtrack.utils.TaskStatus
import com.riramzy.biomedtrack.utils.Timestamps.toRelativeTime
import com.riramzy.biomedtrack.utils.UserRole

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavHostController,
    dashboardVm: DashboardVm = hiltViewModel(),
    authVm: AuthVm = hiltViewModel()
) {
    val state by dashboardVm.uiState.collectAsStateWithLifecycle()
    val isPasswordUpdating by authVm.isPasswordUpdating.collectAsStateWithLifecycle()

    DashboardScreenContent(
        navController = navController,
        state = state,
        onRetryClick = dashboardVm::refresh,
        isPasswordUpdating = isPasswordUpdating,
        changePassword = authVm::changePassword,
        logout = authVm::logout
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreenContent(
    navController: NavHostController,
    state: DashboardUiState = DashboardUiState.Loading,
    onRetryClick: () -> Unit = {},
    isPasswordUpdating: Boolean = false,
    changePassword: (String, String, (Result<Unit>) -> Unit) -> Unit = { _, _, _ -> },
    logout: (() -> Unit) -> Unit = {}
) {
    when(state) {
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
            val context = LocalContext.current

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
                        BioMedUserOverview(
                            username = state.currentUser.name,
                            role = state.currentUser.role.name,
                            modifier = Modifier.padding(
                                vertical = 20.dp,
                                horizontal = 15.dp
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
                                modifier = Modifier.padding(horizontal = 15.dp)
                            ) {
                                BioMedInsightCard(
                                    value = state.stats.total.toString(),
                                    status = "",
                                    modifier = Modifier.weight(1f)
                                )

                                BioMedInsightCard(
                                    value = state.stats.online.toString(),
                                    status = "Online",
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 15.dp)
                            ) {
                                BioMedInsightCard(
                                    value = state.stats.dueService.toString(),
                                    status = "Service",
                                    modifier = Modifier.weight(1f)
                                )

                                BioMedInsightCard(
                                    value = state.stats.down.toString(),
                                    status = "Down",
                                    modifier = Modifier.weight(1f)
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

                            state.recentActivities.forEach { activity ->
                                key(activity.id) {
                                    BioMedActivityCard(
                                        type = activity.type,
                                        status = activity.equipmentStatus ?: EquipmentStatus.SERVICE,
                                        title = activity.title,
                                        name = activity.equipmentName,
                                        model = activity.equipmentModel,
                                        serialNumber = activity.equipmentSerial,
                                        department = activity.departmentName,
                                        changedBy = activity.technicianName,
                                        relativeTime = activity.timestamp.toRelativeTime(),
                                        dateString = activity.dueDate ?: "",
                                        modifier = Modifier.padding(bottom = 10.dp)
                                    )
                                }
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

                            state.upcomingMaintenance.forEach { upcomingMaintenance ->
                                BioMedTaskCard(
                                    task = upcomingMaintenance,
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

                            val departmentsToShow = if (
                                state.currentUser.role == UserRole.ADMIN ||
                                state.currentUser.role == UserRole.SUPERVISOR
                            ) {
                                state.allDepartments
                            } else {
                                state.currentUser.assignedDepartments
                            }

                            departmentsToShow.forEach { department ->
                                BioMedDepartmentInsightCard(
                                    department = department.name,
                                    departmentTotalEquipment = department.totalEquipment.toString(),
                                    departmentDueService = department.dueServiceEquipment.toString(),
                                    departmentDown = department.downEquipment.toString(),
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
            navController = NavHostController(LocalContext.current),
            state = DashboardUiState.Success(
                currentUser = Technician(
                    id = "1",
                    name = "Khaled",
                    email = "william.henry.harrison@example-pet-store.com",
                    role = UserRole.ADMIN,
                    assignedDepartments = emptyList(),
                    employeeId = "1",
                    isActive = true,
                ),
                stats = DashboardStats(
                    total = 50,
                    online = 44,
                    down = 10,
                    dueService = 2,
                ),
                recentActivities = listOf(
                    ActivityItem(
                        id = "1",
                        type = ActivityType.STATUS_CHANGE,
                        title = "Status Change to Service",
                        equipmentId = "",
                        equipmentModel = "4008S",
                        equipmentSerial = "3535353",
                        equipmentName = "Fresenius",
                        equipmentStatus = EquipmentStatus.SERVICE,
                        departmentName = "Dialysis Unit",
                        technicianName = "Mark Milad",
                        timestamp = 12/3/2026L,
                        isRead = false
                    )
                ),
                upcomingMaintenance = listOf(
                    Task(
                        id = "",
                        equipmentId = "",
                        equipmentName = "",
                        equipmentModel = "",
                        equipmentSerial = "",
                        department = Department(
                            totalEquipment = 14,
                            downEquipment = 2,
                            dueServiceEquipment = 3,
                            id = "",
                            name = "Dilaysis Unit"
                        ),
                        dueDate = 0L,
                        status = TaskStatus.IN_PROGRESS,
                        assignedTo = "",
                        assignedToName = "",
                        assignedBy = "",
                        notes = "",
                        scheduledChecklist = emptyList()
                    )
                ),
                allDepartments = listOf(
                    Department(
                        totalEquipment = 14,
                        downEquipment = 2,
                        dueServiceEquipment = 3,
                        id = "",
                        name = "Dilaysis Unit"
                    )
                )
            )
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
            navController = NavHostController(LocalContext.current),
            state = DashboardUiState.Success(
                currentUser = Technician(
                    id = "1",
                    name = "Khaled",
                    email = "william.henry.harrison@example-pet-store.com",
                    role = UserRole.ADMIN,
                    assignedDepartments = emptyList(),
                    employeeId = "1",
                    isActive = true,
                ),
                stats = DashboardStats(
                    total = 50,
                    online = 44,
                    down = 10,
                    dueService = 2,
                ),
                recentActivities = listOf(
                    ActivityItem(
                        id = "1",
                        type = ActivityType.STATUS_CHANGE,
                        title = "Status Change to Service",
                        equipmentId = "",
                        equipmentModel = "4008S",
                        equipmentSerial = "3535353",
                        equipmentName = "Fresenius",
                        equipmentStatus = EquipmentStatus.SERVICE,
                        departmentName = "Dialysis Unit",
                        technicianName = "Mark Milad",
                        timestamp = 12/3/2026L,
                        isRead = false
                    )
                ),
                upcomingMaintenance = listOf(
                    Task(
                        id = "",
                        equipmentId = "",
                        equipmentName = "",
                        equipmentModel = "",
                        equipmentSerial = "",
                        department = Department(
                            totalEquipment = 14,
                            downEquipment = 2,
                            dueServiceEquipment = 3,
                            id = "",
                            name = "Dilaysis Unit"
                        ),
                        dueDate = 0L,
                        status = TaskStatus.IN_PROGRESS,
                        assignedTo = "",
                        assignedToName = "",
                        assignedBy = "",
                        notes = "",
                        scheduledChecklist = emptyList()
                    )
                ),
                allDepartments = listOf(
                    Department(
                        totalEquipment = 14,
                        downEquipment = 2,
                        dueServiceEquipment = 3,
                        id = "",
                        name = "Dilaysis Unit"
                    )
                )
            )
        )
    }
}