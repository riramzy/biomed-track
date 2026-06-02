package com.riramzy.biomedtrack.ui.components.schedule

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.domain.model.ChecklistItem
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Task
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.TaskStatus

@Composable
fun BioMedScheduleDayCard(
    modifier: Modifier = Modifier,
    dayName: String,
    dateDisplay: String,
    tasks: List<Task>,
    onTaskClick: (Task) -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(15.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = dayName,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    } else {
                        Color.Black
                    }
                )

                Text(
                    text = dateDisplay,
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    } else {
                        Color.Black
                    }
                )
            }

            Spacer(Modifier.height(15.dp))

            tasks.ifEmpty {
                Text(
                    text = "No maintenance",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.primary.copy(0.6f)
                    } else {
                        MaterialTheme.colorScheme.primary.copy(0.6f)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }

            tasks.forEach { task ->
                BioMedTaskCard(
                    task = task,
                    modifier = Modifier.padding(bottom = 10.dp),
                    onCardClick = { onTaskClick(task) }
                )
            }
        }
    }
}

@Preview(
    device = "id:pixel_9", showSystemUi = false, showBackground = true
)
@Composable
fun BioMedScheduleDayCardPreview() {
    val mockTasks = listOf(
        Task(
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
            scheduledChecklist = listOf(
                ChecklistItem("1", "Clean filters", true),
                ChecklistItem("2", "Calibrate sensors", false),
                ChecklistItem("3", "Leak test", false)
            )
        )
    )
    BioMedTheme {
        BioMedScheduleDayCard(
            dayName = "SAT",
            dateDisplay = "28 Mar",
            tasks = mockTasks
        )
    }
}

@Preview(
    device = "id:pixel_9", showSystemUi = false, showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedScheduleDayCardDarkPreview() {
    val mockTasks = listOf(
        Task(
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
            scheduledChecklist = listOf(
                ChecklistItem("1", "Clean filters", false),
                ChecklistItem("2", "Calibrate sensors", false),
                ChecklistItem("3", "Leak test", false)
            )
        )
    )
    BioMedTheme {
        BioMedScheduleDayCard(
            dayName = "SAT",
            dateDisplay = "28 Mar",
            tasks = mockTasks
        )
    }
}