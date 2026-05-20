package com.riramzy.biomedtrack.ui.components.schedule

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Task
import com.riramzy.biomedtrack.ui.components.equipment.BioMedOverdueEquipmentCard
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.ui.theme.indicatorColors
import com.riramzy.biomedtrack.utils.TaskStatus

@Composable
fun BioMedOverdueExpandedSection(
    modifier: Modifier = Modifier,
    overdueTasks: List<Task>,
    onTaskClick: (Task) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) {
                MaterialTheme.indicatorColors.red.copy(0.6f)
            } else {
                MaterialTheme.indicatorColors.red.copy(0.2f)
            }
        )
    ) {
        Column(
            modifier = Modifier
                .padding(15.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Overdue Maintenance (${overdueTasks.size})",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSystemInDarkTheme()) {
                        Color.White
                    } else {
                        Color(0xFF980000)
                    },
                    modifier = Modifier.padding(bottom = 15.dp)
                )

                overdueTasks.forEach { task ->
                    BioMedOverdueEquipmentCard(
                        task = task,
                        onCardClick = { onTaskClick(task) },
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }
            }
        }
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true)
@Composable
fun BioMedOverdueExpandedSectionPreview() {
    BioMedTheme {
        BioMedOverdueExpandedSection(
            overdueTasks = listOf(
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
                    scheduledChecklist = emptyList(),
                    readBy = emptyList()
                )
            ),
            onTaskClick = {}
        )
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF000000
)
@Composable
fun BioMedOverdueExpandedSectionDarkPreview() {
    BioMedTheme {
        BioMedOverdueExpandedSection(
            overdueTasks = listOf(
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
                    scheduledChecklist = emptyList(),
                    readBy = emptyList()
                )
            ),
            onTaskClick = {}
        )
    }
}