package com.riramzy.biomedtrack.ui.components.equipment

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.ui.theme.indicatorColors
import com.riramzy.biomedtrack.utils.EquipmentStatus

@Composable
fun BioMedEquipmentStatusCard(
    modifier: Modifier = Modifier,
    equipmentName: String = "Fresenius",
    equipmentModel: String = "4008S",
    equipmentSerialNumber: String = "123456789",
    equipmentCategory: String = "Dialysis Machine",
    equipmentDepartment: Department = Department(
        id = "1",
        name = "Dialysis Unit",
        totalEquipment = 20
    ),
    equipmentStatus: EquipmentStatus = EquipmentStatus.SERVICE,
    equipmentLastServiceDate: String = "2022-01-01",
    isAbbreviated: Boolean = false
) {
    Card(
        modifier = modifier
            .width(380.dp)
            .height(if (isAbbreviated) (50.dp) else (90.dp)),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.onSecondary
            } else {
                MaterialTheme.colorScheme.primaryContainer.copy(0.7f)
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .padding(if (isAbbreviated) (10.dp) else (15.dp))
                    .size(if (isAbbreviated) (24.dp) else (35.dp))
                    .clip(CircleShape)
                    .background(
                        when(equipmentStatus) {
                            EquipmentStatus.ONLINE -> MaterialTheme.indicatorColors.green
                            EquipmentStatus.DOWN -> MaterialTheme.indicatorColors.red
                            EquipmentStatus.SERVICE -> MaterialTheme.indicatorColors.yellow
                        }
                    ),
            )

            Column(
                modifier = Modifier
                    .weight(if (isAbbreviated) (1f) else (2f))
                    .fillMaxHeight()
                    .padding(if (isAbbreviated) (10.dp) else (0.dp)),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$equipmentName $equipmentModel",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSystemInDarkTheme()) {
                        Color.White
                    } else {
                        Color.Black
                    }

                )
                Text(
                    text = equipmentSerialNumber,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSystemInDarkTheme()) {
                        Color.White
                    } else {
                        Color.Black
                    }
                )

                if (!isAbbreviated) {
                    Text(
                        text = equipmentDepartment.name,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Light,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            if (!isAbbreviated) {
                Column(
                    modifier = Modifier
                        .padding(end = 15.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = equipmentLastServiceDate,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSystemInDarkTheme()) {
                            Color.White
                        } else {
                            Color.Black
                        }
                    )

                    Text(
                        text = equipmentCategory,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )

                    LogMaintenancePill(
                        modifier = Modifier.padding(top = 5.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LogMaintenancePill(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(69.dp)
            .height(19.dp),
        shape = CircleShape,
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            IconButton(
                onClick = {},
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.activity_log),
                    contentDescription = null,
                    tint = if (isSystemInDarkTheme()) {
                        Color.White
                    } else {
                        Color.White
                    }
                )
            }
        }
    }
}

@Preview(device = "id:pixel_9", showBackground = true)
@Composable
fun BioMedEquipmentStatusCardPreview() {
    BioMedTheme {
        BioMedEquipmentStatusCard(

        )
    }
}

@Preview(device = "id:pixel_9", showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedEquipmentStatusCardDarkPreview() {
    BioMedTheme {
        BioMedEquipmentStatusCard(

        )
    }
}