package com.riramzy.biomedtrack.ui.components.schedule

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.riramzy.biomedtrack.domain.model.Equipment
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.components.custom.BioMedDateSelector
import com.riramzy.biomedtrack.ui.components.custom.BioMedMultipleItemsSelector
import com.riramzy.biomedtrack.ui.components.custom.BioMedSelector
import com.riramzy.biomedtrack.ui.components.custom.BioMedTextField
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun BioMedScheduleMaintenanceCard(
    modifier: Modifier = Modifier,
    equipmentList: List<Equipment>,
    selectedEquipment: Equipment?,
    onEquipmentSelected: (Equipment) -> Unit,
    dueDate: Long,
    onDateSelected: (Long) -> Unit,
    techniciansList: List<String>,
    selectedTechnicianName: String,
    onTechnicianSelected: (String) -> Unit,
    notes: String,
    onNotesChanged: (String) -> Unit,
    checklist: List<ChecklistItem>,
    onToggleChecklist: (ChecklistItem) -> Unit,
    onAddChecklistItem: (String) -> Unit,
    onRemoveChecklistItem: (ChecklistItem) -> Unit,
    onScheduleClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    val scrollState = rememberScrollState()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.onSecondary
            } else {
                MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
            }
        )
    ) {
        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 15.dp,
                        bottom = 15.dp
                    )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Schedule Maintenance",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSystemInDarkTheme()) {
                                Color.White
                            } else {
                                Color.Black
                            }
                        )

                        Text(
                            text = "Plan and track equipment maintenance schedules",
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 4.dp),
                            color = if (isSystemInDarkTheme()) {
                                MaterialTheme.colorScheme.secondary
                            } else {
                                MaterialTheme.colorScheme.secondary
                            }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                BioMedSelector(
                    title = "Equipment",
                    placeholder = "Select Equipment",
                    items = equipmentList.map { "${it.name} ${it.model} - ${it.serialNumber}" },
                    selectedItem = selectedEquipment?.let { "${it.name} ${it.model} - ${it.serialNumber}" } ?: "",
                    onItemSelected = { eq ->
                        val selectedEquipment = equipmentList.find { equipment ->
                            "${equipment.name} ${equipment.model} - ${equipment.serialNumber}" == eq
                        }

                        selectedEquipment?.let { onEquipmentSelected(it) }
                    },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                BioMedDateSelector(
                    title = "Date",
                    selectedDate = dueDate,
                    onDateSelected = onDateSelected,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                BioMedSelector(
                    title = "Assign To",
                    placeholder = "Select Technician",
                    items = techniciansList,
                    selectedItem = selectedTechnicianName,
                    onItemSelected = onTechnicianSelected,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                BioMedTextField(
                    label = "Additional Notes",
                    value = notes,
                    onValueChange = onNotesChanged,
                    placeholder = "Enter notes here",
                    isNoteCard = true,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                BioMedMultipleItemsSelector(
                    title = "Tasks Required",
                    items = checklist,
                    onToggle = onToggleChecklist,
                    onAddNewItem = onAddChecklistItem,
                    onRemoveItem = onRemoveChecklistItem,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            top = 20.dp
                        ),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BioMedButton(
                        text = "Schedule",
                        customColor = MaterialTheme.colorScheme.primary,
                        customTextColor = MaterialTheme.colorScheme.onPrimary,
                        onClick = onScheduleClick
                    )

                    BioMedButton(
                        text = "Cancel",
                        customColor = MaterialTheme.colorScheme.primaryContainer,
                        customTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        onClick = onCancelClick
                    )
                }
            }
        }
    }
}

@Preview(device = "id:pixel_9", showBackground = true)
@Composable
fun BioMedScheduleMaintenanceCardPreview() {
    BioMedTheme {
        BioMedScheduleMaintenanceCard(
            equipmentList = emptyList(),
            selectedEquipment = null,
            onEquipmentSelected = {},
            dueDate = System.currentTimeMillis(),
            onDateSelected = {},
            techniciansList = emptyList(),
            selectedTechnicianName = "",
            onTechnicianSelected = {},
            notes = "",
            onNotesChanged = {},
            checklist = listOf(
                ChecklistItem(id = "1", label = "Task 1", isChecked = false),
                ChecklistItem(id = "2", label = "Task 2", isChecked = true),
                ChecklistItem(id = "3", label = "Task 3", isChecked = false)
            ),
            onToggleChecklist = {},
            onAddChecklistItem = {},
            onRemoveChecklistItem = {},
            onScheduleClick = {},
            onCancelClick = {}
        )
    }
}

@Preview(device = "id:pixel_9", showBackground = true, backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedScheduleMaintenanceCardDarkPreview() {
    BioMedTheme {
        BioMedScheduleMaintenanceCard(
            equipmentList = emptyList(),
            selectedEquipment = null,
            onEquipmentSelected = {},
            dueDate = System.currentTimeMillis(),
            onDateSelected = {},
            techniciansList = emptyList(),
            selectedTechnicianName = "",
            onTechnicianSelected = {},
            notes = "",
            onNotesChanged = {},
            checklist = listOf(
                ChecklistItem(id = "1", label = "Task 1", isChecked = false),
                ChecklistItem(id = "2", label = "Task 2", isChecked = true),
                ChecklistItem(id = "3", label = "Task 3", isChecked = false)
            ),
            onToggleChecklist = {},
            onAddChecklistItem = {},
            onRemoveChecklistItem = {},
            onScheduleClick = {},
            onCancelClick = {}
        )
    }
}