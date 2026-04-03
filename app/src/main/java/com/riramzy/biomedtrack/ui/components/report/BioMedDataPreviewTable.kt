package com.riramzy.biomedtrack.ui.components.report

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.ui.theme.indicatorColors

data class DataPreviewRow(
    val name: String,
    val model: String,
    val serialNumber: String,
    val department: String,
    val category: String,
    val status: String,
    val logs: String,
    val validationStatus: ValidationStatus = ValidationStatus.VALID,
    val message: String? = null
)

enum class ValidationStatus {
    VALID, ERROR, WARNING
}

@Composable
fun BioMedDataPreviewTable(
    rows: List<DataPreviewRow>,
    onRowClick: (DataPreviewRow) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Card(
        modifier = modifier
            .width(386.dp)
            .height(388.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
        ),
        border = BorderStroke(
            width = 1.dp,
            MaterialTheme.colorScheme.primary
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(25.dp))
                    .padding(vertical = 12.dp, horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = false,
                    onClick = { /*TODO*/ },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.onPrimary,
                        unselectedColor = MaterialTheme.colorScheme.primary,
                        disabledSelectedColor = MaterialTheme.colorScheme.onPrimary,
                        disabledUnselectedColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier
                        .size(24.dp)
                )

                // Space for the leading status icon in rows
                Spacer(modifier = Modifier.width(16.dp))

                Row(
                    modifier = Modifier
                        .horizontalScroll(scrollState)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(30.dp)
                ) {
                    val headers = listOf("Name", "Model", "Serial Number", "Department", "Category", "Status", "Logs")
                    headers.forEach { header ->
                        Card(
                            modifier = Modifier
                                .width(110.dp)
                                .height(30.dp),
                            shape = RoundedCornerShape(25.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                text = header,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(4.dp),
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // Data Rows
                rows.forEach { row ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onRowClick(row)
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 15.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Leading Status Icon
                            val (icon, iconColor) = when (row.validationStatus) {
                                ValidationStatus.VALID -> R.drawable.activity_online to MaterialTheme.colorScheme.primary
                                ValidationStatus.ERROR -> R.drawable.error to MaterialTheme.indicatorColors.red
                                ValidationStatus.WARNING -> R.drawable.warning to MaterialTheme.indicatorColors.yellow
                            }

                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(24.dp)
                                    .background(iconColor),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(icon),
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(15.dp))

                            // Scrollable Columns
                            Row(
                                modifier = Modifier
                                    .horizontalScroll(scrollState)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(30.dp)
                            ) {
                                val textColor = when (row.validationStatus) {
                                    ValidationStatus.VALID -> if (isSystemInDarkTheme()) Color.White else Color.Black
                                    ValidationStatus.ERROR -> MaterialTheme.indicatorColors.red
                                    ValidationStatus.WARNING -> MaterialTheme.indicatorColors.yellow
                                }

                                val dataPoints = listOf(row.name, row.model, row.serialNumber, row.department, row.category, row.status, row.logs)
                                dataPoints.forEach { text ->
                                    Text(
                                        text = text,
                                        color = textColor,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.width(110.dp)
                                    )
                                }
                            }
                        }

                        // Error/Warning Note below the row
                        if (row.message != null) {
                            val messageColor = if (row.validationStatus == ValidationStatus.ERROR)
                                MaterialTheme.indicatorColors.red else MaterialTheme.indicatorColors.yellow

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 15.dp, bottom = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(18.dp)
                                        .background(messageColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = if (row.validationStatus == ValidationStatus.ERROR)
                                            painterResource(R.drawable.error) else painterResource(R.drawable.warning),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }

                                Text(
                                    text = row.message,
                                    color = messageColor,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_9")
@Composable
fun BioMedDataPreviewTablePreview() {
    BioMedTheme {
        val sampleRows = listOf(
            DataPreviewRow("Fresenius", "4008S", "4545885N70", "Dialysis", "Dialysis Machine", "Online", "Logs", ValidationStatus.VALID),
            DataPreviewRow("Fresenius", "4008S", "4545885N70", "Dialysis", "Dialysis Machine", "Down", "Logs", ValidationStatus.ERROR, "Error: Overriding an already existing equipment"),
            DataPreviewRow("Fresenius", "4008S", "4545885N70", "Dialysis", "Dialysis Machine", "Service", "Logs", ValidationStatus.WARNING, "Warning: Serial number format is unusual"),
            DataPreviewRow("Fresenius", "4008S", "4545885N70", "Dialysis", "Dialysis Machine", "Down", "Logs", ValidationStatus.ERROR, "Error: Overriding an already existing equipment"),
            DataPreviewRow("Fresenius", "4008S", "4545885N70", "Dialysis", "Dialysis Machine", "Down", "Logs", ValidationStatus.ERROR, "Error: Overriding an already existing equipment"),
            DataPreviewRow("Fresenius", "4008S", "4545885N70", "Dialysis", "Dialysis Machine", "Down", "Logs", ValidationStatus.ERROR, "Error: Overriding an already existing equipment"),
            DataPreviewRow("Fresenius", "4008S", "4545885N70", "Dialysis", "Dialysis Machine", "Down", "Logs", ValidationStatus.ERROR, "Error: Overriding an already existing equipment"),
            DataPreviewRow("Fresenius", "4008S", "4545885N70", "Dialysis", "Dialysis Machine", "Down", "Logs", ValidationStatus.ERROR, "Error: Overriding an already existing equipment"),
            DataPreviewRow("Fresenius", "4008S", "4545885N70", "Dialysis", "Dialysis Machine", "Down", "Logs", ValidationStatus.ERROR, "Error: Overriding an already existing equipment"),
        )
        BioMedDataPreviewTable(rows = sampleRows, modifier = Modifier.padding(10.dp))
    }
}

@Preview(showBackground = true, device = "id:pixel_9", showSystemUi = false,
    backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedDataPreviewTableDarkPreview() {
    BioMedTheme {
        val sampleRows = listOf(
            DataPreviewRow("Fresenius", "4008S", "4545885N70", "Dialysis", "Dialysis Machine", "Online", "Logs", ValidationStatus.VALID),
            DataPreviewRow("Fresenius", "4008S", "4545885N70", "Dialysis", "Dialysis Machine", "Down", "Logs", ValidationStatus.ERROR, "Error: Overriding an already existing equipment"),
            DataPreviewRow("Fresenius", "4008S", "4545885N70", "Dialysis", "Dialysis Machine", "Service", "Logs", ValidationStatus.WARNING, "Warning: Serial number format is unusual"),
            DataPreviewRow("Fresenius", "4008S", "4545885N70", "Dialysis", "Dialysis Machine", "Down", "Logs", ValidationStatus.ERROR, "Error: Overriding an already existing equipment"),
            DataPreviewRow("Fresenius", "4008S", "4545885N70", "Dialysis", "Dialysis Machine", "Down", "Logs", ValidationStatus.ERROR, "Error: Overriding an already existing equipment"),
            DataPreviewRow("Fresenius", "4008S", "4545885N70", "Dialysis", "Dialysis Machine", "Down", "Logs", ValidationStatus.ERROR, "Error: Overriding an already existing equipment"),
            DataPreviewRow("Fresenius", "4008S", "4545885N70", "Dialysis", "Dialysis Machine", "Down", "Logs", ValidationStatus.ERROR, "Error: Overriding an already existing equipment"),
            DataPreviewRow("Fresenius", "4008S", "4545885N70", "Dialysis", "Dialysis Machine", "Down", "Logs", ValidationStatus.ERROR, "Error: Overriding an already existing equipment"),
            DataPreviewRow("Fresenius", "4008S", "4545885N70", "Dialysis", "Dialysis Machine", "Down", "Logs", ValidationStatus.ERROR, "Error: Overriding an already existing equipment"),
        )
        BioMedDataPreviewTable(rows = sampleRows, modifier = Modifier.padding(10.dp))
    }
}