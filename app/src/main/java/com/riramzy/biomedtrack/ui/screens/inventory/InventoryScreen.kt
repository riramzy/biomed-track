package com.riramzy.biomedtrack.ui.screens.inventory

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.components.BioMedButton
import com.riramzy.biomedtrack.ui.components.BioMedEquipmentOverviewCard
import com.riramzy.biomedtrack.ui.components.BioMedHorizontalSelector
import com.riramzy.biomedtrack.ui.components.BioMedNavBar
import com.riramzy.biomedtrack.ui.components.BioMedSearchBar
import com.riramzy.biomedtrack.ui.components.BioMedTopAppBar
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun InventoryScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            BioMedTopAppBar(
                modifier = Modifier.padding(
                    top = 10.dp
                )
            )
        },
        floatingActionButton = { BioMedNavBar() },
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
                        text = "Inventory",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )

                    Text(
                        text = "Manage and track all medical equipment",
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .padding(
                            bottom = 15.dp,
                            start = 15.dp,
                            end = 15.dp
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        modifier = Modifier.padding(bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        BioMedSearchBar(
                            modifier = Modifier.weight(3f)
                        )

                        BioMedButton(
                            withIcon = true,
                            icon = R.drawable.filter,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    BioMedHorizontalSelector()
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = 15.dp,
                            start = 15.dp,
                            end = 15.dp
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    BioMedEquipmentOverviewCard(modifier = Modifier.padding(bottom = 10.dp))
                    BioMedEquipmentOverviewCard(status = "Service", modifier = Modifier.padding(bottom = 10.dp))
                    BioMedEquipmentOverviewCard(status = "Service", modifier = Modifier.padding(bottom = 10.dp))
                    BioMedEquipmentOverviewCard(modifier = Modifier.padding(bottom = 10.dp))
                    BioMedEquipmentOverviewCard(modifier = Modifier.padding(bottom = 10.dp))
                    BioMedEquipmentOverviewCard(modifier = Modifier.padding(bottom = 10.dp))
                    BioMedEquipmentOverviewCard(status = "Down", modifier = Modifier.padding(bottom = 10.dp))
                    BioMedEquipmentOverviewCard(status = "Down")
                }
            }
        }
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true)
@Composable
fun InventoryScreenPreview() {
    BioMedTheme {
        InventoryScreen(navController = NavHostController(LocalContext.current))
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun InventoryScreenDarkPreview() {
    BioMedTheme {
        InventoryScreen(navController = NavHostController(LocalContext.current))
    }
}