package com.riramzy.biomedtrack.ui.components.report

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun BioMedReportExportFormatCard(
    modifier: Modifier = Modifier,
    selectedFormat: String = "PDF",
    onFormatSelected: (String) -> Unit = {},
    onGenerateReport: () -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(bottom = 15.dp)
        ) {
            BioMedExportFormatItem(
                title = "PDF",
                icon = R.drawable.pdf,
                note = "Best for sharing and printing",
                isSelected = selectedFormat == "PDF",
                modifier = Modifier.clickable { onFormatSelected("PDF") }
            )

            BioMedExportFormatItem(
                title = "Excel",
                icon = R.drawable.excel,
                note = "Best for editing and analysis",
                isSelected = selectedFormat == "Excel",
                modifier = Modifier.clickable { onFormatSelected("Excel") }
            )
        }

        BioMedButton(
            text = "Generate Report",
            modifier = Modifier
                .width(356.dp),
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
            customTextSize = 14,
            onClick = onGenerateReport
        )
    }
}

@Composable
fun BioMedExportFormatItem(
    modifier: Modifier = Modifier,
    title: String = "PDF",
    icon: Int = R.drawable.pdf,
    note: String = "Best for sharing and printing",
    isSelected: Boolean = false
) {
    Card(
        modifier = modifier
            .width(171.dp)
            .height(141.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) {
                if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
                }
            } else {
                if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
                }
            }
        )
    ) {
        Column(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(43.dp)
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier
                        .padding(start = 10.dp),
                    color = if (isSystemInDarkTheme()) {
                        if (isSelected) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            Color.White
                        }
                    } else {
                        if (isSelected) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            Color.Black
                        }
                    }
                )
            }

            Text(
                text = note,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = if (isSystemInDarkTheme()) {
                    if (isSelected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        Color.White
                    }
                } else {
                    if (isSelected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        Color.Black
                    }
                }
            )
        }

    }
}

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9")
@Composable
fun BioMedReportExportFormatCardPreview() {
    BioMedTheme {
        BioMedReportExportFormatCard()
    }
}

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF000000
)
@Composable
fun BioMedReportExportFormatCardDarkPreview() {
    BioMedTheme {
        BioMedReportExportFormatCard()
    }
}