package com.riramzy.biomedtrack.ui.screens.equipment.edit

import android.content.res.Configuration
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.riramzy.biomedtrack.ui.components.custom.BioMedNavBar
import com.riramzy.biomedtrack.ui.components.custom.BioMedSnackbar
import com.riramzy.biomedtrack.ui.components.custom.BioMedTopAppBar
import com.riramzy.biomedtrack.ui.components.equipment.BioMedEditEquipmentCard
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun EditEquipmentScreen(
    navController: NavHostController,
    editEquipmentVm: EditEquipmentVm = hiltViewModel()
) {
    val state by editEquipmentVm.uiState.collectAsStateWithLifecycle()

    EditEquipmentScreenContent(
        navController = navController,
        state = state,
        onAction = editEquipmentVm::onAction
    )
}

@Composable
fun EditEquipmentScreenContent(
    navController: NavHostController,
    state: EditEquipmentUiState = EditEquipmentUiState(),
    onAction: (EditEquipmentAction) -> Unit = {}
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
                    onCancel = { navController.popBackStack() }
                )
            }
        }
    }
}

@Preview(device = "id:pixel_9")
@Composable
fun EditEquipmentScreenPreview() {
    BioMedTheme {
        EditEquipmentScreenContent(navController = NavHostController(LocalContext.current))
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun EditEquipmentScreenDarkPreview() {
    BioMedTheme {
        EditEquipmentScreenContent(navController = NavHostController(LocalContext.current))
    }
}