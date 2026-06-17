package com.riramzy.biomedtrack.ui.screens.importing

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.navigation.compose.rememberNavController
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.ui.components.custom.BioMedNavBar
import com.riramzy.biomedtrack.ui.components.custom.BioMedProgressIndicator
import com.riramzy.biomedtrack.ui.components.custom.BioMedTopAppBar
import com.riramzy.biomedtrack.ui.components.importing.BioMedImportingProgressCard
import com.riramzy.biomedtrack.ui.components.importing.ImportLog
import com.riramzy.biomedtrack.ui.components.importing.LogType
import com.riramzy.biomedtrack.ui.components.user.BioMedChangePasswordDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedLogoutDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedMyProfileDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedNotificationPreferencesDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedProfileSheet
import com.riramzy.biomedtrack.ui.screens.auth.AuthVm
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.utils.Screen
import com.riramzy.biomedtrack.utils.UserRole

@Composable
fun ImportEquipmentProgressScreen(
    navController: NavHostController,
    authVm: AuthVm = hiltViewModel()
) {
    val parentEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry(Screen.ImportEquipmentSelectFile.route)
    }

    val importingEquipmentVm: ImportingEquipmentVm = hiltViewModel(parentEntry)
    val state by importingEquipmentVm.uiState.collectAsStateWithLifecycle()
    val currentUser by importingEquipmentVm.currentUser.collectAsStateWithLifecycle()
    val isPasswordUpdating by authVm.isPasswordUpdating.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        if (state is ImportingUiState.Success) {
            navController.navigate(Screen.ImportEquipmentSuccess.route) {
                popUpTo(Screen.ImportEquipmentSelectFile.route) {
                    inclusive = false
                }
            }
        }
    }

    val importingState = state as? ImportingUiState.Importing
    val progress = importingState?.progress ?: 0f
    val liveLogs = importingState?.liveLogs ?: emptyList()


    ImportEquipmentProgressScreenContent(
        navController = navController,
        currentUser = currentUser!!,
        progress = progress,
        liveLogs = liveLogs,
        isPasswordUpdating = isPasswordUpdating,
        changePassword = authVm::changePassword,
        logout = authVm::logout
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportEquipmentProgressScreenContent(
    navController: NavHostController = rememberNavController(),
    currentUser: Technician,
    progress: Float = 0f,
    liveLogs: List<ImportLog> = emptyList(),
    isPasswordUpdating: Boolean = false,
    changePassword: (String, String, (Result<Unit>) -> Unit) -> Unit = { _, _, _ -> },
    logout: (() -> Unit) -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current

    var showProfileBottomSheet by remember { mutableStateOf(false) }
    var showMyProfileDialog by remember { mutableStateOf(false) }
    var showNotificationsPreferencesDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showLogoutConfirmDialog by remember { mutableStateOf(false) }

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
                selectedPage = "None",
                withActionButton = false,
                onDashboardClick = { navController.navigate(Screen.Dashboard.route) },
                onSchedulerClick = { navController.navigate(Screen.Scheduler.route) },
                onInventoryClick = { navController.navigate(Screen.Inventory.route) },
                onReportsClick = { navController.navigate(Screen.Reports.route) },
                modifier = Modifier.padding(horizontal = 15.dp)
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        modifier = Modifier.statusBarsPadding()
    ) { innerPadding ->
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
                        text = stringResource(R.string.import_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )

                    Text(
                        text = stringResource(R.string.import_subtitle),
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            item {
                val stepsLabels = listOf(
                    stringResource(R.string.import_step_select_file),
                    stringResource(R.string.import_step_preview),
                    stringResource(R.string.import_step_import)
                )

                BioMedProgressIndicator(
                    steps = stepsLabels,
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
                        text = stringResource(R.string.import_step3_header),
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
                    progress = progress,
                    logs = liveLogs,
                    modifier = Modifier.padding(horizontal = 15.dp)
                )
            }
        }
    }
}

@Preview(device = "id:pixel_9", locale = "ar")
@Composable
fun ImportEquipmentProgressScreenPreview() {
    BioMedTheme {
        ImportEquipmentProgressScreenContent(
            liveLogs = listOf(
                ImportLog("Imported Fresenius 4008S - 4545885N70", LogType.SUCCESS),
                ImportLog("4 rows skipped successfully", LogType.WARNING),
                ImportLog("Failed to import 2 rows", LogType.ERROR)
            ),
            currentUser = Technician(
                id = "1",
                name = "Khaled",
                email = "william.henry.harrison@example-pet-store.com",
                role = UserRole.ADMIN,
                assignedDepartments = emptyList(),
                employeeId = "1",
                isActive = true,
            )
        )
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL, locale = "ar"
)
@Composable
fun ImportEquipmentProgressScreenDarkPreview() {
    BioMedTheme {
        ImportEquipmentProgressScreenContent(
            liveLogs = listOf(
                ImportLog("Imported Fresenius 4008S - 4545885N70", LogType.SUCCESS),
                ImportLog("4 rows skipped successfully", LogType.WARNING),
                ImportLog("Failed to import 2 rows", LogType.ERROR)
            ),
            currentUser = Technician(
                id = "1",
                name = "Khaled",
                email = "william.henry.harrison@example-pet-store.com",
                role = UserRole.ADMIN,
                assignedDepartments = emptyList(),
                employeeId = "1",
                isActive = true,
            )
        )
    }
}