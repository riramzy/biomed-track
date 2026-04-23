package com.riramzy.biomedtrack.ui.screens.maintenance

import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.riramzy.biomedtrack.ui.components.custom.BioMedNavBar
import com.riramzy.biomedtrack.ui.components.custom.BioMedSnackbar
import com.riramzy.biomedtrack.ui.components.custom.BioMedTopAppBar
import com.riramzy.biomedtrack.ui.components.maintenance.BioMedLogMaintenanceCard
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.Screen
import java.io.File

@Composable
fun LogMaintenanceScreen(
    navController: NavHostController,
    logMaintenanceVm: LogMaintenanceVm = hiltViewModel()
) {
    val state by logMaintenanceVm.uiState.collectAsStateWithLifecycle()

    LogMaintenanceScreenContent(
        navController = navController,
        state = state,
        onAction = logMaintenanceVm::onAction
    )
}

@Composable
fun LogMaintenanceScreenContent(
    navController: NavHostController,
    state: LogMaintenanceUiState = LogMaintenanceUiState(),
    onAction: (LogMaintenanceAction) -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }

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
                actionLabel = "Dismiss"
            )
            onAction(LogMaintenanceAction.ResetError)
        }
    }

    val context = LocalContext.current
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

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            onAction(LogMaintenanceAction.AddPhoto(tempUri.toString()))
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            BioMedLogMaintenanceCard(
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

@Preview(device = "id:pixel_9")
@Composable
fun LogMaintenanceScreenPreview() {
    BioMedTheme {
        LogMaintenanceScreenContent(navController = NavHostController(LocalContext.current))
    }
}

@Preview(device = "id:pixel_9",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showSystemUi = false
)
@Composable
fun LogMaintenanceScreenDarkPreview() {
    BioMedTheme {
        LogMaintenanceScreenContent(navController = NavHostController(LocalContext.current))
    }
}