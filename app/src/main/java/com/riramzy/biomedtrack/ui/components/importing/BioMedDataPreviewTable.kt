package com.riramzy.biomedtrack.ui.components.importing

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.components.custom.BioMedRadioButton
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.ui.theme.indicatorColors
import com.riramzy.biomedtrack.utils.DataPreviewRow
import com.riramzy.biomedtrack.utils.ValidationStatus

@Composable
fun BioMedDataPreviewTable(
    modifier: Modifier = Modifier,
    rows: List<DataPreviewRow>,
    onRowToggle: (String) -> Unit = {},
    onToggleSelectAll: (Boolean) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    val headers = listOf(
        stringResource(R.string.table_header_name),
        stringResource(R.string.table_header_model),
        stringResource(R.string.table_header_serial),
        stringResource(R.string.table_header_department),
        stringResource(R.string.table_header_category),
        stringResource(R.string.table_header_status),
        stringResource(R.string.table_header_logs)
    )

    Column(
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
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
                    val allValidSelected = rows.filter { it.validationStatus == ValidationStatus.VALID }.let { validRows ->
                        validRows.isNotEmpty() && validRows.all { it.isSelected }
                    }

                    RadioButton(
                        selected = allValidSelected,
                        onClick = { onToggleSelectAll(!allValidSelected) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary,
                            unselectedColor = MaterialTheme.colorScheme.primary,
                            disabledSelectedColor = MaterialTheme.colorScheme.primary,
                            disabledUnselectedColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Row(
                        modifier = Modifier
                            .horizontalScroll(scrollState)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(30.dp)
                    ) {
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
                    rows.forEach { row ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            val rowScrollState = rememberScrollState()

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp, horizontal = 15.dp)
                                    .clickable { onRowToggle(row.id) },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val (icon, iconColor) = when (row.validationStatus) {
                                    ValidationStatus.VALID -> R.drawable.activity_online to MaterialTheme.colorScheme.primary
                                    ValidationStatus.ERROR -> R.drawable.activity_online to MaterialTheme.indicatorColors.red
                                    ValidationStatus.WARNING -> R.drawable.activity_online to MaterialTheme.indicatorColors.yellow
                                }

                                BioMedRadioButton(
                                    isSelected = row.isSelected,
                                    icon = icon,
                                    iconColor = iconColor,
                                    onClick = { if (row.validationStatus == ValidationStatus.VALID) onRowToggle(row.id) },
                                )

                                Spacer(modifier = Modifier.width(15.dp))

                                Row(
                                    modifier = Modifier
                                        .horizontalScroll(rowScrollState)
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

                            if (row.message != null) {
                                val messageColor = if (row.validationStatus == ValidationStatus.ERROR)
                                    MaterialTheme.indicatorColors.red else MaterialTheme.indicatorColors.yellow

                                val displayMessage = when (row.message) {
                                    "Serial Number is blank" -> stringResource(R.string.import_validation_serial_blank)
                                    "Serial Number already exists" -> stringResource(R.string.import_validation_serial_exists_db)
                                    "Serial Number already exists in the file" -> stringResource(R.string.import_validation_serial_exists_file)
                                    else -> row.message
                                }

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
                                            modifier = Modifier.size(11.dp)
                                        )
                                    }

                                    Text(
                                        text = displayMessage,
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

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BioMedButton(
                modifier = Modifier
                    .weight(1f),
                text = stringResource(R.string.import_btn_select_all),
                customColor = MaterialTheme.colorScheme.primary,
                customTextColor = MaterialTheme.colorScheme.onPrimary,
                onClick = { onToggleSelectAll(true) }
            )

            BioMedButton(
                modifier = Modifier
                    .weight(1f),
                text = stringResource(R.string.import_btn_deselect_all),
                onClick = { onToggleSelectAll(false) }
            )
        }
    }

}

@Preview(showBackground = true, device = "id:pixel_9", locale = "ar")
@Composable
fun BioMedDataPreviewTablePreview() {
    BioMedTheme {
        val sampleRows = listOf(
            DataPreviewRow("1", false,"Fresenius", "4008S", "4545885N70", "Dialysis", "Dialysis Machine", "Online", "Logs", ValidationStatus.VALID),
            DataPreviewRow(
                "1",
                false,
                "Fresenius",
                "4008S",
                "4545885N70",
                "Dialysis",
                "Dialysis Machine",
                "Down",
                "Logs",
                ValidationStatus.ERROR,
                "Serial Number is blank"
            ),
            DataPreviewRow(
                "1",
                false,
                "Fresenius",
                "4008S",
                "4545885N70",
                "Dialysis",
                "Dialysis Machine",
                "Service",
                "Logs",
                ValidationStatus.WARNING,
                "Serial Number already exists in the file"
            ),
            DataPreviewRow(
                "1",
                false,
                "Fresenius",
                "4008S",
                "4545885N70",
                "Dialysis",
                "Dialysis Machine",
                "Down",
                "Logs",
                ValidationStatus.ERROR,
                "Serial Number is blank"
            ),
            DataPreviewRow(
                "1",
                false,
                "Fresenius",
                "4008S",
                "4545885N70",
                "Dialysis",
                "Dialysis Machine",
                "Down",
                "Logs",
                ValidationStatus.ERROR,
                "Serial Number already exists in the file"
            ),
            DataPreviewRow(
                "1",
                false,
                "Fresenius",
                "4008S",
                "4545885N70",
                "Dialysis",
                "Dialysis Machine",
                "Down",
                "Logs",
                ValidationStatus.ERROR,
                "Serial Number already exists in the file"
            ),
            DataPreviewRow(
                "1",
                false,
                "Fresenius",
                "4008S",
                "4545885N70",
                "Dialysis",
                "Dialysis Machine",
                "Down",
                "Logs",
                ValidationStatus.ERROR,
                "Serial Number already exists in the file"
            ),
            DataPreviewRow(
                "1",
                false,
                "Fresenius",
                "4008S",
                "4545885N70",
                "Dialysis",
                "Dialysis Machine",
                "Down",
                "Logs",
                ValidationStatus.ERROR,
                "Serial Number already exists in the file"
            ),
            DataPreviewRow(
                "1",
                false,
                "Fresenius",
                "4008S",
                "4545885N70",
                "Dialysis",
                "Dialysis Machine",
                "Down",
                "Logs",
                ValidationStatus.ERROR,
                "Serial Number already exists in the file"
            ),
        )
        BioMedDataPreviewTable(rows = sampleRows, modifier = Modifier.padding(10.dp))
    }
}

@Preview(showBackground = true, device = "id:pixel_9", showSystemUi = false,
    backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL, locale = "ar"
)
@Composable
fun BioMedDataPreviewTableDarkPreview() {
    BioMedTheme {
        val sampleRows = listOf(
            DataPreviewRow("1", false,"Fresenius", "4008S", "4545885N70", "Dialysis", "Dialysis Machine", "Online", "Logs", ValidationStatus.VALID),
            DataPreviewRow(
                "1",
                false,
                "Fresenius",
                "4008S",
                "4545885N70",
                "Dialysis",
                "Dialysis Machine",
                "Down",
                "Logs",
                ValidationStatus.ERROR,
                "Serial Number is blank"
            ),
            DataPreviewRow(
                "1",
                false,
                "Fresenius",
                "4008S",
                "4545885N70",
                "Dialysis",
                "Dialysis Machine",
                "Service",
                "Logs",
                ValidationStatus.WARNING,
                "Serial Number already exists in the file"
            ),
            DataPreviewRow(
                "1",
                false,
                "Fresenius",
                "4008S",
                "4545885N70",
                "Dialysis",
                "Dialysis Machine",
                "Down",
                "Logs",
                ValidationStatus.ERROR,
                "Serial Number is blank"
            ),
            DataPreviewRow(
                "1",
                false,
                "Fresenius",
                "4008S",
                "4545885N70",
                "Dialysis",
                "Dialysis Machine",
                "Down",
                "Logs",
                ValidationStatus.ERROR,
                "Serial Number already exists in the file"
            ),
            DataPreviewRow(
                "1",
                false,
                "Fresenius",
                "4008S",
                "4545885N70",
                "Dialysis",
                "Dialysis Machine",
                "Down",
                "Logs",
                ValidationStatus.ERROR,
                "Serial Number already exists in the file"
            ),
            DataPreviewRow(
                "1",
                false,
                "Fresenius",
                "4008S",
                "4545885N70",
                "Dialysis",
                "Dialysis Machine",
                "Down",
                "Logs",
                ValidationStatus.ERROR,
                "Serial Number already exists in the file"
            ),
            DataPreviewRow(
                "1",
                false,
                "Fresenius",
                "4008S",
                "4545885N70",
                "Dialysis",
                "Dialysis Machine",
                "Down",
                "Logs",
                ValidationStatus.ERROR,
                "Serial Number already exists in the file"
            ),
            DataPreviewRow(
                "1",
                false,
                "Fresenius",
                "4008S",
                "4545885N70",
                "Dialysis",
                "Dialysis Machine",
                "Down",
                "Logs",
                ValidationStatus.ERROR,
                "Serial Number already exists in the file"
            ),
        )
        BioMedDataPreviewTable(rows = sampleRows, modifier = Modifier.padding(10.dp))
    }
}