package com.riramzy.biomedtrack.ui.screens.scheduler

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.components.custom.BioMedDateRangeSelector
import com.riramzy.biomedtrack.ui.components.custom.BioMedHorizontalSelector
import com.riramzy.biomedtrack.ui.components.custom.BioMedNavBar
import com.riramzy.biomedtrack.ui.components.custom.BioMedTopAppBar
import com.riramzy.biomedtrack.ui.components.custom.BioMedViewToggle
import com.riramzy.biomedtrack.ui.components.schedule.BioMedOverdueExpandedSection
import com.riramzy.biomedtrack.ui.components.schedule.BioMedOverdueOverviewCard
import com.riramzy.biomedtrack.ui.components.schedule.BioMedScheduleDayCard
import com.riramzy.biomedtrack.ui.components.schedule.BioMedTaskCard
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.Screen
import com.riramzy.biomedtrack.utils.Timestamps.toDayMonthString
import com.riramzy.biomedtrack.utils.Timestamps.toDayName

@Composable
fun SchedulerScreen(
    navController: NavHostController,
    schedulerVm: SchedulerVm = hiltViewModel()
) {
    val state by schedulerVm.uiState.collectAsStateWithLifecycle()

    SchedulerScreenContent(
        navController = navController,
        state = state,
        onNextWeekClick = { schedulerVm.nextWeek() },
        onPreviousWeekClick = { schedulerVm.previousWeek() },
        onCurrentWeekClick = { schedulerVm.currentWeek() },
        onToggleViewClick = { schedulerVm.toggleViewMode() },
        onCustomDateRangeSelected = { start, end -> schedulerVm.setCustomDateRange(start, end) }
    )
}

