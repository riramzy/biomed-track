package com.riramzy.biomedtrack.ui.screens.equipment.edit

import android.content.res.Configuration
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.ui.components.custom.BioMedNavBar
import com.riramzy.biomedtrack.ui.components.custom.BioMedSnackbar
import com.riramzy.biomedtrack.ui.components.custom.BioMedTopAppBar
import com.riramzy.biomedtrack.ui.components.equipment.BioMedEditEquipmentCard
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
import java.io.File

@Composable
fun EditEquipmentScreen(
    navController: NavHostController,
    editEquipmentVm: EditEquipmentVm = hiltViewModel(),
    authVm: AuthVm = hiltViewModel()
) {
    val state by editEquipmentVm.uiState.collectAsStateWithLifecycle()
    val currentUser by editEquipmentVm.currentUser.collectAsStateWithLifecycle()
    val isPasswordUpdating by authVm.isPasswordUpdating.collectAsStateWithLifecycle()

    EditEquipmentScreenContent(
        navController = navController,
        state = state,
        currentUser = currentUser!!,
        onAction = editEquipmentVm::onAction,
        isPasswordUpdating = isPasswordUpdating,
        changePassword = authVm::changePassword,
        logout = authVm::logout
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEquipmentScreenContent(
    navController: NavHostController,
    state: EditEquipmentUiState = EditEquipmentUiState(),
    currentUser: Technician,
    onAction: (EditEquipmentAction) -> Unit = {},
    isPasswordUpdating: Boolean = false,
    changePassword: (String, String, (Result<Unit>) -> Unit) -> Unit = { _, _, _ -> },
    logout: (() -> Unit) -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current
    val dismissLabel = stringResource(R.string.dismiss)

    var showProfileBottomSheet by remember { mutableStateOf(false) }
    var showMyProfileDialog by remember { mutableStateOf(false) }
    var showNotificationsPreferencesDialog by remember { mutableStateOf(false) }
    var showChangePasswordDialog by remember { mutableStateOf(false) }
    var showLogoutConfirmDialog by remember { mutableStateOf(false) }

    val isPreview = LocalInspectionMode.current
    val tempUri = remember(context) {
        if (isPreview) {
            Uri.EMPTY
        } else {
            val file = File(context.cacheDir, "temp_image.jpg")
            FileProvider.getUriForFile(
                context,
                "com.riramzy.biomedtrack.fileprovider",
                file
            )
        }
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                onAction(EditEquipmentAction.AddPhoto(tempUri.toString()))
            }
        }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            navController.popBackStack()
        }
    }

    LaunchedEffect(state.isError) {
        state.isError?.let { message ->
            snackbarHostState.showSnackbar(
                message,
                withDismissAction = true,
                actionLabel = dismissLabel
            )
            onAction(EditEquipmentAction.ResetError)
        }
    }

    if (state.isLoading) {
        Scaffold(
            topBar = {
                BioMedTopAppBar(
                    modifier = Modifier.padding(
                        top = 10.dp
                    )
                )
            },
            bottomBar = {
                BioMedNavBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    withActionButton = false,
                    selectedPage = "Inventory"
                )
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    BioMedSnackbar(
                        snackbarData = data,
                        isError = state.isError != null
                    )
                }
            },
            modifier = Modifier.statusBarsPadding()
        ) { innerPadding ->
            EditEquipmentShimmer(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    } else {
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
            bottomBar = {
                BioMedNavBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    withActionButton = false,
                    selectedPage = "Inventory",
                    onDashboardClick = { navController.navigate(Screen.Dashboard.route) },
                    onSchedulerClick = { navController.navigate(Screen.Scheduler.route) },
                    onInventoryClick = { navController.navigate(Screen.Inventory.route) },
                    onReportsClick = { navController.navigate(Screen.Reports.route) }
                )
            },
            snackbarHost = {
                SnackbarHost(snackbarHostState) { data ->
                    BioMedSnackbar(
                        snackbarData = data,
                        isError = state.isError != null
                    )
                }
            },
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                BioMedEditEquipmentCard(
                    modifier = Modifier.padding(top = 20.dp),
                    state = state,
                    onAction = onAction,
                    onAddNewPhoto = {
                        cameraLauncher.launch(tempUri)
                    },
                    onCancel = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Preview(device = "id:pixel_9")
@Composable
fun EditEquipmentScreenPreview() {
    BioMedTheme {
        EditEquipmentScreenContent(
            navController = NavHostController(LocalContext.current),
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
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun EditEquipmentScreenDarkPreview() {
    BioMedTheme {
        EditEquipmentScreenContent(
            navController = NavHostController(LocalContext.current),
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