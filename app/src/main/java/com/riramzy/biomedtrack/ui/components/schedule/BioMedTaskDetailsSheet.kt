package com.riramzy.biomedtrack.ui.components.schedule

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.domain.model.ChecklistItem
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Task
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.TaskStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BioMedTaskDetailsSheet(
    task: Task,
    onStartTaskClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(15.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "${task.equipmentName} ${task.equipmentModel}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 22.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = "Serial Number: ${task.equipmentSerial}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium,
        )

        Spacer(modifier = Modifier.height(15.dp))

        if (task.notes.isNotBlank()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
                ),
                shape = RoundedCornerShape(25.dp)
            ) {
                Column(
                    modifier = Modifier.padding(15.dp)
                ) {
                    Text(
                        text = "Supervisor Notes",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = task.notes,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Configured Safety Checklist (${task.scheduledChecklist.size} steps)",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(task.scheduledChecklist) { step ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(size = 8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = step.label,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        val buttonText = when (task.status) {
            TaskStatus.PENDING -> "Start Service"
            TaskStatus.IN_PROGRESS -> "Resume Service"
            TaskStatus.DONE -> "Completed"
        }

        val isButtonEnabled = task.status != TaskStatus.DONE

        BioMedButton(
            text = buttonText,
            onClick = onStartTaskClick,
            isEnabled = isButtonEnabled,
            customColor = MaterialTheme.colorScheme.primary,
            customTextColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true)
@Composable
fun BioMedTaskDetailsSheetPreview() {
    BioMedTheme {
        BioMedTaskDetailsSheet(
            task = Task(
                id = "1212",
                equipmentId = "212",
                equipmentName = "Fresenius",
                equipmentModel = "4008S Classic",
                equipmentSerial = "213323943",
                department = Department(
                    id = "112",
                    name = "Dialysis Unit",
                    downEquipment = 1,
                    dueServiceEquipment = 23,
                    totalEquipment = 35
                ),
                assignedTo = "1",
                assignedBy = "Ramzy Ibrahim",
                assignedToName = "Ibrahim Foad",
                dueDate = 22/6/2026L,
                scheduledChecklist = listOf(
                    ChecklistItem("1", "Calibration", false),
                    ChecklistItem("1", "Calibration", false),
                    ChecklistItem("1", "Calibration", false),
                    ChecklistItem("1", "Calibration", false),
                    ChecklistItem("1", "Calibration", false)
                ),
                status = TaskStatus.PENDING,
                notes = "ssssssssssssssssssssssssssssssssssssssssss"
            )
        )
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF000000,
)
@Composable
fun BioMedTaskDetailsSheetDarkPreview() {
    BioMedTheme {
        BioMedTaskDetailsSheet(
            task = Task(
                id = "1212",
                equipmentId = "212",
                equipmentName = "Fresenius",
                equipmentModel = "4008S Classic",
                equipmentSerial = "213323943",
                department = Department(
                    id = "112",
                    name = "Dialysis Unit",
                    downEquipment = 1,
                    dueServiceEquipment = 23,
                    totalEquipment = 35
                ),
                assignedTo = "1",
                assignedBy = "Ramzy Ibrahim",
                assignedToName = "Ibrahim Foad",
                dueDate = 22/6/2026L,
                scheduledChecklist = listOf(
                    ChecklistItem("1", "Calibration", false),
                    ChecklistItem("1", "Calibration", false),
                    ChecklistItem("1", "Calibration", false),
                    ChecklistItem("1", "Calibration", false),
                    ChecklistItem("1", "Calibration", false)
                ),
                status = TaskStatus.PENDING,
                notes = "sssssssssssssssssssssssssssssss"
            )
        )
    }
}