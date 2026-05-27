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
import androidx.navigation.compose.rememberNavController
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.components.custom.BioMedNavBar
import com.riramzy.biomedtrack.ui.components.custom.BioMedProgressIndicator
import com.riramzy.biomedtrack.ui.components.custom.BioMedTopAppBar
import com.riramzy.biomedtrack.ui.components.importing.BioMedProcessCompletedCard
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
fun ImportEquipmentCompletionScreen(
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

    val successState = state as? ImportingUiState.Success
    val importedCount = successState?.importedCount ?: 0
    val failedCount = successState?.failedCount ?: 0
    val skippedCount = successState?.skippedCount ?: 0

    ImportEquipmentCompletionScreenContent(
        navController = navController,
        currentUser = currentUser!!,
        importedCount = importedCount,
        skippedCount = skippedCount,
        failedCount = failedCount,
        onImportAnotherClick = {
            navController.navigate(Screen.ImportEquipmentSelectFile.route) {
                popUpTo(Screen.ImportEquipmentSelectFile.route) {
                    inclusive = true
                }
            }
        },
        onGoBackToInventoryClick = {
            navController.navigate(Screen.Inventory.route) {
                popUpTo(Screen.Inventory.route) {
                    inclusive = true
                }
            }
        },
        isPasswordUpdating = isPasswordUpdating,
        changePassword = authVm::changePassword,
        logout = authVm::logout
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportEquipmentCompletionScreenContent(
    navController: NavHostController,
    currentUser: Technician,
    importedCount: Int? = null,
    skippedCount: Int? = null,
    failedCount: Int? = null,
    onImportAnotherClick: () -> Unit = {},
    onGoBackToInventoryClick: () -> Unit = {},
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
                    currentStep = 3,
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
                    BioMedProcessCompletedCard(
                        modifier = Modifier.padding(top = 15.dp),
                        successCount = importedCount ?: 0,
                        warningCount = skippedCount ?: 0,
                        errorCount = failedCount ?: 0
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BioMedButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Import Another File",
                            customTextSize = 14,
                            onClick = {
                                onImportAnotherClick()
                            }
                        )

                        BioMedButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Go to Inventory",
                            customTextSize = 14,
                            customColor = MaterialTheme.colorScheme.primary,
                            customTextColor = MaterialTheme.colorScheme.onPrimary,
                            onClick = {
                                onGoBackToInventoryClick()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(device = "id:pixel_9")
@Composable
fun ImportEquipmentCompletionScreenPreview() {
    BioMedTheme {
        ImportEquipmentCompletionScreenContent(
            navController = rememberNavController(),
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

@Preview(device = "id:pixel_9",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun ImportEquipmentCompletionScreenDarkPreview() {
    BioMedTheme {
        ImportEquipmentCompletionScreenContent(
            navController = rememberNavController(),
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