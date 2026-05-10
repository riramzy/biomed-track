package com.riramzy.biomedtrack.ui.screens.scheduler

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.components.custom.BioMedHorizontalSelector
import com.riramzy.biomedtrack.ui.components.custom.BioMedNavBar
import com.riramzy.biomedtrack.ui.components.custom.BioMedTopAppBar
import com.riramzy.biomedtrack.ui.components.custom.BioMedViewToggle
import com.riramzy.biomedtrack.ui.components.equipment.BioMedEquipmentStatusCard
import com.riramzy.biomedtrack.ui.components.schedule.BioMedOverdueExpandedSection
import com.riramzy.biomedtrack.ui.components.schedule.BioMedOverdueOverviewCard
import com.riramzy.biomedtrack.ui.components.schedule.BioMedScheduleDayCard
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.EquipmentStatus

@Composable
fun SchedulerScreen(
    navController: NavHostController,
    isExpanded: Boolean = false
) {
    var expandedOrNot by remember { mutableStateOf(isExpanded) }

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
                withActionButton = true,
                actionButtonIcon = R.drawable.add
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
                        text = "Scheduler",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                    )

                    Text(
                        text = "Plan and track equipment maintenance schedules",
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
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
                    horizontalAlignment = Alignment.Start,
                ) {
                    BioMedViewToggle(weekOrList = if (isExpanded) "List" else "Week")
                }
            }

            if (!isExpanded) {
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
                            items = listOf(
                                "Previous Week",
                                "This Week",
                                "Next Week",
                            )
                        )

                        BioMedButton(
                            text = "Filter",
                            withIcon = true,
                            icon = R.drawable.filter,
                            modifier = Modifier
                        )
                    }
                }
            }

            if (!isExpanded) {
                item {
                    BioMedOverdueOverviewCard(
                        modifier = Modifier.padding(
                            bottom = 15.dp
                        )
                    )
                }
            }

            if (!isExpanded) {
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
                        BioMedScheduleDayCard(modifier = Modifier.padding(bottom = 10.dp))
                        BioMedScheduleDayCard(modifier = Modifier.padding(bottom = 10.dp))
                        BioMedScheduleDayCard(modifier = Modifier.padding(bottom = 10.dp))
                        BioMedScheduleDayCard(modifier = Modifier.padding(bottom = 10.dp))
                        BioMedScheduleDayCard(modifier = Modifier.padding(bottom = 10.dp))
                        BioMedScheduleDayCard()
                    }
                }
            }

            if (isExpanded) {
                item {
                    BioMedOverdueExpandedSection(
                        modifier = Modifier.padding(
                            bottom = 15.dp
                        )
                    )
                }
            }

            if (isExpanded) {
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
                                text = "Upcoming Maintenance (5)",
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }

                        BioMedEquipmentStatusCard(equipmentStatus = EquipmentStatus.SERVICE, modifier = Modifier.padding(bottom = 10.dp))
                        BioMedEquipmentStatusCard(equipmentStatus = EquipmentStatus.DOWN, modifier = Modifier.padding(bottom = 10.dp))
                        BioMedEquipmentStatusCard(equipmentStatus = EquipmentStatus.DOWN, modifier = Modifier.padding(bottom = 10.dp))
                        BioMedEquipmentStatusCard(equipmentStatus = EquipmentStatus.ONLINE, modifier = Modifier.padding(bottom = 10.dp))
                        BioMedEquipmentStatusCard(equipmentStatus = EquipmentStatus.SERVICE, modifier = Modifier.padding(bottom = 10.dp))
                    }
                }
            }
        }
    }
}

@Preview(device = "id:pixel_9", showBackground = true)
@Composable
fun SchedulerScreenPreview() {
    BioMedTheme {
        SchedulerScreen(navController = NavHostController(LocalContext.current))
    }
}

@Preview(device = "id:pixel_9", showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun SchedulerScreenDArkPreview() {
    BioMedTheme {
        SchedulerScreen(navController = NavHostController(LocalContext.current))
    }
}