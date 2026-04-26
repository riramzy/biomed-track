package com.riramzy.biomedtrack.ui.components.notifications

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.components.custom.BioMedStatusIndicator
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.ui.theme.indicatorColors
import com.riramzy.biomedtrack.utils.ActivityItem
import com.riramzy.biomedtrack.utils.ActivityType
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.utils.Timestamps.toDateString
import com.riramzy.biomedtrack.utils.Timestamps.toRelativeTime

@Composable
fun BioMedNotificationsCard(
    modifier: Modifier = Modifier,
    item: ActivityItem = ActivityItem(
        id = "1",
        title = "Task Assigned to You",
        equipmentId = "1",
        equipmentName = "Fresenius",
        equipmentModel = "4008S",
        equipmentSerial = "434344050",
        departmentName = "Dialysis Unit",
        technicianName = "Ramzy Habel",
        type = ActivityType.TASK_ASSIGNED,
        timestamp = 1/1/2026,
        dueDate = "28/4/2026",
        status = EquipmentStatus.SERVICE.name
    ),
    onCardClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isRead) {
                MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
            } else {
                MaterialTheme.colorScheme.primaryContainer
            }
        ),
        onClick = { onCardClick() }
    ) {
        Column(
            modifier = Modifier.padding(15.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(
                            shape = CircleShape,
                            color = when(item.type) {
                                ActivityType.STATUS_CHANGE ->
                                    when(item.status) {
                                        EquipmentStatus.ONLINE.name -> MaterialTheme.indicatorColors.green
                                        EquipmentStatus.SERVICE.name -> MaterialTheme.indicatorColors.yellow
                                        EquipmentStatus.DOWN.name -> MaterialTheme.indicatorColors.red
                                        else -> MaterialTheme.indicatorColors.green
                                    }
                                ActivityType.MAINTENANCE_LOG -> if (isSystemInDarkTheme()) {
                                    MaterialTheme.colorScheme.secondaryContainer
                                } else {
                                    MaterialTheme.colorScheme.secondary
                                }
                                ActivityType.TASK_ASSIGNED -> if (isSystemInDarkTheme()) {
                                    MaterialTheme.colorScheme.tertiaryContainer
                                } else {
                                    MaterialTheme.colorScheme.tertiary
                                }
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = when(item.type) {
                            ActivityType.STATUS_CHANGE ->
                                when(item.status) {
                                    EquipmentStatus.ONLINE.name -> painterResource(id = R.drawable.activity_online)
                                    EquipmentStatus.SERVICE.name -> painterResource(R.drawable.activity_service)
                                    EquipmentStatus.DOWN.name -> painterResource(R.drawable.activity_down)
                                    else -> painterResource(R.drawable.activity_down)
                                }
                            ActivityType.MAINTENANCE_LOG -> painterResource(R.drawable.activity_log)
                            ActivityType.TASK_ASSIGNED -> painterResource(R.drawable.profile)
                        },
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(2f)
                        .padding(start = 15.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = item.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSystemInDarkTheme()) {
                            Color.White
                        } else {
                            Color.Black
                        }
                    )

                    Text(
                        text = "${item.equipmentName} ${item.equipmentModel} - ${item.equipmentSerial}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSystemInDarkTheme()) {
                            Color.White
                        } else {
                            Color.Black
                        }
                    )

                    BioMedStatusIndicator(
                        modifier = Modifier.padding(top = 5.dp),
                        status = when (item.type.name) {
                            ActivityType.STATUS_CHANGE.name -> {
                                when (item.status) {
                                    EquipmentStatus.ONLINE.name -> EquipmentStatus.ONLINE.name
                                    EquipmentStatus.SERVICE.name -> EquipmentStatus.SERVICE.name
                                    EquipmentStatus.DOWN.name -> EquipmentStatus.DOWN.name
                                    else -> ""
                                }
                            }
                            ActivityType.TASK_ASSIGNED.name -> "ASSIGNED"
                            ActivityType.MAINTENANCE_LOG.name -> "LOG"
                            else -> ""
                        },
                        changeable = false
                    )

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.padding(top = 5.dp)
                    ) {
                        if (item.type == ActivityType.TASK_ASSIGNED) {
                            Text(
                                text = "Due: ${item.dueDate}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSystemInDarkTheme()) {
                                    Color.White
                                } else {
                                    Color.Black
                                }
                            )
                        } else {
                            Text(
                                text = item.departmentName,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSystemInDarkTheme()) {
                                    Color.White
                                } else {
                                    Color.Black
                                }
                            )
                        }


                        Text(
                            text = if (item.type == ActivityType.TASK_ASSIGNED) {
                                "Assigned by ${item.technicianName}"
                            } else {
                                "By ${item.technicianName}"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSystemInDarkTheme()) {
                                Color.White
                            } else {
                                Color.Black
                            }
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Top
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = item.timestamp.toRelativeTime(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Normal,
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.End,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        if (!item.isRead) {
                            Box(
                                modifier = Modifier
                                    .padding(start = 5.dp)
                                    .clip(CircleShape)
                                    .size(10.dp)
                                    .background(
                                        if (isSystemInDarkTheme()) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.primary
                                        }
                                    )
                            )
                        }
                    }

                    Text(
                        text = item.timestamp.toDateString(),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = false, device = "id:pixel_9")
@Composable
fun BioMedNotificationsCardPreview() {
    BioMedTheme {
        BioMedNotificationsCard(
            item = ActivityItem(
                id = "1",
                title = "Task Assigned to You",
                equipmentId = "1",
                equipmentName = "Fresenius",
                equipmentModel = "4008S",
                equipmentSerial = "434344050",
                departmentName = "Dialysis Unit",
                technicianName = "Ramzy Habel",
                type = ActivityType.TASK_ASSIGNED,
                timestamp = System.currentTimeMillis(),
                dueDate = System.currentTimeMillis().toDateString(),
                status = EquipmentStatus.ONLINE.name
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = false, device = "id:pixel_9",
    backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedNotificationsCardDarkPreview() {
    BioMedTheme {
        BioMedNotificationsCard(
            item = ActivityItem(
                id = "1",
                title = "Task Assigned to You",
                equipmentId = "1",
                equipmentName = "Fresenius",
                equipmentModel = "4008S",
                equipmentSerial = "434344050",
                departmentName = "Dialysis Unit",
                technicianName = "Ramzy Habel",
                type = ActivityType.TASK_ASSIGNED,
                timestamp = System.currentTimeMillis(),
                dueDate = System.currentTimeMillis().toDateString(),
                status = EquipmentStatus.ONLINE.name
            )
        )
    }
}