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
import com.riramzy.biomedtrack.domain.model.Equipment
import com.riramzy.biomedtrack.ui.components.custom.BioMedDeleteButton
import com.riramzy.biomedtrack.ui.components.custom.BioMedEditButton
import com.riramzy.biomedtrack.ui.components.custom.BioMedHeader
import com.riramzy.biomedtrack.ui.components.custom.BioMedStatusIndicator
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.EquipmentStatus

@Composable
fun BioMedEquipmentDetailsCard(
    modifier: Modifier = Modifier,
    equipment: Equipment,
    canEditEquipment: Boolean,
    canDeleteEquipment: Boolean,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
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
                        .weight(1f)
                ) {
                    Text(
                        text = "${equipment.name} \n${equipment.model}",
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
                        text = equipment.serialNumber,
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 4.dp),
                        color = if (isSystemInDarkTheme()) {
                            Color.White
                        } else {
                            Color.Black
                        }
                    )

                    BioMedStatusIndicator(
                        status = equipment.status.name,
                        changeable = true
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    if (canEditEquipment) {
                        BioMedEditButton(onClick = { onEditClick() })
                    }

                    if (canDeleteEquipment) {
                        BioMedDeleteButton(onClick = { onDeleteClick() })
                    }
                }
            }
        }

        Section(
            title = "Equipment Details",
            items = listOf(
                Pair("Model", equipment.model),
                Pair("Manufacturer", equipment.manufacturer),
                Pair("Agent", equipment.agent),
                Pair("Category", equipment.category),
                Pair("Department", equipment.department.name)
            )
        )

        Section(
            title = "Location and Dates",
            items = listOf(
                Pair("Location", equipment.location),
                Pair("Installation Date", equipment.installDate),
                Pair("Contract", equipment.contractInfo ?: "No contract")
            )
        )

        Section(
            title = "Maintenance Schedule",
            items = listOf(
                Pair("Next Service", equipment.nextMaintenanceDate ?: "None"),
                Pair("Last Service", equipment.lastMaintenanceDate ?: "Never"),
            )
        )
    }
}

@Composable
fun Section(
    modifier: Modifier = Modifier,
    title: String,
    items: List<Pair<String, String>>
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 20.dp,
                bottom = 15.dp
            )
    ) {
        BioMedHeader(
            title = title
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items.forEach { item ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = item.first,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Light,
                        color = if (isSystemInDarkTheme()) {
                            MaterialTheme.colorScheme.secondary
                        } else {
                            MaterialTheme.colorScheme.secondary
                        }
                    )

                    Text(
                        text = item.second,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isSystemInDarkTheme()) {
                            Color.White
                        } else {
                            Color.Black
                        }
                    )
                }
            }
        }
    }
}

@Preview(device = "id:pixel_9", showBackground = true)
@Composable
fun BioMedEquipmentDetailsCardPreview() {
    BioMedTheme {
        BioMedEquipmentDetailsCard(
            equipment = Equipment(
                id = "1",
                name = "Fresenius",
                model = "4008S",
                serialNumber = "234523N70",
                department = Department(
                    id = "1",
                    name = "Dialysis Unit",
                    totalEquipment = 52,
                    dueServiceEquipment = 27,
                    downEquipment = 15,
                ),
                status = EquipmentStatus.SERVICE,
                location = "Dialysis Unit",
                installDate = "12/2/2014",
                nextMaintenanceDate = "21/3/2026",
                lastMaintenanceDate = "12/1/2026",
                manufacturer = "Fresenius Medical Group",
                agent = "EGMED",
                category = "Dialysis Machine",
                createdBy = "Ramzy Habel",
                serviceIntervalDays = 90
            ),
            canEditEquipment = true,
            canDeleteEquipment = true,
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}

@Preview(device = "id:pixel_9", showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedEquipmentDetailsCardDarkPreview() {
    BioMedTheme {
        BioMedEquipmentDetailsCard(
            equipment = Equipment(
                id = "1",
                name = "Fresenius",
                model = "4008S",
                serialNumber = "234523N70",
                department = Department(
                    id = "1",
                    name = "Dialysis Unit",
                    totalEquipment = 52,
                    dueServiceEquipment = 27,
                    downEquipment = 15,
                ),
                status = EquipmentStatus.SERVICE,
                location = "Dialysis Unit",
                installDate = "12/2/2014",
                nextMaintenanceDate = "21/3/2026",
                lastMaintenanceDate = "12/1/2026",
                manufacturer = "Fresenius Medical Group",
                agent = "EGMED",
                category = "Dialysis Machine",
                createdBy = "Ramzy Habel",
                serviceIntervalDays = 90
            ),
            canEditEquipment = true,
            canDeleteEquipment = true,
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}
