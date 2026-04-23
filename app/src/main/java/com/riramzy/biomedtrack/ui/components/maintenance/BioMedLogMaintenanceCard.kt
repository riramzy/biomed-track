package com.riramzy.biomedtrack.ui.components.maintenance

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.riramzy.biomedtrack.ui.components.BioMedPhotoDocumentationCard
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.components.custom.BioMedDateSelector
import com.riramzy.biomedtrack.ui.components.custom.BioMedMultipleItemsSelector
import com.riramzy.biomedtrack.ui.components.custom.BioMedSelector
import com.riramzy.biomedtrack.ui.components.custom.BioMedTextField
import com.riramzy.biomedtrack.ui.screens.maintenance.LogMaintenanceAction
import com.riramzy.biomedtrack.ui.screens.maintenance.LogMaintenanceUiState
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.utils.MaintenanceType

@Composable
fun BioMedLogMaintenanceCard(
    modifier: Modifier = Modifier,
    state: LogMaintenanceUiState = LogMaintenanceUiState(),
    onAction: (LogMaintenanceAction) -> Unit = {},
    onCancel: () -> Unit = {},
    onAddNewPhoto: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    Card(
        modifier = modifier
            .width(380.dp)
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
                            text = "Log Maintenance",
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
                            text = "Record maintenance, repairs, and inspections",
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
                    .wrapContentHeight()
                    .padding(bottom = 15.dp)
                    .verticalScroll(scrollState)
                    .weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                BioMedTextField(
                    label = "Equipment",
                    value = state.equipmentName,
                    onValueChange = {},
                    isNoteCard = false,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                BioMedSelector(
                    title = "Maintenance Type",
                    placeholder = "Select Maintenance Type",
                    items = MaintenanceType.entries.map { it.name },
                    selectedItem = state.type.name,
                    onItemSelected = { selected ->
                        onAction(LogMaintenanceAction.UpdateType(MaintenanceType.valueOf(selected)))
                    },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                BioMedSelector(
                    title = "Current Status",
                    placeholder = "Select Current Status",
                    items = EquipmentStatus.entries.map { it.name },
                    selectedItem = state.currentStatus.name,
                    onItemSelected = { selected ->
                        onAction(LogMaintenanceAction.UpdateStatus(EquipmentStatus.valueOf(selected)))
                    },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                BioMedDateSelector(
                    selectedDate = state.date,
                    onDateSelected = { selected ->
                        onAction(LogMaintenanceAction.UpdateDate(selected))
                    },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                BioMedTextField(
                    label = "By",
                    value = state.technicianName,
                    onValueChange = {},
                    isNoteCard = false,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                BioMedTextField(
                    value = state.notes,
                    onValueChange = {},
                    label = "Notes and Observations",
                    isNoteCard = true,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                BioMedMultipleItemsSelector(
                    title = "Tasks Completed",
                    items = state.checklist,
                    onToggle = { selected ->
                        onAction(LogMaintenanceAction.ToggleChecklistItem(selected))
                    },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                BioMedPhotoDocumentationCard(
                    title = "Photo Documentation",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    capturedPhotoUri = state.capturedPhotoUri,
                    addPhotoClick = { onAddNewPhoto() }
                )
            }

            Row(
                modifier = Modifier
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 15.dp
                    )
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                BioMedButton(
                    modifier = Modifier.padding(end = 10.dp),
                    text = "Save",
                    customColor = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                    customTextColor = if (isSystemInDarkTheme()) {
                        Color.White
                    } else {
                        MaterialTheme.colorScheme.onPrimary
                    },
                    onClick = { onAction(LogMaintenanceAction.Save) }
                )

                BioMedButton(
                    text = "Cancel",
                    customColor = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.primaryContainer
                    },
                    customTextColor = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    } else {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    },
                    onClick = { onCancel() }
                )
            }
        }
    }
}

@Preview(device = "id:pixel_9", showBackground = true)
@Composable
fun BioMedLogMaintenanceCardPreview() {
    BioMedTheme {
        BioMedLogMaintenanceCard()
    }
}

@Preview(device = "id:pixel_9", showBackground = true, showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedLogMaintenanceCardDarkPreview() {
    BioMedTheme {
        BioMedLogMaintenanceCard()
    }
}