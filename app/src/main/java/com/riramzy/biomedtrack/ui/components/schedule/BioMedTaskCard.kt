package com.riramzy.biomedtrack.ui.components.schedule

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.riramzy.biomedtrack.domain.model.ChecklistItem
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Task
import com.riramzy.biomedtrack.ui.components.user.BioMedAvatar
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.ui.theme.indicatorColors
import com.riramzy.biomedtrack.utils.TaskStatus
import com.riramzy.biomedtrack.utils.getLocalizedDepartmentName

@Composable
fun BioMedTaskCard(
    task: Task,
    modifier: Modifier = Modifier,
    onCardClick: () -> Unit = {}
) {
    val isDark = isSystemInDarkTheme()
    val (statusText, statusBg, statusTextCol) = when(task.status) {
        TaskStatus.PENDING -> Triple(
            stringResource(R.string.status_pending),
            if (isDark) MaterialTheme.indicatorColors.yellow.copy(alpha = 0.15f) else MaterialTheme.indicatorColors.yellow.copy(
                0.15f
            ),
            if (isDark) MaterialTheme.indicatorColors.yellow else MaterialTheme.indicatorColors.yellow
        )
        TaskStatus.IN_PROGRESS -> Triple(
            stringResource(R.string.status_in_progress),
            if (isDark) MaterialTheme.colorScheme.primaryContainer.copy(0.7f) else MaterialTheme.colorScheme.primaryContainer.copy(
                0.7f
            ),
            if (isDark) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary
        )
        TaskStatus.DONE -> Triple(
            stringResource(R.string.status_done),
            if (isDark) MaterialTheme.indicatorColors.green.copy(alpha = 0.15f) else MaterialTheme.indicatorColors.green.copy(
                0.15f
            ),
            if (isDark) MaterialTheme.indicatorColors.green else MaterialTheme.indicatorColors.green
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
        ),
        onClick = onCardClick
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "${task.equipmentName} ${task.equipmentModel}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                        color = if (isDark) Color.White else Color.Black
                    )
                    
                    Spacer(modifier = Modifier.height(2.dp))
                    
                    Text(
                        text = stringResource(R.string.task_serial_format, task.equipmentSerial),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isDark) Color.White.copy(alpha = 0.6f) else Color.Black.copy(
                            alpha = 0.6f
                        )
                    )
                }
                
                Box(
                    modifier = Modifier
                        .wrapContentHeight()
                        .wrapContentWidth()
                        .heightIn(min = 25.dp)
                        .widthIn(min = 100.dp)
                        .clip(RoundedCornerShape(25.dp))
                        .background(statusBg)
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = statusText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusTextCol
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(25.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(0.5f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = getLocalizedDepartmentName(task.department.name),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        BioMedAvatar(
                            name = task.assignedToName,
                            size = 25.dp,
                        )
                        
                        Text(
                            text = task.assignedToName,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Normal,
                            color = if (isDark) Color.White.copy(alpha = 0.9f) else Color.Black.copy(
                                alpha = 0.9f
                            )
                        )
                    }
                }

                val totalSteps = task.scheduledChecklist.size
                val checkedSteps = task.scheduledChecklist.count { it.isChecked }
                if (totalSteps > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Image(
                            painter = painterResource(
                                id = if (checkedSteps == totalSteps) R.drawable.checked else R.drawable.task
                            ),
                            contentDescription = "Checklist Progress Icon",
                            modifier = Modifier.size(12.dp)
                        )
                        
                        Text(
                            text = stringResource(
                                R.string.task_steps_format,
                                checkedSteps,
                                totalSteps
                            ),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (checkedSteps == totalSteps) {
                                if (isDark) Color(0xFF81C784) else Color(0xFF2E7D32)
                            } else {
                                if (isDark) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.secondary
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    device = "id:pixel_9", showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFFFFFFFF, locale = "ar"
)
@Composable
fun BioMedTaskCardPreview() {
    val mockTask = Task(
        id = "1",
        equipmentId = "eq1",
        equipmentName = "Fresenius",
        equipmentModel = "4008S",
        equipmentSerial = "12345678",
        department = Department(id = "1", name = "Dialysis Unit", totalEquipment = 20),
        assignedTo = "tech1",
        assignedToName = "John Doe",
        assignedBy = "sup1",
        dueDate = System.currentTimeMillis(),
        status = TaskStatus.IN_PROGRESS,
        notes = "Check pressure sensors and replace faulty filters",
        scheduledChecklist = listOf(
            ChecklistItem("1", "Clean filters", false),
            ChecklistItem("2", "Calibrate sensors", false),
            ChecklistItem("3", "Leak test", false)
        )
    )
    BioMedTheme {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
        ) {
            BioMedTaskCard(
                task = mockTask,
                modifier = Modifier.padding(15.dp)
            )
        }
    }
}

@Preview(
    device = "id:pixel_9", showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedTaskCardDarkPreview() {
    val mockTask = Task(
        id = "2",
        equipmentId = "eq2",
        equipmentName = "Philips",
        equipmentModel = "IntelliVue MX450",
        equipmentSerial = "SN87654321",
        department = Department(id = "2", name = "ICU", totalEquipment = 15),
        assignedTo = "tech2",
        assignedToName = "Sarah Connor",
        assignedBy = "sup1",
        dueDate = System.currentTimeMillis(),
        status = TaskStatus.IN_PROGRESS,
        notes = "Calibrate blood pressure modules and check battery backup",
        scheduledChecklist = listOf(
            ChecklistItem("1", "Clean modules", true),
            ChecklistItem("2", "Calibrate NIBP", false),
            ChecklistItem("3", "Battery load test", true)
        )
    )
    BioMedTheme {
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
        ) {
            BioMedTaskCard(
                task = mockTask,
                modifier = Modifier.padding(15.dp)
            )
        }
    }
}
