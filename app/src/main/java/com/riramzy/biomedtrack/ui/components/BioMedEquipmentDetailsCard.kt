package com.riramzy.biomedtrack.ui.components

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
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun BioMedEquipmentDetailsCard(
    modifier: Modifier = Modifier
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
                        text = "Fresenius \n4008S",
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
                        text = "4545885N70",
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

                    BioMedStatusIndicator()
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    BioMedEditButton()
                    BioMedDeleteButton()
                }
            }
        }

        Section(
            title = "Equipment Details",
            items = listOf(
                Pair("Model", "4008S"),
                Pair("Manufacturer", "Fresenius Medical Group"),
                Pair("Agent", "EGMED"),
                Pair("Category", "Dialysis Machine"),
                Pair("Department", "Hemodialysis")
            )
        )

        Section(
            title = "Location and Dates",
            items = listOf(
                Pair("Location", "Dialysis Unit"),
                Pair("Installation Date", "12/2/2014"),
                Pair("Contract", "EGMED from 1/1/2025 to 31/12/2026")
            )
        )

        Section(
            title = "Maintenance Schedule",
            items = listOf(
                Pair("Next Service", "21/3/2026"),
                Pair("Last Service", "12/1/2026"),
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
        BioMedEquipmentDetailsCard()
    }
}

@Preview(device = "id:pixel_9", showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedEquipmentDetailsCardDarkPreview() {
    BioMedTheme {
        BioMedEquipmentDetailsCard()
    }
}
