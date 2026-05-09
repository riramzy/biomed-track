package com.riramzy.biomedtrack.ui.screens.importing

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.riramzy.biomedtrack.ui.components.custom.BioMedNavBar
import com.riramzy.biomedtrack.ui.components.custom.BioMedNoteCard
import com.riramzy.biomedtrack.ui.components.custom.BioMedProgressIndicator
import com.riramzy.biomedtrack.ui.components.custom.BioMedSnackbar
import com.riramzy.biomedtrack.ui.components.custom.BioMedTopAppBar
import com.riramzy.biomedtrack.ui.components.custom.BioMedUploadFileCard
import com.riramzy.biomedtrack.ui.components.importing.BioMedGeneratedFileCard
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.Screen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun ImportEquipmentSelectFileScreen(
    navController: NavHostController,
    importingEquipmentVm: ImportingEquipmentVm = hiltViewModel()
) {
    val state by importingEquipmentVm.uiState.collectAsStateWithLifecycle()
    val uploadHistory by importingEquipmentVm.uploadHistory.collectAsStateWithLifecycle()
    val selectedFile by importingEquipmentVm.selectedFile.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        if (state is ImportingUiState.PreviewReady) {
            navController.navigate(Screen.ImportEquipmentPreview.route)
        }
    }

    ImportEquipmentSelectFileScreenContent(
        navController = navController,
        state = state,
        uploadHistory = uploadHistory,
        selectedFile = selectedFile,
        snackbarEvent = importingEquipmentVm.snackbarEvent,
        downloadTemplate = importingEquipmentVm::downloadTemplate,
        selectFileForPreview = importingEquipmentVm::selectFileForPreview,
    )
}

@Composable
fun ImportEquipmentSelectFileScreenContent(
    navController: NavHostController,
    state: ImportingUiState = ImportingUiState.Idle,
    uploadHistory: List<UploadedFile> = emptyList(),
    selectedFile: UploadedFile? = null,
    snackbarEvent: SharedFlow<String> = MutableSharedFlow(),
    downloadTemplate: () -> Unit = {},
    selectFileForPreview: (Uri) -> Unit = { _ -> },
) {
    val snackbarHostState = remember { SnackbarHostState() }

    val excelPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            selectFileForPreview(uri)
        }
    }

    LaunchedEffect(Unit) {
        snackbarEvent.collect { message ->
            snackbarHostState.showSnackbar(
                message = message,
                withDismissAction = true,
                actionLabel = "Dismiss",
                duration = SnackbarDuration.Short
            )
        }
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
                selectedPage = "None",
                withActionButton = true,
                isActionButtonText = true,
                actionButtonText = "Preview",
                onActionButtonClick = {
                    if (state !is ImportingUiState.PreviewReady) return@BioMedNavBar
                    navController.navigate(Screen.ImportEquipmentPreview.route)
                },
                modifier = Modifier.padding(horizontal = 15.dp)
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                BioMedSnackbar(
                    snackbarData = data,
                    isError = state is ImportingUiState.Error,
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
                    currentStep = 0,
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
                        text = "Step 1: Select File",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    BioMedUploadFileCard(
                        onBrowseClick = {
                            excelPickerLauncher.launch("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                        },
                        isUploaded = selectedFile != null
                    )

                    BioMedNoteCard(
                        note = "Excel format must match the exact format of the template for easy and successful data extraction",
                        onNoteButtonClick = { downloadTemplate() }
                    )
                }
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
                        text = "Uploaded Files",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    uploadHistory.forEach { file ->
                        BioMedGeneratedFileCard(
                            isUploaded = true,
                            fileName = file.fileName,
                            fileSize = "${file.fileSize / 1024} KB"
                        )
                    }
                }
            }
        }
    }
}

@Preview(device = "id:pixel_9")
@Composable
fun ImportEquipmentSelectFileScreenPreview() {
    BioMedTheme {
        ImportEquipmentSelectFileScreenContent(
            navController = rememberNavController(),
            state = ImportingUiState.Idle,
            uploadHistory = listOf(
                UploadedFile(
                    uri = Uri.EMPTY,
                    fileName = "Sample File.xlsx",
                    fileSize = 1024
                )
            )
        )
    }
}

@Preview(device = "id:pixel_9",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun ImportEquipmentSelectFileScreenDarkPreview() {
    BioMedTheme {
        ImportEquipmentSelectFileScreenContent(
            navController = rememberNavController(),
            state = ImportingUiState.Idle,
            uploadHistory = listOf(
                UploadedFile(
                    uri = Uri.EMPTY,
                    fileName = "Sample File.xlsx",
                    fileSize = 1024
                )
            )
        )
    }
}