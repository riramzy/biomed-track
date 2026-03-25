package com.riramzy.biomedtrack.ui.components

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
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.ui.theme.indicatorColors

@Composable
fun BioMedEquipmentOverviewCard(
    modifier: Modifier = Modifier,
    status: String = "Online"
) {
    Card(
        modifier = modifier
            .width(355.dp)
            .height(190.dp),
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
                    .width(21.dp)
                    .height(190.dp)
                    .fillMaxHeight()
                    .background(
                        color = when (status) {
                            "Online" -> MaterialTheme.indicatorColors.green
                            "Down" -> MaterialTheme.indicatorColors.red
                            "Service" -> MaterialTheme.indicatorColors.yellow
                            else -> MaterialTheme.colorScheme.primary
                                              },
                        shape = RoundedCornerShape(
                            topStart = 24.dp,
                            bottomStart = 24.dp
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Fresenius 4008S",
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

                    BioMedStatusIndicator(status = status)
                }

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
                                text = "Serial Number",
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
                                text = "4545885N70",
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
                                text = "Model",
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
                                text = "4008S",
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
                                text = "Location",
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
                                text = "Dialysis Unit",
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
                                text = "Next Service",
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
                                text = "21/3/2026",
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
                            text = "Category: ",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Text(
                            text = "Dialysis Machine",
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
                            text = "Manufacturer: ",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Text(
                            text = "Fresenius Medical Group",
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

@Preview(showBackground = true, device = "id:pixel_9")
@Composable
fun BioMedEquipmentOverviewCardPreview() {
    BioMedTheme {
        BioMedEquipmentOverviewCard(
            status = "Down"
        )
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedEquipmentOverviewCardDarkPreview() {
    BioMedTheme {
        BioMedEquipmentOverviewCard(
            status = "Service"
        )
    }
}

