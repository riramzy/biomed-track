package com.riramzy.biomedtrack.ui.components.maintenance

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.riramzy.biomedtrack.domain.model.MaintenanceLog
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.utils.MaintenanceType

@Composable
fun BioMedMaintenanceCard(
    modifier: Modifier = Modifier,
    log: MaintenanceLog,
) {
    Card(
        modifier = modifier
            .width(386.dp)
            .height(145.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.onSecondary
            } else {
                MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
            }
        )
    ) {
        Row {
            Spacer(
                modifier = Modifier
                    .padding(15.dp)
                    .width(10.dp)
                    .height(120.dp)
                    .fillMaxHeight()
                    .background(
                        color = if (isSystemInDarkTheme()) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.primary
                        },
                        shape = RoundedCornerShape(25.dp)
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        end = 15.dp,
                        top = 15.dp,
                        bottom = 15.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = log.type.name,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSystemInDarkTheme()) {
                            Color.White
                        } else {
                            Color.Black
                        }
                    )

                    Text(
                        text = log.date,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSystemInDarkTheme()) {
                            Color.White
                        } else {
                            Color.Black
                        }
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = log.notes,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isSystemInDarkTheme()) {
                            Color.White
                        } else {
                            Color.Black
                        }
                    )

                    Text(
                        text = log.technicianName,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Normal,
                        color = if (isSystemInDarkTheme()) {
                            MaterialTheme.colorScheme.secondary
                        } else {
                            MaterialTheme.colorScheme.secondary
                        }
                    )

                    Text(
                        text = "Tasks Completed:",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isSystemInDarkTheme()) {
                            MaterialTheme.colorScheme.secondary
                        } else {
                            MaterialTheme.colorScheme.secondary
                        }
                    )

                    log.checklist.forEach { task ->
                        Text(
                            text = task.label,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Normal,
                            color = if (isSystemInDarkTheme()) {
                                MaterialTheme.colorScheme.secondary
                            } else {
                                MaterialTheme.colorScheme.secondary
                            },
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9")
@Composable
fun BioMedMaintenanceCardPreview() {
    BioMedTheme {
        BioMedMaintenanceCard(
            log = MaintenanceLog(
                id = "1",
                equipmentId = "1",
                equipmentName = "Equipment 1",
                equipmentSerial = "123456789",
                department = Department(
                    id = "1",
                    name = "Department 1",
                    totalEquipment = 20
                ),
                type = MaintenanceType.REPAIR,
                technicianId = "1",
                technicianName = "Technician 1",
                date = "14/4/2026",
                currentStatus = EquipmentStatus.SERVICE,
                checklist = emptyList(),
                notes = "Replaced faulty parts with new ones",
                workDone = "Replaced faulty parts with new ones"
            )
        )
    }
}

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedMaintenanceCardDarkPreview() {
    BioMedTheme {
        BioMedMaintenanceCard(
            log = MaintenanceLog(
                id = "1",
                equipmentId = "1",
                equipmentName = "Equipment 1",
                equipmentSerial = "123456789",
                department = Department(
                    id = "1",
                    name = "Department 1",
                    totalEquipment = 20
                ),
                type = MaintenanceType.REPAIR,
                technicianId = "1",
                technicianName = "Technician 1",
                date = "14/4/2026",
                currentStatus = EquipmentStatus.SERVICE,
                checklist = emptyList(),
                notes = "Replaced faulty parts with new ones",
                workDone = "Replaced faulty parts with new ones"
            )
        )
    }
}