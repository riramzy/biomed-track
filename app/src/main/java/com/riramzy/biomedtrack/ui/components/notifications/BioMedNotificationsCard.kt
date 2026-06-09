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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.riramzy.biomedtrack.utils.getLocalizedDepartmentName

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
        equipmentStatus = EquipmentStatus.SERVICE
    ),
    onCardClick: () -> Unit = {}
) {
    val context = LocalContext.current

    val displayTitle = when (item.type) {
        ActivityType.STATUS_CHANGE -> {
            val statusNewStr = when (item.equipmentStatus) {
                EquipmentStatus.ONLINE -> stringResource(R.string.status_online)
                EquipmentStatus.DOWN -> stringResource(R.string.status_down)
                EquipmentStatus.SERVICE -> stringResource(R.string.status_service)
                else -> ""
            }
            if (item.previousStatus != null) {
                val statusOldStr = when (item.previousStatus) {
                    EquipmentStatus.ONLINE -> stringResource(R.string.status_online)
                    EquipmentStatus.DOWN -> stringResource(R.string.status_down)
                    EquipmentStatus.SERVICE -> stringResource(R.string.status_service)
                }
                stringResource(R.string.activity_status_changed_from_to, statusOldStr, statusNewStr)
            } else {
                stringResource(R.string.activity_status_changed, statusNewStr)
            }
        }

        ActivityType.MAINTENANCE_LOG -> stringResource(R.string.activity_maintenance_logged)
        ActivityType.TASK_ASSIGNED -> {
            if (item.taskAssigneeName != null) {
                stringResource(R.string.activity_task_assigned_to, item.taskAssigneeName)
            } else {
                item.title
            }
        }
    }
    val iconBoxColor = when (item.type) {
        ActivityType.STATUS_CHANGE ->
            when (item.equipmentStatus) {
                EquipmentStatus.ONLINE -> MaterialTheme.indicatorColors.green
                EquipmentStatus.SERVICE -> MaterialTheme.indicatorColors.yellow
                EquipmentStatus.DOWN -> MaterialTheme.indicatorColors.red
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
    val statusImage = when (item.type) {
        ActivityType.STATUS_CHANGE ->
            when (item.equipmentStatus) {
                EquipmentStatus.ONLINE -> painterResource(id = R.drawable.activity_online)
                EquipmentStatus.SERVICE -> painterResource(R.drawable.activity_service)
                EquipmentStatus.DOWN -> painterResource(R.drawable.activity_down)
                else -> painterResource(R.drawable.activity_down)
            }

        ActivityType.MAINTENANCE_LOG -> painterResource(R.drawable.activity_log)
        ActivityType.TASK_ASSIGNED -> painterResource(R.drawable.profile)
    }
    val statusName = when (item.type.name) {
        ActivityType.STATUS_CHANGE.name -> {
            when (item.equipmentStatus) {
                EquipmentStatus.ONLINE -> EquipmentStatus.ONLINE.name
                EquipmentStatus.SERVICE -> EquipmentStatus.SERVICE.name
                EquipmentStatus.DOWN -> EquipmentStatus.DOWN.name
                else -> ""
            }
        }

        ActivityType.TASK_ASSIGNED.name -> "ASSIGNED"
        ActivityType.MAINTENANCE_LOG.name -> "LOG"
        else -> ""
    }

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
                            color = iconBoxColor
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = statusImage,
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
                        text = displayTitle,
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
                        status = statusName,
                        changeable = false,
                        onStatusClicked = {}
                    )

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        if (item.type == ActivityType.TASK_ASSIGNED) {
                            Text(
                                text = stringResource(
                                    R.string.task_due_date_format,
                                    item.dueDate ?: ""
                                ),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSystemInDarkTheme()) Color.White else Color.Black
                            )
                        } else {
                            Text(
                                text = getLocalizedDepartmentName(item.departmentName),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSystemInDarkTheme()) Color.White else Color.Black
                            )
                        }

                        Text(
                            text = stringResource(R.string.label_by_name, item.technicianName),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSystemInDarkTheme()) Color.White else Color.Black
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
                            text = item.timestamp.toRelativeTime(context),
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

@Preview(showBackground = true, showSystemUi = false, device = "id:pixel_9", locale = "ar")
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
                type = ActivityType.STATUS_CHANGE,
                timestamp = System.currentTimeMillis(),
                dueDate = System.currentTimeMillis().toDateString(),
                equipmentStatus = EquipmentStatus.ONLINE
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
                equipmentStatus = EquipmentStatus.ONLINE
            )
        )
    }
}