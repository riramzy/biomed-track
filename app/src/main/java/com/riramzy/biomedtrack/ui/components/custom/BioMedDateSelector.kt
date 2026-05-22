package com.riramzy.biomedtrack.ui.components.custom

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.Timestamps.toDateString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BioMedDateSelector(
    modifier: Modifier = Modifier,
    title: String = "Date",
    selectedDate: Long = System.currentTimeMillis(),
    onDateSelected: (Long) -> Unit = {}
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.onSecondaryContainer
            } else {
                MaterialTheme.colorScheme.onPrimaryContainer
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.primaryContainer.copy(0.4f)
                    }
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.primaryContainer
                    }
                ),
                onClick = {
                    showDatePicker = true
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = selectedDate.toDateString(),
                        style = MaterialTheme.typography.labelLarge,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )

                    Icon(
                        painter = painterResource(R.drawable.date),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = {
                        showDatePicker = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                datePickerState.selectedDateMillis?.let { date ->
                                    onDateSelected(date)
                                }
                                showDatePicker = false
                            }
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showDatePicker = false
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(
                        state = datePickerState
                    )
                }
            }
        }
    }
}

@Composable
fun BioMedDateRangeSelector(
    modifier: Modifier = Modifier,
    startDate: Long = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000,
    endDate: Long = System.currentTimeMillis(),
    onConfirm: (Long, Long) -> Unit = { _, _ -> },
    onCancel: () -> Unit = {}
) {
    var selectedStartDate by remember { mutableLongStateOf(startDate) }
    var selectedEndDate by remember { mutableLongStateOf(endDate) }

    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Select Custom Date Range") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                BioMedDateSelector(
                    title = "Start Date",
                    selectedDate = selectedStartDate,
                    onDateSelected = {
                        selectedStartDate = it
                    },
                    modifier = Modifier
                )

                BioMedDateSelector(
                    title = "End Date",
                    selectedDate = selectedEndDate,
                    onDateSelected = {
                        selectedEndDate = it
                    },
                    modifier = Modifier
                )
            }
        },
        confirmButton = {
            BioMedButton(
                text = "Confirm",
                onClick = {
                    onConfirm(selectedStartDate, selectedEndDate)
                },
                customColor = MaterialTheme.colorScheme.primary,
                customTextColor = MaterialTheme.colorScheme.onPrimary
            )
        },
        dismissButton = {
            BioMedButton(
                text = "Cancel",
                onClick = onCancel
            )
        },
        modifier = modifier
    )
}

@Preview(device = "id:pixel_9", showBackground = true)
@Composable
fun BioMedDateSelectorPreview() {
    BioMedTheme {
        BioMedDateRangeSelector()
    }
}

@Preview(device = "id:pixel_9", showBackground = true, showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedDateSelectorDarkPreview() {
    BioMedTheme {
        BioMedDateSelector()
    }
}