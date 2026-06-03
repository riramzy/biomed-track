package com.riramzy.biomedtrack.ui.screens.scheduler

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.riramzy.biomedtrack.domain.model.ChecklistItem
import com.riramzy.biomedtrack.ui.components.custom.BioMedNavBar
import com.riramzy.biomedtrack.ui.components.custom.BioMedSnackbar
import com.riramzy.biomedtrack.ui.components.custom.BioMedTopAppBar
import com.riramzy.biomedtrack.ui.components.schedule.BioMedScheduleMaintenanceCard
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.Screen

@Composable
fun ScheduleMaintenanceScreen(
    navController: NavHostController,
    scheduleMaintenanceVm: ScheduleMaintenanceVm = hiltViewModel()
) {
    val state by scheduleMaintenanceVm.uiState.collectAsStateWithLifecycle()

    ScheduleMaintenanceScreenContent(
        navController = navController,
        state = state,
        onAction = scheduleMaintenanceVm::onAction
    )
}

@Composable
fun ScheduleMaintenanceScreenContent(
    navController: NavHostController,
    state: ScheduleMaintenanceUiState = ScheduleMaintenanceUiState(),
    onAction: (ScheduleMaintenanceAction) -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            navController.navigate(Screen.Scheduler.route) {
                popUpTo(Screen.Scheduler.route) {
                    inclusive = true
                }
            }
        }
    }

    LaunchedEffect(state.isError) {
        state.isError?.let { message ->
            snackbarHostState.showSnackbar(
                message,
                actionLabel = "Dismiss",
                withDismissAction = true
            )
            onAction(ScheduleMaintenanceAction.ResetError)
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
                modifier = Modifier
                    .fillMaxWidth(),
                withActionButton = false,
                selectedPage = "Scheduler",
                onInventoryClick = { navController.navigate(Screen.Inventory.route) },
                onReportsClick = { navController.navigate(Screen.Reports.route) },
                onDashboardClick = { navController.navigate(Screen.Dashboard.route) },
                onSchedulerClick = { navController.navigate(Screen.Scheduler.route) }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                BioMedSnackbar(
                    snackbarData = it,
                    isError = state.isError != null,
                    modifier = Modifier.padding(10.dp)
                )
            }
        },
        modifier = Modifier.statusBarsPadding()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BioMedScheduleMaintenanceCard(
                modifier = Modifier.padding(
                    top = 20.dp,
                    start = 15.dp,
                    end = 15.dp,
                    bottom = 100.dp
                ),
                equipmentList = state.equipmentList,
                selectedEquipment = state.selectedEquipment,
                onEquipmentSelected = { onAction(ScheduleMaintenanceAction.SelectEquipment(it)) },
                dueDate = state.selectedDate,
                onDateSelected = { onAction(ScheduleMaintenanceAction.SelectDate(it)) },
                techniciansList = state.techniciansList.map { it.name },
                selectedTechnicianName = state.selectedTechnician?.name ?: "",
                onTechnicianSelected = { onAction(ScheduleMaintenanceAction.SelectTechnician(it)) },
                notes = state.notes,
                onNotesChanged = { onAction(ScheduleMaintenanceAction.UpdateNotes(it)) },
                checklist = state.checklist,
                onToggleChecklist = { onAction(ScheduleMaintenanceAction.ToggleChecklistItem(it)) },
                onAddChecklistItem = {
                    onAction(
                        ScheduleMaintenanceAction.AddChecklistItem(
                            ChecklistItem(id = "", label = it, isChecked = false)
                        )
                    )
                },
                onRemoveChecklistItem = { onAction(ScheduleMaintenanceAction.RemoveChecklistItem(it)) },
                onScheduleClick = { onAction(ScheduleMaintenanceAction.ScheduleTask) },
                onCancelClick = { navController.navigate(Screen.Scheduler.route) }
            )
        }
    }
}

@Preview(device = "id:pixel_9", showBackground = true)
@Composable
fun ScheduleMaintenanceScreenPreview() {
    BioMedTheme {
        ScheduleMaintenanceScreenContent(
            navController = NavHostController(LocalContext.current)
        )
    }
}

@Preview(device = "id:pixel_9", showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun ScheduleMaintenanceScreenDarkPreview() {
    BioMedTheme {
        ScheduleMaintenanceScreenContent(
            navController = NavHostController(LocalContext.current)
        )
    }
}