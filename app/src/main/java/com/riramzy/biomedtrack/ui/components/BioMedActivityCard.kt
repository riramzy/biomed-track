package com.riramzy.biomedtrack.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.riramzy.biomedtrack.utils.ActivityType
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.utils.getLocalizedDepartmentName

@Composable
fun BioMedActivityCard(
    modifier: Modifier = Modifier,
    type: ActivityType = ActivityType.STATUS_CHANGE,
    status: EquipmentStatus = EquipmentStatus.SERVICE,
    previousStatus: EquipmentStatus? = EquipmentStatus.ONLINE,
    name: String = "Fresenius",
    model: String = "4008S",
    serialNumber: String = "4545885N70",
    department: String = "Dialysis Unit",
    changedBy: String = "Ramsey Ibrahim",
    taskAssigneeName: String? = null,
    relativeTime: String = "12m ago",
    dateString: String = "13/3/2026",
    onClick: () -> Unit = {}
) {
    val displayTitle = when (type) {
        ActivityType.MAINTENANCE_LOG -> stringResource(R.string.activity_maintenance_logged)
        ActivityType.TASK_ASSIGNED -> {
            if (taskAssigneeName != null) {
                stringResource(R.string.activity_task_assigned_to, taskAssigneeName)
            } else {
                stringResource(R.string.activity_task_assigned)
            }
        }

        ActivityType.STATUS_CHANGE -> {
            val statusNewStr = when (status) {
                EquipmentStatus.ONLINE -> stringResource(R.string.status_online)
                EquipmentStatus.DOWN -> stringResource(R.string.status_down)
                EquipmentStatus.SERVICE -> stringResource(R.string.status_service)
            }
            if (previousStatus != null) {
                val statusOldStr = when (previousStatus) {
                    EquipmentStatus.ONLINE -> stringResource(R.string.status_online)
                    EquipmentStatus.DOWN -> stringResource(R.string.status_down)
                    EquipmentStatus.SERVICE -> stringResource(R.string.status_service)
                }
                stringResource(R.string.activity_status_changed_from_to, statusOldStr, statusNewStr)
            } else {
                stringResource(R.string.activity_status_changed, statusNewStr)
            }
        }
    }
    val iconBoxColor = when (type) {
        ActivityType.MAINTENANCE_LOG -> if (isSystemInDarkTheme()) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.secondary
        ActivityType.TASK_ASSIGNED -> if (isSystemInDarkTheme()) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.tertiary
        ActivityType.STATUS_CHANGE -> when (status) {
            EquipmentStatus.SERVICE -> MaterialTheme.indicatorColors.yellow
            EquipmentStatus.ONLINE -> MaterialTheme.indicatorColors.green
            EquipmentStatus.DOWN -> MaterialTheme.indicatorColors.red
        }
    }
    val statusImage = when (type) {
        ActivityType.MAINTENANCE_LOG -> painterResource(R.drawable.activity_log)
        ActivityType.TASK_ASSIGNED -> painterResource(R.drawable.add)
        ActivityType.STATUS_CHANGE -> when (status) {
            EquipmentStatus.ONLINE -> painterResource(id = R.drawable.activity_online)
            EquipmentStatus.DOWN -> painterResource(R.drawable.activity_down)
            EquipmentStatus.SERVICE -> painterResource(R.drawable.activity_service)
        }
    }
    val statusName = when (type) {
        ActivityType.MAINTENANCE_LOG -> "LOG"
        ActivityType.TASK_ASSIGNED -> "ASSIGNED"
        ActivityType.STATUS_CHANGE -> status.name
    }

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
        ),
        onClick = onClick
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 15.dp,
                        end = 15.dp,
                        top = 10.dp,
                        bottom = 10.dp
                    ),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .size(34.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = iconBoxColor
                    )
                ) {
                    Icon(
                        painter = statusImage,
                        contentDescription = null,
                        tint = Color.White,
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
                        text = "$name $model - $serialNumber",
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
                        changeable = false
                    )

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = stringResource(R.string.label_department),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Light,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Text(
                            text = getLocalizedDepartmentName(department),
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

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.padding(top = 5.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.label_by),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Light,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Text(
                            text = changedBy,
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
                    Text(
                        text = relativeTime,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    Text(
                        text = dateString,
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

@Preview(device = "spec:width=411dp,height=891dp", showBackground = true,
    backgroundColor = 0xFFFFFFFF, locale = "ar"
)
@Composable
fun BioMedActivityCardPreview() {
    BioMedTheme {
        BioMedActivityCard(
            type = ActivityType.TASK_ASSIGNED,
            status = EquipmentStatus.SERVICE
        )
    }
}

@Preview(device = "spec:width=411dp,height=891dp", showBackground = true, showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF000000
)
@Composable
fun BioMedActivityCardDarkPreview() {
    BioMedTheme {
        BioMedActivityCard(
            type = ActivityType.STATUS_CHANGE,
            status = EquipmentStatus.SERVICE
        )
    }
}