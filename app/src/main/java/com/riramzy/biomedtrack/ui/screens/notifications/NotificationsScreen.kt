package com.riramzy.biomedtrack.ui.screens.notifications

import android.content.res.Configuration
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.components.custom.BioMedHorizontalSelector
import com.riramzy.biomedtrack.ui.components.custom.BioMedTopAppBar
import com.riramzy.biomedtrack.ui.components.notifications.BioMedNotificationsCard
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.Screen

@Composable
fun NotificationsScreen(
    navController: NavHostController,
    notificationsVm: NotificationsVm = hiltViewModel()
) {
    val state by notificationsVm.uiState.collectAsStateWithLifecycle()

    NotificationsScreenContent(
        navController = navController,
        state = state,
        onAction = notificationsVm::onAction
    )
}

@Composable
fun NotificationsScreenContent(
    navController: NavHostController,
    state: NotificationsUiState = NotificationsUiState.Success(),
    onAction: (NotificationsAction) -> Unit = {}
) {
    when(state) {
        is NotificationsUiState.Error -> {
            Scaffold(
                topBar = {
                    BioMedTopAppBar(
                        modifier = Modifier.padding(
                            top = 10.dp
                        )
                    )
                },
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
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.error
                            )

                            BioMedButton(
                                text = "Retry",
                                onClick = { onAction(NotificationsAction.Refresh) },
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
        is NotificationsUiState.Loading -> {
            Scaffold(
                topBar = {
                    BioMedTopAppBar(
                        modifier = Modifier.padding(
                            top = 10.dp
                        )
                    )
                },
                modifier = Modifier.statusBarsPadding()
            ) { innerPadding ->
                NotificationsShimmer(modifier = Modifier.padding(innerPadding))
            }
        }
        is NotificationsUiState.Success -> {
            Scaffold(
                topBar = {
                    BioMedTopAppBar(
                        modifier = Modifier.padding(
                            top = 10.dp
                        )
                    )
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 15.dp,
                                    bottom = 15.dp,
                                    start = 15.dp,
                                    end = 15.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.Start,
                            ) {
                                Text(
                                    text = "Notifications",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                )

                                Text(
                                    text = "${state.unreadCount} Unread Notifications",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                )
                            }

                            BioMedButton(
                                text = "Mark all as read",
                                withIcon = false,
                                modifier = Modifier
                                    .width(140.dp),
                                onClick = {
                                    onAction(NotificationsAction.MarkAllAsRead)
                                }
                            )
                        }

                    }

                    item {
                        BioMedHorizontalSelector(
                            modifier = Modifier
                                .padding(
                                    bottom = 10.dp,
                                    start = 15.dp,
                                    end = 15.dp
                                )
                                .fillMaxWidth(),
                            items = listOf(
                                "All",
                                "Status Changes",
                                "Maintenance Logs",
                                "Service Reminders"
                            ),
                            selectedItem = state.selectedCategory,
                            onItemSelected = {
                                onAction(NotificationsAction.SelectCategory(it))
                            }
                        )
                    }

                    state.notifications.forEach { (header, items) ->
                        item {
                            Text(
                                text = header,
                                style = MaterialTheme.typography.titleLarge,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 15.dp)
                            )
                        }

                        items(items) { item ->
                            BioMedNotificationsCard(
                                item = item,
                                onCardClick = {
                                    onAction.invoke(NotificationsAction.MarkAsRead(item))
                                    navController.navigate(Screen.EquipmentDetail.createRoute(item.equipmentId))
                                }
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true)
@Composable
fun NotificationsScreenPreview() {
    BioMedTheme {
        NotificationsScreenContent(navController = NavHostController(LocalContext.current))
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun NotificationsScreenDarkPreview() {
    BioMedTheme {
        NotificationsScreenContent(navController = NavHostController(LocalContext.current))
    }
}