@Composable
fun SchedulerScreenContent(
    navController: NavHostController,
    state: SchedulerUiState = SchedulerUiState.Loading,
    onNextWeekClick: () -> Unit = {},
    onPreviousWeekClick: () -> Unit = {},
    onCurrentWeekClick: () -> Unit = {},
    onToggleViewClick: () -> Unit = {},
    onCustomDateRangeSelected: (Long, Long) -> Unit = { _, _ -> }
) {
    when (state) {
        is SchedulerUiState.Success -> {
            var showDateRangePicker by remember { mutableStateOf(false) }

            if (showDateRangePicker) {
                BioMedDateRangeSelector(
                    onConfirm = { start, end ->
                        onCustomDateRangeSelected(start, end)
                        showDateRangePicker = false
                    },
                    onCancel = {
                        showDateRangePicker = false
                    }
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
                        withActionButton = true,
                        actionButtonIcon = R.drawable.add,
                        isActionButtonText = false,
                        selectedPage = "Scheduler",
                        onInventoryClick = { navController.navigate(Screen.Inventory.route) },
                        onReportsClick = { navController.navigate(Screen.Reports.route) },
                        onDashboardClick = { navController.navigate(Screen.Dashboard.route) },
                        onActionButtonClick = { navController.navigate(Screen.ScheduleMaintenance.route) },
                        modifier = Modifier.padding(horizontal = 15.dp),
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

                            Text(
                                text = state.weekRangeText,
                                style = MaterialTheme.typography.labelMedium,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 8.dp)
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
                            BioMedViewToggle(
                                selectedView = if (state.isListView) "List" else "Week",
                                onViewChange = { onToggleViewClick() }
                            )
                        }
                    }

                    item {
                        val selectedWeekItem = when {
                            state.isCustomRangeActive -> "Custom Range"
                            state.currentWeekOffset > 0 -> "Next Week"
                            state.currentWeekOffset < 0 -> "Previous Week"
                            else -> "Current Week"
                        }

                        val selectorItems = if (state.isCustomRangeActive) {
                            listOf("Previous Week", "Current Week", "Next Week", "Custom Range")
                        } else {
                            listOf("Previous Week", "Current Week", "Next Week")
                        }

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
                                items = selectorItems,
                                selectedItem = selectedWeekItem,
                                onItemSelected = { item ->
                                    when (item) {
                                        "Previous Week" -> onPreviousWeekClick()
                                        "Next Week" -> onNextWeekClick()
                                        "Current Week" -> onCurrentWeekClick()
                                    }
                                }
                            )

                            BioMedButton(
                                text = "Filter",
                                withIcon = true,
                                icon = R.drawable.filter,
                                onClick = {
                                    showDateRangePicker = true
                                },
                                modifier = Modifier
                            )
                        }
                    }

                    if (!state.isListView) {
                        item {
                            BioMedOverdueOverviewCard(
                                modifier = Modifier.padding(15.dp),
                                overdueCount = state.overdueTasks.size,
                                onViewAllClick = { onToggleViewClick() }
                            )
                        }
                    }

                    if (!state.isListView) {
                        state.tasksByDay.forEach { (timestamp, tasks) ->
                            item {
                                BioMedScheduleDayCard(
                                    modifier = Modifier.padding(
                                        start = 15.dp,
                                        end = 15.dp,
                                        bottom = 15.dp
                                    ),
                                    dayName = timestamp.toDayName(),
                                    dateDisplay = timestamp.toDayMonthString(),
                                    tasks = tasks,
                                    onTaskClick = {
                                        navController.navigate(Screen.LogMaintenance.createRoute(it.equipmentId))
                                    }
                                )
                            }
                        }
                    }

                    if (state.isListView) {
                        item {
                            BioMedOverdueExpandedSection(
                                modifier = Modifier.padding(15.dp),
                                overdueTasks = state.overdueTasks,
                                onTaskClick = {
                                    navController.navigate(Screen.LogMaintenance.createRoute(it.equipmentId))
                                }
                            )
                        }
                    }

                    if (state.isListView) {
                        state.allUpcomingTasks.forEach { task ->
                            item {
                                BioMedTaskCard(
                                    task = task,
                                    modifier = Modifier.padding(15.dp),
                                    onCardClick = {
                                        navController.navigate(Screen.LogMaintenance.createRoute(task.equipmentId))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        is SchedulerUiState.Loading -> {
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
                        actionButtonIcon = R.drawable.add,
                        selectedPage = "Scheduler",
                        onInventoryClick = { navController.navigate(Screen.Inventory.route) },
                        onReportsClick = { navController.navigate(Screen.Reports.route) },
                        onDashboardClick = { navController.navigate(Screen.Dashboard.route) },
                        onActionButtonClick = { navController.navigate(Screen.ScheduleMaintenance.route) },
                        modifier = Modifier.padding(horizontal = 15.dp),
                    )
                },
                floatingActionButtonPosition = FabPosition.Center,
                modifier = Modifier.statusBarsPadding()
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        is SchedulerUiState.Error -> {
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
                        actionButtonIcon = R.drawable.add,
                        selectedPage = "Scheduler",
                        onInventoryClick = { navController.navigate(Screen.Inventory.route) },
                        onReportsClick = { navController.navigate(Screen.Reports.route) },
                        onDashboardClick = { navController.navigate(Screen.Dashboard.route) },
                        onActionButtonClick = { navController.navigate(Screen.ScheduleMaintenance.route) },
                        modifier = Modifier.padding(horizontal = 15.dp),
                    )
                },
                floatingActionButtonPosition = FabPosition.Center,
                modifier = Modifier.statusBarsPadding()
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier
                            .padding(10.dp)
                            .height(130.dp)
                            .width(350.dp),
                        shape = RoundedCornerShape(25.dp),
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = state.message,
                                style = MaterialTheme.typography.titleLarge,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.error
                            )

                            BioMedButton(
                                text = "Retry",
                                onClick = { onCurrentWeekClick() },
                                customColor = MaterialTheme.colorScheme.error,
                                customTextColor = MaterialTheme.colorScheme.onError,
                                customTextSize = 16,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
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
        SchedulerScreenContent(
            navController = NavHostController(LocalContext.current),
            state = SchedulerUiState.Success(
                tasksByDay = emptyMap(),
                overdueTasks = emptyList(),
                allUpcomingTasks = emptyList(),
                currentWeekOffset = 0,
                isListView = false,
                weekRangeText = "28 Mar - 31 Mar",
                isCustomRangeActive = false
            )
        )
    }
}

@Preview(device = "id:pixel_9", showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun SchedulerScreenDArkPreview() {
    BioMedTheme {
        SchedulerScreenContent(
            navController = NavHostController(LocalContext.current),
            state = SchedulerUiState.Success(
                tasksByDay = emptyMap(),
                overdueTasks = emptyList(),
                allUpcomingTasks = emptyList(),
                currentWeekOffset = 0,
                isListView = false,
                weekRangeText = "28 Mar - 31 Mar",
                isCustomRangeActive = false
            )
        )
    }
}