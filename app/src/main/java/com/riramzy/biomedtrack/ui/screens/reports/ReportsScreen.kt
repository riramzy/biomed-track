package com.riramzy.biomedtrack.ui.screens.reports

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.components.custom.BioMedDateRangeSelector
import com.riramzy.biomedtrack.ui.components.custom.BioMedHorizontalSelector
import com.riramzy.biomedtrack.ui.components.custom.BioMedNavBar
import com.riramzy.biomedtrack.ui.components.custom.BioMedSnackbar
import com.riramzy.biomedtrack.ui.components.custom.BioMedToggle
import com.riramzy.biomedtrack.ui.components.custom.BioMedTopAppBar
import com.riramzy.biomedtrack.ui.components.importing.BioMedGeneratedFileCard
import com.riramzy.biomedtrack.ui.components.report.BioMedReportExportFormatCard
import com.riramzy.biomedtrack.ui.components.report.BioMedReportSummaryCard
import com.riramzy.biomedtrack.ui.components.user.BioMedChangePasswordDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedLogoutDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedMyProfileDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedNotificationPreferencesDialog
import com.riramzy.biomedtrack.ui.components.user.BioMedProfileSheet
import com.riramzy.biomedtrack.ui.screens.auth.AuthVm
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.utils.FileExportHelper
import com.riramzy.biomedtrack.utils.Result
import com.riramzy.biomedtrack.utils.Screen
import com.riramzy.biomedtrack.utils.UserRole
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun ReportsScreen(
    navController: NavHostController,
    reportsVm: ReportsVm = hiltViewModel(),
    authVm: AuthVm = hiltViewModel()
) {
    val state by reportsVm.uiState.collectAsStateWithLifecycle()
    val currentUser by reportsVm.currentUser.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val isPasswordUpdating by authVm.isPasswordUpdating.collectAsStateWithLifecycle()

    ReportsScreenContent(
        navController = navController,
        state = state,
        currentUser = currentUser!!,
        onAction = reportsVm::onAction,
        context = context,
        isPasswordUpdating = isPasswordUpdating,
        changePassword = authVm::changePassword,
        logout = authVm::logout
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreenContent(
    navController: NavHostController,
    state: ReportsUiState = ReportsUiState(),
    currentUser: Technician,
    onAction: (ReportsAction) -> Unit = {},
    context: Context = LocalContext.current,
    isPasswordUpdating: Boolean = false,
    changePassword: (String, String, (Result<Unit>) -> Unit) -> Unit = { _, _, _ -> },
    logout: (() -> Unit) -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var selectedPeriodIndex by remember { mutableIntStateOf(1) }
    val sheetState = rememberModalBottomSheetState()

    val dismissLabel = stringResource(R.string.dismiss)
    val generationSuccessMessage = stringResource(R.string.reports_download_success)
    val generationErrorMessage = stringResource(R.string.reports_download_failed)
    val reportsShareChooser = stringResource(R.string.reports_share_chooser_title)

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

    LaunchedEffect(state.generationResult, state.generationError) {
        state.generationResult?.let {
            snackbarHostState.showSnackbar(
                message = generationSuccessMessage,
                withDismissAction = true,
                actionLabel = dismissLabel
            )
            onAction(ReportsAction.ClearGenerationResult)
        }

        state.generationError?.let {
            snackbarHostState.showSnackbar(
                message = generationErrorMessage,
                withDismissAction = true,
                actionLabel = dismissLabel
            )
            onAction(ReportsAction.ClearGenerationResult)
        }
    }

    if (state.isFilterDialogOpen) {
        BioMedDateRangeSelector(
            startDate = state.startDate,
            endDate = state.endDate,
            onConfirm = { start, end ->
                onAction(ReportsAction.ApplyCustomDateRange(start, end))
                onAction(ReportsAction.SetFilterDialogVisibility(false))
            },
            onCancel = { onAction(ReportsAction.SetFilterDialogVisibility(false)) }
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
                withActionButton = false,
                selectedPage = "Reports",
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
                    isError = state.generationError != null
                )
            }
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
                        text = stringResource(R.string.reports_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )

                    Text(
                        text = stringResource(R.string.reports_subtitle),
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .padding(
                            bottom = 15.dp,
                            start = 15.dp,
                            end = 15.dp
                        )
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val periodLabels = listOf(
                        stringResource(R.string.reports_period_previous),
                        stringResource(R.string.reports_period_current),
                        stringResource(R.string.reports_period_next)
                    )

                    BioMedHorizontalSelector(
                        items = periodLabels,
                        selectedItem = periodLabels[selectedPeriodIndex],
                        onItemSelected = { label ->
                            val index = periodLabels.indexOf(label)
                            selectedPeriodIndex = index

                            val now = LocalDate.now()
                            val zoneId = ZoneId.systemDefault()

                            when (index) {
                                0 -> {
                                    val prev = now.minusMonths(1)
                                    onAction(
                                        ReportsAction.SelectDateRange(
                                            prev.withDayOfMonth(1).atStartOfDay(zoneId).toInstant()
                                                .toEpochMilli(),
                                            prev.withDayOfMonth(prev.lengthOfMonth())
                                                .atTime(23, 59, 59).atZone(zoneId).toInstant()
                                                .toEpochMilli()
                                        )
                                    )
                                }

                                1 -> {
                                    onAction(
                                        ReportsAction.SelectDateRange(
                                            now.withDayOfMonth(1).atStartOfDay(zoneId).toInstant()
                                                .toEpochMilli(),
                                            now.withDayOfMonth(now.lengthOfMonth())
                                                .atTime(23, 59, 59).atZone(zoneId).toInstant()
                                                .toEpochMilli()
                                        )
                                    )
                                }

                                2 -> {
                                    val next = now.plusMonths(1)
                                    onAction(
                                        ReportsAction.SelectDateRange(
                                            next.withDayOfMonth(1).atStartOfDay(zoneId).toInstant()
                                                .toEpochMilli(),
                                            next.withDayOfMonth(next.lengthOfMonth())
                                                .atTime(23, 59, 59).atZone(zoneId).toInstant()
                                                .toEpochMilli()
                                        )
                                    )
                                }
                            }
                        },
                        modifier = Modifier.weight(3f)
                    )

                    BioMedButton(
                        text = stringResource(R.string.filter_button),
                        withIcon = true,
                        icon = R.drawable.filter,
                        onClick = { onAction(ReportsAction.SetFilterDialogVisibility(true)) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = 15.dp,
                            start = 15.dp,
                            end = 15.dp
                        ),
                    shape = RoundedCornerShape(25.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSystemInDarkTheme()) {
                            MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
                        } else {
                            MaterialTheme.colorScheme.primaryContainer.copy(0.5f)
                        }
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {
                        val statusLabels = listOf(
                            stringResource(R.string.reports_status_all),
                            stringResource(R.string.status_online),
                            stringResource(R.string.status_service),
                            stringResource(R.string.status_down)
                        )

                        val statusValues = listOf(
                            null,
                            EquipmentStatus.ONLINE,
                            EquipmentStatus.SERVICE,
                            EquipmentStatus.DOWN
                        )

                        val selectedStatusIndex =
                            statusValues.indexOf(state.selectedStatus).let { if (it < 0) 0 else it }

                        BioMedHorizontalSelector(
                            items = statusLabels,
                            selectedItem = statusLabels[selectedStatusIndex],
                            onItemSelected = { label ->
                                val index = statusLabels.indexOf(label)
                                onAction(ReportsAction.SelectStatus(statusValues.getOrElse(index) { null }))
                            }
                        )
                    }
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
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val allDeptLabel = stringResource(R.string.reports_dept_all)
                    val departments = listOf(allDeptLabel) + state.departmentsList.map { it.name }

                    BioMedHorizontalSelector(
                        items = departments,
                        selectedItem = state.selectedDepartment?.name ?: allDeptLabel,
                        onItemSelected = { deptName ->
                            val selectedDepartment = state.departmentsList.find { it.name == deptName }
                            onAction(ReportsAction.SelectDepartment(selectedDepartment))
                        }
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
                        )
                ) {
                    BioMedToggle(
                        text = stringResource(R.string.reports_toggle_include_logs),
                        modifier = Modifier
                            .fillMaxWidth(),
                        isChecked = state.includeLogs,
                        onCheckedChange = { onAction(ReportsAction.ToggleLogs(it)) }
                    )
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
                            text = stringResource(R.string.reports_section_summary),
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Text(
                            text = stringResource(R.string.reports_refresh_preview),
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable {
                                onAction(ReportsAction.Refresh)
                            }
                        )
                    }

                    BioMedReportSummaryCard(
                        totalEquipment = state.totalCount,
                        healthy = state.healthyCount,
                        dueService = state.dueServiceCount,
                        down = state.downCount,
                        logsCount = if (state.includeLogs) {
                            state.dueServiceCount + state.downCount
                        } else {
                            0
                        }
                    )
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
                    Text(
                        text = stringResource(R.string.reports_section_export_format),
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp)
                    )

                    BioMedReportExportFormatCard(
                        selectedFormat = state.exportFormat,
                        onFormatSelected = { onAction(ReportsAction.SelectFormat(it)) },
                        onGenerateReport = {
                            val isPdf = state.exportFormat == "PDF"
                            val extension = if (isPdf) "pdf" else "xlsx"
                            val mimeType = if (isPdf) "application/pdf" else "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                            val fileName = "BioMedTrack_Report_${System.currentTimeMillis()}.$extension"
                            val target = FileExportHelper.getOutputStreamForExport(context, fileName, mimeType)
                            val stream = target?.outputStream
                            val uriStream = target?.uri.toString()

                            if (isPdf) {
                                onAction(ReportsAction.GeneratePdfReport(stream, uriStream))
                            } else {
                                onAction(ReportsAction.GenerateExcelReport(stream, uriStream))
                            }
                        }
                    )
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
                    Text(
                        text = stringResource(R.string.reports_section_last_generated),
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 15.dp)
                    )

                    state.generatedReports.forEach { report ->
                        BioMedGeneratedFileCard(
                            fileName = report.fileName,
                            fileSize = report.fileSize,
                            fileFormat = if (report.isPdf) "PDF" else "Excel",
                            isUploaded = false,
                            onDownloadClick = {
                                coroutineScope.launch(Dispatchers.IO) {
                                    try {
                                        val mimeType = if (report.isPdf) "application/pdf" else "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                                        val newFileName = "Copy_${report.fileName}"
                                        val target = FileExportHelper.getOutputStreamForExport(context, newFileName, mimeType)
                                        val stream = target?.outputStream

                                        stream.let {
                                            val inStream = context.contentResolver.openInputStream(report.fileUri.toUri())

                                            inStream?.use { input ->
                                                stream?.use { output ->
                                                    input.copyTo(output)
                                                }
                                            }

                                            withContext(Dispatchers.Main) {
                                                snackbarHostState.showSnackbar(
                                                    message = generationSuccessMessage,
                                                    withDismissAction = true,
                                                    actionLabel = dismissLabel
                                                )
                                            }
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()

                                        withContext(Dispatchers.Main) {
                                            snackbarHostState.showSnackbar(
                                                message = generationErrorMessage,
                                                withDismissAction = true,
                                                actionLabel = dismissLabel
                                            )
                                        }
                                    }
                                }
                            },
                            onDeleteClick = {},
                            onShareClick = {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = if (report.isPdf) "application/pdf" else "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                                    putExtra(Intent.EXTRA_STREAM, report.fileUri.toUri())
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }

                                context.startActivity(
                                    Intent.createChooser(
                                        shareIntent,
                                        reportsShareChooser
                                    )
                                )
                            },
                            onPreviewClick = {
                                val previewIntent = Intent(Intent.ACTION_VIEW).apply {
                                    setDataAndType(report.fileUri.toUri(), if (report.isPdf) "application/pdf" else "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }

                                try {
                                    context.startActivity(previewIntent)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        )

                        Spacer(modifier = Modifier.height(15.dp))
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9", locale = "ar")
@Composable
fun ReportsScreenPreview() {
    BioMedTheme {
        ReportsScreenContent(
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
        )
    }
}

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL, locale = "ar"
)
@Composable
fun ReportsScreenDarkPreview() {
    BioMedTheme {
        ReportsScreenContent(
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
        )
    }
}