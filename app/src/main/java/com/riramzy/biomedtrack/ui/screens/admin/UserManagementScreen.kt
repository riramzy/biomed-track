package com.riramzy.biomedtrack.ui.screens.admin

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.ui.components.custom.BioMedHorizontalSelector
import com.riramzy.biomedtrack.ui.components.custom.BioMedNavBar
import com.riramzy.biomedtrack.ui.components.custom.BioMedSearchBar
import com.riramzy.biomedtrack.ui.components.custom.BioMedSnackbar
import com.riramzy.biomedtrack.ui.components.custom.BioMedTopAppBar
import com.riramzy.biomedtrack.ui.components.department.BioMedManageDepartmentsSheet
import com.riramzy.biomedtrack.ui.components.user.BioMedAddNewUserSheet
import com.riramzy.biomedtrack.ui.components.user.BioMedChangePasswordDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedChangeRoleSheet
import com.riramzy.biomedtrack.ui.components.user.BioMedLogoutDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedMyProfileDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedNotificationPreferencesDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedProfileSheet
import com.riramzy.biomedtrack.ui.components.user.BioMedUserInfoCard
import com.riramzy.biomedtrack.ui.components.user.BioMedUsersInsightCard
import com.riramzy.biomedtrack.ui.screens.auth.AuthVm
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.utils.Screen
import com.riramzy.biomedtrack.utils.UserRole
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun UserManagementScreen(
    navController: NavHostController,
    userManagementVm: UserManagementVm = hiltViewModel(),
    authVm: AuthVm = hiltViewModel()
) {
    val state by userManagementVm.uiState.collectAsStateWithLifecycle()
    val currentUser by userManagementVm.currentUser.collectAsStateWithLifecycle()
    val searchQuery by userManagementVm.searchQuery.collectAsStateWithLifecycle()
    val selectedRoleIndex by userManagementVm.selectedRoleIndex.collectAsStateWithLifecycle()
    val allDepartments by userManagementVm.allDepartments.collectAsStateWithLifecycle()
    val snackbarMessage by userManagementVm.snackbarMessage.collectAsStateWithLifecycle()
    val isSnackbarMessageAnError by userManagementVm.isSnackbarMessageError.collectAsStateWithLifecycle()
    val userCreatedEvent = userManagementVm.userCreatedEvent
    val isPasswordUpdating by authVm.isPasswordUpdating.collectAsStateWithLifecycle()

    UserManagementScreenContent(
        navController = navController,
        state = state,
        currentUser = currentUser!!,
        searchQuery = searchQuery,
        selectedRoleIndex = selectedRoleIndex,
        allDepartments = allDepartments,
        onSearchQueryChange = userManagementVm::setSearchQuery,
        onSelectedRoleChange = userManagementVm::setSelectedRole,
        toggleUserActiveStatus = userManagementVm::toggleUserActiveStatus,
        changeUserRole = userManagementVm::changeUserRole,
        updateUserDepartments = userManagementVm::updateUserDepartments,
        createUser = userManagementVm::createUser,
        snackbarMessage = snackbarMessage,
        isSnackbarMessageAnError = isSnackbarMessageAnError,
        onSnackbarMessageConsumed = userManagementVm::clearSnackbarMessage,
        userCreatedEvent = userCreatedEvent,
        isPasswordUpdating = isPasswordUpdating,
        changePassword = authVm::changePassword,
        logout = authVm::logout
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagementScreenContent(
    navController: NavHostController,
    state: UserManagementUiState = UserManagementUiState.Loading,
    currentUser: Technician,
    searchQuery: String = "",
    selectedRoleIndex: Int = 0,
    allDepartments: List<Department> = emptyList(),
    onSearchQueryChange: (String) -> Unit = {},
    onSelectedRoleChange: (Int) -> Unit = {},
    toggleUserActiveStatus: (Technician) -> Unit = {},
    changeUserRole: (String, UserRole) -> Unit = { _, _ -> },
    updateUserDepartments: (String, List<Department>) -> Unit = { _, _ -> },
    createUser: (Technician, String) -> Unit = { _, _ -> },
    snackbarMessage: String? = null,
    isSnackbarMessageAnError: Boolean = false,
    onSnackbarMessageConsumed: () -> Unit = {},
    userCreatedEvent: SharedFlow<Unit> = MutableSharedFlow(),
    isPasswordUpdating: Boolean = false,
    changePassword: (String, String, (Result<Unit>) -> Unit) -> Unit = { _, _, _ -> },
    logout: (() -> Unit) -> Unit = {}
) {
    var showManageDepartmentsSheet by remember { mutableStateOf(false) }
    var showChangeRoleSheet by remember { mutableStateOf(false) }
    var showAddNewUserSheet by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<Technician?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

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
                user = currentUser,
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
            user = currentUser,
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
                            Toast.makeText(
                                context,
                                R.string.password_updated_success,
                                Toast.LENGTH_SHORT
                            ).show()
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

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it, withDismissAction = true)
            onSnackbarMessageConsumed()
        }
    }

    LaunchedEffect(Unit) {
        userCreatedEvent.collect {
            showAddNewUserSheet = false
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
                selectedPage = "",
                isActionButtonText = false,
                withActionButton = true,
                actionButtonIcon = R.drawable.add,
                onActionButtonClick = { showAddNewUserSheet = true },
                onDashboardClick = { navController.navigate(Screen.Dashboard.route) },
                onSchedulerClick = { navController.navigate(Screen.Scheduler.route) },
                onInventoryClick = { navController.navigate(Screen.Inventory.route) },
                onReportsClick = { navController.navigate(Screen.Reports.route) }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                BioMedSnackbar(
                    snackbarData = data,
                    isError = isSnackbarMessageAnError,
                    modifier = Modifier.padding(15.dp)
                )
            }
                       },
        modifier = Modifier.statusBarsPadding()
    ) { innerPadding ->
        if (showManageDepartmentsSheet && selectedUser != null) {
            ModalBottomSheet(
                onDismissRequest = { showManageDepartmentsSheet = false },
                dragHandle = { BottomSheetDefaults.DragHandle() },
                containerColor = if (isSystemInDarkTheme()) {
                    MaterialTheme.colorScheme.onSecondary
                } else {
                    Color.White
                }
            ) {
                BioMedManageDepartmentsSheet(
                    user = selectedUser!!,
                    departments = allDepartments,
                    onSave = { departments ->
                        updateUserDepartments(selectedUser!!.id, departments)
                        showManageDepartmentsSheet = false
                    },
                    onCancel = { showManageDepartmentsSheet = false }
                )
            }
        }

        if (showChangeRoleSheet && selectedUser != null) {
            ModalBottomSheet(
                onDismissRequest = { showChangeRoleSheet = false },
                dragHandle = { BottomSheetDefaults.DragHandle() },
                containerColor = if (isSystemInDarkTheme()) {
                    MaterialTheme.colorScheme.onSecondary
                } else {
                    Color.White
                }
            ) {
                BioMedChangeRoleSheet(
                    user = selectedUser!!,
                    onSave = { newRole ->
                        changeUserRole(selectedUser!!.id, newRole)
                        showChangeRoleSheet = false
                    },
                    onCancel = { showChangeRoleSheet = false }
                )
            }
        }

        if (showAddNewUserSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAddNewUserSheet = false },
                dragHandle = { BottomSheetDefaults.DragHandle() },
                containerColor = if (isSystemInDarkTheme()) {
                    MaterialTheme.colorScheme.onSecondary
                } else {
                    Color.White
                }
            ) {
                BioMedAddNewUserSheet(
                    onConfirm = { user, password ->
                        createUser(user, password)
                    },
                    onCancel = { showAddNewUserSheet = false }
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                bottom = innerPadding.calculateBottomPadding() + 100.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (state) {
                is UserManagementUiState.Success -> {
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
                                text = stringResource(R.string.user_mgmt_title),
                                style = MaterialTheme.typography.titleLarge,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                            )

                            Text(
                                text = stringResource(R.string.user_mgmt_subtitle),
                                style = MaterialTheme.typography.labelLarge,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    bottom = 15.dp,
                                    start = 15.dp,
                                    end = 15.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            BioMedUsersInsightCard(
                                numberOfUsers = state.adminCount,
                                usersRole = stringResource(R.string.user_mgmt_role_admins),
                                modifier = Modifier.weight(1f)
                            )
                            BioMedUsersInsightCard(
                                numberOfUsers = state.supervisorCount,
                                usersRole = stringResource(R.string.user_mgmt_role_supervisors),
                                modifier = Modifier.weight(1f)
                            )
                            BioMedUsersInsightCard(
                                numberOfUsers = state.technicianCount,
                                usersRole = stringResource(R.string.user_mgmt_role_technicians),
                                modifier = Modifier.weight(1f)
                            )
                            BioMedUsersInsightCard(
                                numberOfUsers = state.totalUsersCount,
                                usersRole = stringResource(R.string.insight_card_total),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    item {
                        BioMedSearchBar(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    bottom = 15.dp,
                                    start = 15.dp,
                                    end = 15.dp
                                ),
                            value = searchQuery,
                            onValueChange = { onSearchQueryChange(it) }
                        )
                    }

                    item {
                        val roleLabels = listOf(
                            stringResource(R.string.user_mgmt_role_all),
                            stringResource(R.string.user_mgmt_role_admins),
                            stringResource(R.string.user_mgmt_role_supervisors),
                            stringResource(R.string.user_mgmt_role_technicians)
                        )

                        BioMedHorizontalSelector(
                            items = roleLabels,
                            selectedItem = roleLabels[selectedRoleIndex],
                            onItemSelected = { label ->
                                val index = roleLabels.indexOf(label)
                                onSelectedRoleChange(index)
                            },
                            modifier = Modifier
                                .padding(
                                    bottom = 15.dp,
                                    start = 15.dp,
                                    end = 15.dp
                                )
                        )
                    }

                    state.users.forEach { user ->
                        item {
                            BioMedUserInfoCard(
                                modifier = Modifier.padding(
                                    bottom = 15.dp,
                                    start = 15.dp,
                                    end = 15.dp
                                ),
                                user = user,

                                isUserActive = user.isActive,
                                onActiveToggle = { toggleUserActiveStatus(user) },
                                onManageDepartmentsClick = { showManageDepartmentsSheet = true; selectedUser = user },
                                onMoreClick = { showChangeRoleSheet = true; selectedUser = user }
                            )
                        }
                    }
                }
                is UserManagementUiState.Error -> {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        MaterialTheme.colorScheme.errorContainer,
                                        shape = MaterialTheme.shapes.large
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = state.message,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.error,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(15.dp)
                                )
                            }
                        }
                    }
                }
                is UserManagementUiState.Loading -> {
                    item {
                        UserManagementShimmer()
                    }
                }
            }
        }
    }
}

