package com.riramzy.biomedtrack.ui.components.equipment

import android.content.res.Configuration
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Task
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.ui.theme.indicatorColors
import com.riramzy.biomedtrack.utils.TaskStatus
import com.riramzy.biomedtrack.utils.Timestamps.toDateString
import com.riramzy.biomedtrack.utils.getLocalizedDepartmentName

@Composable
fun BioMedOverdueEquipmentCard(
    modifier: Modifier = Modifier,
    task: Task,
    onCardClick: () -> Unit
) {
    val isDarkTheme = isSystemInDarkTheme()
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkTheme) {
                Color.Black.copy(0.2f)
            } else {
                Color.White.copy(0.2f)
            }
        ),
        onClick = onCardClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .padding(end = 10.dp)
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(
                        when (task.status) {
                            TaskStatus.PENDING -> MaterialTheme.indicatorColors.yellow
                            TaskStatus.IN_PROGRESS -> MaterialTheme.indicatorColors.red
                            TaskStatus.DONE -> MaterialTheme.indicatorColors.green
                        }
                    ),
            ) {
                Icon(
                    painter = when(task.status) {
                        TaskStatus.PENDING -> painterResource(R.drawable.activity_service)
                        TaskStatus.IN_PROGRESS -> painterResource(R.drawable.activity_down)
                        TaskStatus.DONE -> painterResource(R.drawable.activity_online)
                    },
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(0.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${task.equipmentName} ${task.equipmentModel}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isDarkTheme) {
                        Color.White
                    } else {
                        Color.Black
                    }
                )

                Text(
                    text = stringResource(R.string.task_serial_format, task.equipmentSerial),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isDarkTheme) {
                        Color.White
                    } else {
                        Color.Black
                    }
                )

                Text(
                    text = getLocalizedDepartmentName(task.department.name),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isDarkTheme) {
                        Color.White
                    } else {
                        Color.Black
                    }
                )
            }

            Column(
                modifier = Modifier
                    .padding(end = 15.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = task.dueDate.toDateString(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelSmall,
                    color = when(task.status) {
                        TaskStatus.PENDING -> MaterialTheme.indicatorColors.yellow
                        TaskStatus.IN_PROGRESS -> MaterialTheme.indicatorColors.red
                        TaskStatus.DONE -> MaterialTheme.indicatorColors.green
                    }
                )

                Text(
                    text = task.assignedToName,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isDarkTheme) {
                        Color.White
                    } else {
                        Color.Black
                    }
                )
            }
        }
    }
}

@Preview(device = "id:pixel_9", showBackground = true, locale = "ar")
@Composable
fun BioMedOverdueEquipmentCardPreview() {
    BioMedTheme {
        BioMedOverdueEquipmentCard(
            task = Task(
                id = "1",
                equipmentId = "eq1",
                equipmentName = "Fresenius",
                equipmentModel = "4008S",
                equipmentSerial = "SN12345678",
                department = Department(id = "1", name = "Dialysis Unit", totalEquipment = 20),
                assignedTo = "tech1",
                assignedToName = "John Doe",
                assignedBy = "sup1",
                dueDate = System.currentTimeMillis(),
                status = TaskStatus.IN_PROGRESS,
                notes = "Check pressure sensors and replace faulty filters",
                scheduledChecklist = emptyList(),
                readBy = emptyList()
            ),
            onCardClick = {}
        )
    }
}

@Preview(device = "id:pixel_9", showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showSystemUi = false, backgroundColor = 0xFFB20000
)
@Composable
fun BioMedOverdueEquipmentCardDarkPreview() {
    BioMedTheme {
        BioMedOverdueEquipmentCard(
            task = Task(
                id = "1",
                equipmentId = "eq1",
                equipmentName = "Fresenius",
                equipmentModel = "4008S",
                equipmentSerial = "SN12345678",
                department = Department(id = "1", name = "Dialysis Unit", totalEquipment = 20),
                assignedTo = "tech1",
                assignedToName = "John Doe",
                assignedBy = "sup1",
                dueDate = System.currentTimeMillis(),
                status = TaskStatus.PENDING,
                notes = "Check pressure sensors and replace faulty filters",
                scheduledChecklist = emptyList(),
                readBy = emptyList()
            ),
            onCardClick = {}
        )
    }
}