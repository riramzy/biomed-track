package com.riramzy.biomedtrack.ui.components.equipment

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.ui.components.BioMedPhotoDocumentationCard
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.components.custom.BioMedDateSelector
import com.riramzy.biomedtrack.ui.components.custom.BioMedHeader
import com.riramzy.biomedtrack.ui.components.custom.BioMedSelector
import com.riramzy.biomedtrack.ui.components.custom.BioMedTextField
import com.riramzy.biomedtrack.ui.screens.equipment.add.AddEquipmentAction
import com.riramzy.biomedtrack.ui.screens.equipment.add.AddEquipmentUiState
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.EquipmentStatus

@Composable
fun BioMedAddEquipmentCard(
    modifier: Modifier = Modifier,
    state: AddEquipmentUiState = AddEquipmentUiState(),
    onAction: (AddEquipmentAction) -> Unit = {},
    onCancel: () -> Unit = {}
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
                            text = "Add Equipment",
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
                            text = "Fill in the equipment details below",
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
                    .weight(1f, fill = false)
            ) {
                BioMedHeader(
                    title = "Equipment Details",
                    height = 45,
                    textSize = 16,
                    modifier = Modifier
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 15.dp
                        )
                        .clip(
                            shape = RoundedCornerShape(15.dp)
                        )
                )

                BioMedTextField(
                    value = state.name,
                    onValueChange = { onAction(AddEquipmentAction.UpdateName(it)) },
                    label = "Name",
                    placeholder = "Enter Name",
                    isNoteCard = false
                )

                BioMedTextField(
                    value = state.model,
                    onValueChange = { onAction(AddEquipmentAction.UpdateModel(it)) },
                    label = "Model",
                    placeholder = "Enter Model",
                    isNoteCard = false
                )

                BioMedTextField(
                    value = state.serialNumber,
                    onValueChange = { onAction(AddEquipmentAction.UpdateSerialNumber(it)) },
                    label = "Serial Number",
                    placeholder = "Enter Serial Number",
                    isNoteCard = false
                )

                BioMedTextField(
                    value = state.manufacturer,
                    onValueChange = { onAction(AddEquipmentAction.UpdateManufacturer(it)) },
                    label = "Manufacturer",
                    placeholder = "Enter Manufacturer",
                    isNoteCard = false
                )

                BioMedTextField(
                    value = state.agent,
                    onValueChange = { onAction(AddEquipmentAction.UpdateAgent(it)) },
                    label = "Agent",
                    placeholder = "Enter Agent",
                    isNoteCard = false
                )

                BioMedHeader(
                    title = "Classification",
                    height = 45,
                    textSize = 16,
                    modifier = Modifier
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 15.dp
                        )
                        .clip(
                            shape = RoundedCornerShape(15.dp)
                        )
                )

                BioMedSelector(
                    title = "Category",
                    selectedItem = state.category,
                    onItemSelected = { onAction(AddEquipmentAction.UpdateCategory(it)) },
                    placeholder = "Select Category",
                    items = listOf(
                        "Dialysis Machine",
                        "Dialysis Unit",
                        "Dialysis System"
                    )
                )

                BioMedSelector(
                    title = "Department",
                    placeholder = "Select Department",
                    selectedItem = state.department?.name ?: "Select Department",
                    onItemSelected = { selectedName ->
                        state.departments.find { it.name == selectedName }?.let {
                            onAction(AddEquipmentAction.UpdateDepartment(it))
                        }
                    },
                    items = state.departments.map { it.name }
                )

                BioMedSelector(
                    title = "Current Status",
                    selectedItem = state.currentStatus?.name ?: "Select Status",
                    onItemSelected = {
                        onAction(AddEquipmentAction.UpdateCurrentStatus(EquipmentStatus.valueOf(it)))
                    },
                    placeholder = "Select Status",
                    items = EquipmentStatus.entries.map { it.name }
                )

                BioMedHeader(
                    title = "Location and Dates",
                    height = 45,
                    textSize = 16,
                    modifier = Modifier
                        .padding(
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 15.dp
                        )
                        .clip(
                            shape = RoundedCornerShape(15.dp)
                        )
                )

                BioMedSelector(
                    title = "Location",
                    placeholder = "Dialysis Unit",
                    selectedItem = state.location,
                    onItemSelected = { onAction(AddEquipmentAction.UpdateLocation(it)) },
                    items = listOf(
                        "Dental",
                        "OR",
                        "ICU",
                        "PICU"
                    )
                )

                BioMedDateSelector(
                    title = "Installation Date",
                    selectedDate = state.installDate,
                    onDateSelected = { onAction(AddEquipmentAction.UpdateInstallDate(it)) }
                )

                BioMedDateSelector(
                    title = "Warranty End Date",
                    selectedDate = state.warrantyEndDate,
                    onDateSelected = { onAction(AddEquipmentAction.UpdateWarrantyEndDate(it)) }
                )

                BioMedPhotoDocumentationCard(
                    title = "Photo Documentation",
                    description = "Take photos of installation reports or documentations"
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
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onPrimary
                    },
                    onClick = {
                        onAction(AddEquipmentAction.Save)
                    }
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

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9")
@Composable
fun BioMedAddEquipmentCardPreview() {
    BioMedTheme {
        BioMedAddEquipmentCard()
    }
}

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedAddEquipmentCardDarkPreview() {
    BioMedTheme {
        BioMedAddEquipmentCard()
    }
}