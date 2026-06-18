package com.riramzy.biomedtrack.ui.components.equipment

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
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
                            text = stringResource(R.string.add_equipment_title),
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
                            text = stringResource(R.string.add_equipment_subtitle),
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
                BioMedHeader(
                    title = stringResource(R.string.label_equipment_details),
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
                    label = stringResource(R.string.field_name),
                    placeholder = stringResource(R.string.field_name_placeholder),
                    isNoteCard = false,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                BioMedTextField(
                    value = state.model,
                    onValueChange = { onAction(AddEquipmentAction.UpdateModel(it)) },
                    label = stringResource(R.string.field_model),
                    placeholder = stringResource(R.string.field_model_placeholder),
                    isNoteCard = false,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                BioMedTextField(
                    value = state.serialNumber,
                    onValueChange = { onAction(AddEquipmentAction.UpdateSerialNumber(it)) },
                    label = stringResource(R.string.field_serial_number),
                    placeholder = stringResource(R.string.field_serial_placeholder),
                    isNoteCard = false,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                BioMedTextField(
                    value = state.manufacturer,
                    onValueChange = { onAction(AddEquipmentAction.UpdateManufacturer(it)) },
                    label = stringResource(R.string.field_manufacturer),
                    placeholder = stringResource(R.string.field_manufacturer_placeholder),
                    isNoteCard = false,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                BioMedTextField(
                    value = state.agent,
                    onValueChange = { onAction(AddEquipmentAction.UpdateAgent(it)) },
                    label = stringResource(R.string.field_agent),
                    placeholder = stringResource(R.string.field_agent_placeholder),
                    isNoteCard = false,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                BioMedHeader(
                    title = stringResource(R.string.label_classification),
                    height = 45,
                    textSize = 16,
                    modifier = Modifier
                        .padding(
                            top = 15.dp,
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 15.dp
                        )
                        .clip(
                            shape = RoundedCornerShape(15.dp)
                        )
                )

                BioMedSelector(
                    title = stringResource(R.string.field_category),
                    selectedItem = state.category,
                    onItemSelected = { onAction(AddEquipmentAction.UpdateCategory(it)) },
                    placeholder = stringResource(R.string.field_category_placeholder),
                    items = listOf(
                        "Dialysis Machine",
                        "Dialysis Unit",
                        "Dialysis System"
                    ),
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                BioMedSelector(
                    title = stringResource(R.string.field_department),
                    placeholder = stringResource(R.string.field_department_placeholder),
                    selectedItem = state.department?.name
                        ?: stringResource(R.string.field_department_placeholder),
                    onItemSelected = { selectedName ->
                        state.departments.find { it.name == selectedName }?.let {
                            onAction(AddEquipmentAction.UpdateDepartment(it))
                        }
                    },
                    items = state.departments.map { it.name },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                BioMedSelector(
                    title = stringResource(R.string.field_status),
                    selectedItem = state.currentStatus?.name
                        ?: stringResource(R.string.field_status_placeholder),
                    onItemSelected = {
                        onAction(AddEquipmentAction.UpdateCurrentStatus(EquipmentStatus.valueOf(it)))
                    },
                    placeholder = stringResource(R.string.field_status_placeholder),
                    items = EquipmentStatus.entries.map { it.name },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                BioMedHeader(
                    title = stringResource(R.string.label_location_and_dates),
                    height = 45,
                    textSize = 16,
                    modifier = Modifier
                        .padding(
                            top = 15.dp,
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 15.dp
                        )
                        .clip(
                            shape = RoundedCornerShape(15.dp)
                        )
                )

                BioMedSelector(
                    title = stringResource(R.string.field_location),
                    placeholder = stringResource(R.string.field_location_placeholder),
                    selectedItem = state.location,
                    onItemSelected = { onAction(AddEquipmentAction.UpdateLocation(it)) },
                    items = listOf(
                        "Dental",
                        "OR",
                        "ICU",
                        "PICU"
                    ),
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                BioMedDateSelector(
                    title = stringResource(R.string.field_install_date),
                    selectedDate = state.installDate,
                    onDateSelected = { onAction(AddEquipmentAction.UpdateInstallDate(it)) },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                BioMedDateSelector(
                    title = stringResource(R.string.field_warranty_expiry),
                    selectedDate = state.warrantyEndDate ?: 0L,
                    onDateSelected = { onAction(AddEquipmentAction.UpdateWarrantyEndDate(it)) },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                BioMedPhotoDocumentationCard(
                    capturedPhotoUri = state.capturedPhotoUri,
                    addPhotoClick = onAddNewPhoto,
                    modifier = Modifier.padding(horizontal = 20.dp)
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
                horizontalArrangement = Arrangement.Center,
            ) {
                BioMedButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.btn_save),
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

                Spacer(Modifier.width(10.dp))

                BioMedButton(
                    modifier = Modifier.weight(1f),
                    text = stringResource(R.string.btn_cancel),
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

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9", locale = "ar")
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