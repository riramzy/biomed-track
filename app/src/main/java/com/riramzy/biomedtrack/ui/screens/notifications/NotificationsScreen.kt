package com.riramzy.biomedtrack.ui.screens.notifications

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.components.custom.BioMedHorizontalSelector
import com.riramzy.biomedtrack.ui.components.custom.BioMedTopAppBar
import com.riramzy.biomedtrack.ui.components.notifications.BioMedNotificationsCard
import com.riramzy.biomedtrack.ui.components.user.BioMedChangePasswordDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedLogoutDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedMyProfileDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedNotificationPreferencesDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedProfileSheet
import com.riramzy.biomedtrack.ui.screens.auth.AuthVm
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.ActivityItem
import com.riramzy.biomedtrack.utils.ActivityType
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.utils.NotificationHeader
import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.utils.Screen
import com.riramzy.biomedtrack.utils.UserRole

@Composable
fun NotificationsScreen(
    navController: NavHostController,
    notificationsVm: NotificationsVm = hiltViewModel(),
    authVm: AuthVm = hiltViewModel()
) {
    val state by notificationsVm.uiState.collectAsStateWithLifecycle()
    val isPasswordUpdating by authVm.isPasswordUpdating.collectAsStateWithLifecycle()
    val currentUser = notificationsVm.user

    NotificationsScreenContent(
        navController = navController,
        state = state,
        currentUser = currentUser!!,
        onAction = notificationsVm::onAction,
        isPasswordUpdating = isPasswordUpdating,
        changePassword = authVm::changePassword,
        logout = authVm::logout
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreenContent(
    navController: NavHostController,
    state: NotificationsUiState = NotificationsUiState.Success(),
    currentUser: Technician,
    onAction: (NotificationsAction) -> Unit = {},
    isPasswordUpdating: Boolean = false,
    changePassword: (String, String, (Result<Unit>) -> Unit) -> Unit = { _, _, _ -> },
    logout: (() -> Unit) -> Unit = {}
) {
    when(state) {
        is NotificationsUiState.Error -> {
            Scaffold(
                topBar = {
                    BioMedTopAppBar(
                        modifier = Modifier.padding(
                            top = 10.dp
                        )
                    )
                },
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
                                text = stringResource(R.string.retry),
                                onClick = { onAction(NotificationsAction.Refresh) },
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
        is NotificationsUiState.Loading -> {
            Scaffold(
                topBar = {
                    BioMedTopAppBar(
                        modifier = Modifier.padding(
                            top = 10.dp
                        )
                    )
                },
                modifier = Modifier.statusBarsPadding()
            ) { innerPadding ->
                NotificationsShimmer(modifier = Modifier.padding(innerPadding))
            }
        }
        is NotificationsUiState.Success -> {
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 15.dp,
                                    bottom = 15.dp,
                                    start = 15.dp,
                                    end = 15.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start,
                            ) {
                                Text(
                                    text = stringResource(R.string.notifications_title),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                )

                                Text(
                                    text = LocalResources.current.getQuantityString(
                                        R.plurals.unread_notifications_count,
                                        state.unreadCount,
                                        state.unreadCount
                                    ),
                                    style = MaterialTheme.typography.labelLarge,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }

                            BioMedButton(
                                text = stringResource(R.string.mark_all_read),
                                withIcon = false,
                                modifier = Modifier
                                    .width(140.dp),
                                onClick = {
                                    onAction(NotificationsAction.MarkAllAsRead)
                                }
                            )
                        }

                    }

                    item {
                        val categories = remember { NotificationCategory.entries }
                        val categoryLabels = categories.map { stringResource(it.stringResId) }

                        BioMedHorizontalSelector(
                            modifier = Modifier
                                .padding(
                                    bottom = 10.dp,
                                    start = 15.dp,
                                    end = 15.dp
                                )
                                .fillMaxWidth(),
                            items = NotificationCategory.entries.map { stringResource(it.stringResId) },
                            selectedItem = stringResource(state.selectedCategory.stringResId),
                            onItemSelected = { selectedLabel ->
                                val index = categoryLabels.indexOf(selectedLabel)
                                if (index in categories.indices) {
                                    onAction(NotificationsAction.SelectCategory(categories[index]))
                                }
                            }
                        )
                    }

                    state.notifications.forEach { (header, items) ->
                        item {
                            val headerText = when (header) {
                                is NotificationHeader.ResourceHeader -> stringResource(header.stringResId)
                                is NotificationHeader.StringHeader -> header.text
                            }

                            Text(
                                text = headerText,
                                style = MaterialTheme.typography.titleLarge,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .padding(start = 15.dp, bottom = 15.dp)
                                    .fillMaxWidth(),
                                textAlign = TextAlign.Start
                            )
                        }

                        items(items) { item ->
                            BioMedNotificationsCard(
                                item = item,
                                onCardClick = {
                                    onAction.invoke(NotificationsAction.MarkAsRead(item))
                                    navController.navigate(Screen.EquipmentDetail.createRoute(item.equipmentId))
                                },
                                modifier = Modifier.padding(horizontal = 15.dp)
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true, locale = "ar")
@Composable
fun NotificationsScreenPreview() {
    BioMedTheme {
        NotificationsScreenContent(
            navController = NavHostController(LocalContext.current),
            currentUser = Technician(
                id = "1",
                name = "Khaled",
                email = "william.henry.harrison@example-pet-store.com",
                role = UserRole.ADMIN,
                assignedDepartments = emptyList(),
                employeeId = "1",
                isActive = true,
            ),
            state = NotificationsUiState.Success(
                notifications = mapOf(
                    NotificationHeader.StringHeader("Today") to listOf(
                        ActivityItem(
                            id = "1",
                            type = ActivityType.STATUS_CHANGE,
                            title = "Equipment 1",
                            equipmentId = "",
                            equipmentName = "Equipment 1",
                            equipmentModel = "Model 1",
                            equipmentSerial = "Serial 1",
                            departmentName = "Department 1",
                            technicianName = "Technician 1",
                            equipmentStatus = EquipmentStatus.DOWN,
                            previousStatus = EquipmentStatus.ONLINE,
                            timestamp = 12 - 1 - 2015
                        )
                    )
                ),
                unreadCount = 3,
                selectedCategory = NotificationCategory.ALL
            )
        )
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun NotificationsScreenDarkPreview() {
    BioMedTheme {
        NotificationsScreenContent(
            navController = NavHostController(LocalContext.current),
            currentUser = Technician(
                id = "1",
                name = "Khaled",
                email = "william.henry.harrison@example-pet-store.com",
                role = UserRole.ADMIN,
                assignedDepartments = emptyList(),
                employeeId = "1",
                isActive = true,
            ),
            state = NotificationsUiState.Success(
                notifications = mapOf(
                    NotificationHeader.StringHeader("Today") to listOf(
                        ActivityItem(
                            id = "1",
                            type = ActivityType.STATUS_CHANGE,
                            title = "Equipment 1",
                            equipmentId = "",
                            equipmentName = "Equipment 1",
                            equipmentModel = "Model 1",
                            equipmentSerial = "Serial 1",
                            departmentName = "Department 1",
                            technicianName = "Technician 1",
                            equipmentStatus = EquipmentStatus.DOWN,
                            previousStatus = EquipmentStatus.ONLINE,
                            timestamp = 12 - 1 - 2015
                        )
                    )
                ),
                unreadCount = 3,
                selectedCategory = NotificationCategory.ALL
            )
        )
    }
}