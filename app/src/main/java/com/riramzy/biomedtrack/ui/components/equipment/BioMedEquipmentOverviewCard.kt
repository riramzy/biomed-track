package com.riramzy.biomedtrack.ui.components.equipment

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.ui.components.custom.BioMedStatusIndicator
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.EquipmentStatus
import com.riramzy.biomedtrack.utils.getLocalizedDepartmentName

@Composable
fun BioMedEquipmentOverviewCard(
    modifier: Modifier = Modifier,
    name: String = "Fresenius",
    model: String = "4008S",
    status: EquipmentStatus = EquipmentStatus.SERVICE,
    serialNumber: String = "4545885N70",
    department: Department = Department(
        id = "1",
        name = "Dialysis Unit",
        totalEquipment = 10,
    ),
    category: String = "Dialysis Machine",
    manufacturer: String = "Fresenius Medical Group",
    nextServiceDate: String? = "13/3/2026",
    onCardClick: () -> Unit = {},
    onStatusClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
        ),
        onClick = { onCardClick() }
    ) {
        Row(
            modifier = Modifier
                .wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$name\n$model",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 10.dp),
                        color = if (isSystemInDarkTheme()) {
                            Color.White
                        } else {
                            Color.Black
                        }
                    )

                    Box(
                        modifier = Modifier
                            .clickable { onStatusClick() }
                    ) {
                        BioMedStatusIndicator(
                            status = status.name,
                            changeable = true,
                            onStatusClicked = { onStatusClick() }
                        )
                    }
                }

                Spacer(Modifier.height(15.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.label_serial_number),
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Light,
                                color = if (isSystemInDarkTheme()) {
                                    Color.White
                                } else {
                                    Color.Black
                                }
                            )

                            Text(
                                text = serialNumber,
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

                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.label_model),
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Light,
                                color = if (isSystemInDarkTheme()) {
                                    Color.White
                                } else {
                                    Color.Black
                                }
                            )

                            Text(
                                text = model,
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

                    Spacer(Modifier.height(5.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.label_department),
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Light,
                                color = if (isSystemInDarkTheme()) {
                                    Color.White
                                } else {
                                    Color.Black
                                }
                            )

                            Text(
                                text = getLocalizedDepartmentName(department.name),
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

                        Column(
                            modifier = Modifier
                                .weight(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.label_next_service),
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Light,
                                color = if (isSystemInDarkTheme()) {
                                    Color.White
                                } else {
                                    Color.Black
                                }
                            )

                            Text(
                                text = if (nextServiceDate.isNullOrEmpty()) stringResource(R.string.label_none) else nextServiceDate,
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

                Spacer(Modifier.height(15.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = stringResource(R.string.label_category_prefix) + " ",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Text(
                            text = category,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }


                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = stringResource(R.string.label_manufacturer_prefix) + " ",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Text(
                            text = manufacturer,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_9", backgroundColor = 0xFFFFFFFF, locale = "ar")
@Composable
fun BioMedEquipmentOverviewCardPreview() {
    BioMedTheme {
        BioMedEquipmentOverviewCard()
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF000000
)
@Composable
fun BioMedEquipmentOverviewCardDarkPreview() {
    BioMedTheme {
        BioMedEquipmentOverviewCard()
    }
}

