package com.riramzy.biomedtrack.ui.screens.reports

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.utils.FileExportHelper
import com.riramzy.biomedtrack.utils.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ReportsScreen(
    navController: NavHostController,
    reportsVm: ReportsVm = hiltViewModel()
) {
    val state by reportsVm.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ReportsScreenContent(
        navController = navController,
        state = state,
        onAction = reportsVm::onAction,
        context = context
    )
}

@Composable
fun ReportsScreenContent(
    navController: NavHostController,
    state: ReportsUiState = ReportsUiState(),
    onAction: (ReportsAction) -> Unit = {},
    context: Context = LocalContext.current
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.generationResult, state.generationError) {
        state.generationResult?.let {
            snackbarHostState.showSnackbar(
                it,
                withDismissAction = true,
                actionLabel = "Dismiss"
            )
            onAction(ReportsAction.ClearGenerationResult)
        }

        state.generationError?.let {
            snackbarHostState.showSnackbar(
                it,
                withDismissAction = true,
                actionLabel = "Dismiss"
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
                )
            )
        },
        floatingActionButton = {
            BioMedNavBar(
                withActionButton = false,
                selectedPage = "Reports",
                onDashboardClick = { navController.navigate(Screen.Dashboard) },
                onInventoryClick = { navController.navigate(Screen.Inventory) },
                onSchedulerClick = { navController.navigate(Screen.Scheduler) }
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
                        text = "Reports",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )

                    Text(
                        text = "Generate and export detailed reports at ease",
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
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    BioMedHorizontalSelector(
                        items = listOf("Previous Week", "This Week", "Next Week"),
                        onItemSelected = {
                            val now = System.currentTimeMillis()
                            val onWeek = 7L * 24 * 60 * 60 * 1000

                            when(it) {
                                "Previous Week" -> onAction(ReportsAction.SelectDateRange(now - (2 * onWeek), now - onWeek))
                                "This Week" -> onAction(ReportsAction.SelectDateRange(now - onWeek, now))
                                "Next Week" -> onAction(ReportsAction.SelectDateRange(now, now + onWeek))
                            }
                        }
                    )

                    BioMedButton(
                        text = "Filter",
                        withIcon = true,
                        icon = R.drawable.filter,
                        modifier = Modifier,
                        onClick = { onAction(ReportsAction.SetFilterDialogVisibility(true)) }
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
                        BioMedHorizontalSelector(
                            items = listOf("All", "Healthy", "Due Service", "Down"),
                            selectedItem = when(state.selectedStatus) {
                                EquipmentStatus.ONLINE -> "Healthy"
                                EquipmentStatus.SERVICE -> "Due Service"
                                EquipmentStatus.DOWN -> "Down"
                                else -> "All"
                            },
                            onItemSelected = {
                                val status = when(it) {
                                    "Healthy" -> EquipmentStatus.ONLINE
                                    "Due Service" -> EquipmentStatus.SERVICE
                                    "Down" -> EquipmentStatus.DOWN
                                    else -> null
                                }

                                onAction(ReportsAction.SelectStatus(status))
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
                    val departments = listOf("All Departments") + state.departmentsList.map { it.name }

                    BioMedHorizontalSelector(
                        items = departments,
                        selectedItem = state.selectedDepartment?.name ?: "All Departments",
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
                        text = "Include Maintenance Logs",
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
                            text = "Report Summary",
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Text(
                            text = "Refresh Preview",
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
                        text = "Export Format",
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
                        text = "Last Generated",
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
                                                    "File downloaded successfully",
                                                    withDismissAction = true,
                                                    actionLabel = "Dismiss"
                                                )
                                            }
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()

                                        withContext(Dispatchers.Main) {
                                            snackbarHostState.showSnackbar(
                                                "Failed to download file",
                                                withDismissAction = true,
                                                actionLabel = "Dismiss"
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

                                context.startActivity(Intent.createChooser(shareIntent, "Share Report"))
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

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9")
@Composable
fun ReportsScreenPreview() {
    BioMedTheme {
        ReportsScreenContent(navController = NavHostController(LocalContext.current))
    }
}

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun ReportsScreenDarkPreview() {
    BioMedTheme {
        ReportsScreenContent(navController = NavHostController(LocalContext.current))
    }
}