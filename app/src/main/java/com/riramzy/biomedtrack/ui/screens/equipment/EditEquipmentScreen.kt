package com.riramzy.biomedtrack.ui.screens.equipment

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.components.BioMedEditEquipmentCard
import com.riramzy.biomedtrack.ui.components.BioMedNavBar
import com.riramzy.biomedtrack.ui.components.BioMedTopAppBar
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun EditEquipmentScreen(navController: NavHostController, equipmentId: String?) {
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
                withActionButton = true,
                actionButtonIcon = R.drawable.activity_online,
                selectedPage = "Inventory"
            )
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
                modifier = Modifier.padding(top = 20.dp)
            )
        }
    }
}

@Preview(device = "id:pixel_9")
@Composable
fun EditEquipmentScreenPreview() {
    BioMedTheme {
        EditEquipmentScreen(navController = NavHostController(LocalContext.current), equipmentId = null)
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun EditEquipmentScreenDarkPreview() {
    BioMedTheme {
        EditEquipmentScreen(navController = NavHostController(LocalContext.current), equipmentId = null)
    }
}