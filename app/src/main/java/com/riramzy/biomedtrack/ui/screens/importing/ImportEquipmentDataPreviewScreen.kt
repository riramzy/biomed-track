package com.riramzy.biomedtrack.ui.screens.importing

import android.content.res.Configuration
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.components.custom.BioMedNavBar
import com.riramzy.biomedtrack.ui.components.custom.BioMedProgressIndicator
import com.riramzy.biomedtrack.ui.components.custom.BioMedTopAppBar
import com.riramzy.biomedtrack.ui.components.importing.BioMedDataPreviewTable
import com.riramzy.biomedtrack.ui.components.importing.BioMedImportedDataPreviewCard
import com.riramzy.biomedtrack.ui.components.importing.DataPreviewRow
import com.riramzy.biomedtrack.ui.components.importing.ValidationStatus
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.Screen

@Composable
fun ImportEquipmentDataPreviewScreen(
    navController: NavHostController,
) {
    val parentEntry = remember(navController.currentBackStackEntry) {
        navController.getBackStackEntry(Screen.ImportEquipmentSelectFile.route)
    }

    val importingEquipmentVm: ImportingEquipmentVm = hiltViewModel(parentEntry)

    val state by importingEquipmentVm.uiState.collectAsStateWithLifecycle()

    val previewState = state as? ImportingUiState.PreviewReady
    val previewRows = previewState?.previewRows ?: emptyList()
    val totalValidRows = previewState?.totalValidRows ?: 0
    val selectedRowsCount = previewState?.selectedCount ?: 0

    ImportEquipmentDataPreviewScreenContent(
        previewRows = previewRows,
        totalPreviewRows = totalValidRows,
        selectedRowsCount = selectedRowsCount,
        onImportClick = {
            importingEquipmentVm.startImporting()
            navController.navigate(Screen.ImportEquipmentProcessing.route)
        },
        onReturnClick = {
            importingEquipmentVm.resetState()
            navController.popBackStack()
        },
        onToggleSelectAll = importingEquipmentVm::toggleAllRowsSelection,
        onRowToggle = importingEquipmentVm::toggleRowSelection
    )
}

@Composable
fun ImportEquipmentDataPreviewScreenContent(
    previewRows: List<DataPreviewRow> = emptyList(),
    totalPreviewRows: Int = 0,
    selectedRowsCount: Int = 0,
    onImportClick: () -> Unit = {},
    onReturnClick: () -> Unit = {},
    onToggleSelectAll: (Boolean) -> Unit = {},
    onRowToggle: (String) -> Unit = {}
) {
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
                withActionButton = false,
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
                    currentStep = 1,
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
                        text = "Step 2: Preview",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    BioMedImportedDataPreviewCard(
                        totalRows = totalPreviewRows,
                        validRows = previewRows.count { it.validationStatus == ValidationStatus.VALID },
                        errorRows = previewRows.count { it.validationStatus == ValidationStatus.ERROR },
                        warningRows = previewRows.count { it.validationStatus == ValidationStatus.WARNING }
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
                        text = "Data Preview",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    BioMedDataPreviewTable(
                        rows = previewRows,
                        onRowToggle = { onRowToggle(it) },
                        onToggleSelectAll = onToggleSelectAll
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BioMedButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Return to File Selection",
                            customTextSize = 14,
                            onClick = { onReturnClick() }
                        )

                        BioMedButton(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Import $selectedRowsCount Selected Rows",
                            customTextSize = 14,
                            customColor = MaterialTheme.colorScheme.primary,
                            customTextColor = MaterialTheme.colorScheme.onPrimary,
                            onClick = onImportClick,
                            isEnabled = selectedRowsCount > 0
                        )
                    }
                }
            }
        }
    }
}

@Preview(device = "id:pixel_9", showBackground = false)
@Composable
fun ImportEquipmentDataPreviewScreenPreview() {
    BioMedTheme {
        ImportEquipmentDataPreviewScreenContent()
    }
}

@Preview(device = "id:pixel_9", showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun ImportEquipmentDataPreviewScreenDarkPreview() {
    BioMedTheme {
        ImportEquipmentDataPreviewScreenContent()
    }
}