@Preview(device = "id:pixel_9", locale = "ar")
@Composable
fun UserManagementScreenContentPreview() {
    BioMedTheme {
        UserManagementScreenContent(
            navController = NavHostController(LocalContext.current),
            state = UserManagementUiState.Success(
                users = listOf(
                    Technician(
                        id = "1",
                        name = "Khaled",
                        email = "john.c.calhoun@examplepetstore.com",
                        role = UserRole.ADMIN,
                        assignedDepartments = emptyList(),
                        employeeId = "1",
                        isActive = true,
                    )
                ),
                adminCount = 0,
                supervisorCount = 0,
                technicianCount = 0,
                totalUsersCount = 0
            ),
            currentUser = Technician(
                id = "1",
                name = "Khaled",
                email = "john.c.calhoun@examplepetstore.com",
                role = UserRole.ADMIN,
                assignedDepartments = emptyList(),
                employeeId = "1",
                isActive = true,
            )
        )
    }
}

@Preview(device = "id:pixel_9",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun UserManagementScreenContentDarkPreview() {
    BioMedTheme {
        UserManagementScreenContent(
            navController = NavHostController(LocalContext.current),
            state = UserManagementUiState.Success(
                users = listOf(
                    Technician(
                        id = "1",
                        name = "Khaled",
                        email = "john.c.calhoun@examplepetstore.com",
                        role = UserRole.ADMIN,
                        assignedDepartments = emptyList(),
                        employeeId = "1",
                        isActive = true,
                    )
                ),
                adminCount = 0,
                supervisorCount = 0,
                technicianCount = 0,
                totalUsersCount = 0
            ),
            currentUser = Technician(
                id = "1",
                name = "Khaled",
                email = "john.c.calhoun@examplepetstore.com",
                role = UserRole.ADMIN,
                assignedDepartments = emptyList(),
                employeeId = "1",
                isActive = true,
            )
        )
    }
}