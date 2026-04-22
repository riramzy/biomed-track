package com.riramzy.biomedtrack.ui.components.equipment

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.components.custom.BioMedTextField
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.ui.theme.indicatorColors
import com.riramzy.biomedtrack.utils.EquipmentStatus

@Composable
fun BioMedChangeStatusDialog(
    modifier: Modifier = Modifier,
    equipmentName: String = "Fresenius 4008S",
    onDismiss: () -> Unit = {},
    onConfirm: (EquipmentStatus, String) -> Unit = { _, _ -> }
) {
    var selectedStatus by remember { mutableStateOf<EquipmentStatus?>(null) }
    var note by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = {},
        dismissButton = {
            BioMedButton(
                text = "Dismiss",
                onClick = { onDismiss() },
                customColor = MaterialTheme.colorScheme.inversePrimary,
                customTextColor = MaterialTheme.colorScheme.primary
            )
        },
        confirmButton = {
            BioMedButton(
                text = "Confirm",
                onClick = {
                    selectedStatus?.let {
                        onConfirm(it, note)
                    }
                          },
                customColor = MaterialTheme.colorScheme.primary,
                customTextColor = MaterialTheme.colorScheme.onPrimary
            )
        },
        title = {
            Text(
                text = "Please select the new status of $equipmentName",
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                InputChip(
                    selected =  selectedStatus == EquipmentStatus.ONLINE,
                    onClick = { selectedStatus = EquipmentStatus.ONLINE },
                    label = { Text("Online") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.activity_online),
                            modifier = Modifier
                                .size(18.dp),
                            tint = MaterialTheme.indicatorColors.green,
                            contentDescription = "Online"
                        )
                                  },
                    colors = InputChipDefaults.inputChipColors(
                        containerColor = if (isSystemInDarkTheme()) {
                            Color.Black
                        } else {
                            Color.White
                        },
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    border = BorderStroke(
                        color = MaterialTheme.colorScheme.primary,
                        width = 1.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                InputChip(
                    selected = selectedStatus == EquipmentStatus.SERVICE,
                    onClick = { selectedStatus = EquipmentStatus.SERVICE },
                    label = { Text("Service") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.activity_service),
                            modifier = Modifier
                                .size(18.dp),
                            tint = MaterialTheme.indicatorColors.yellow,
                            contentDescription = "Service"
                        )
                    },
                    colors = InputChipDefaults.inputChipColors(
                        containerColor = if (isSystemInDarkTheme()) {
                            Color.Black
                        } else {
                            Color.White
                        },
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    border = BorderStroke(
                        color = MaterialTheme.colorScheme.primary,
                        width = 1.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                InputChip(
                    selected = selectedStatus == EquipmentStatus.DOWN,
                    onClick = { selectedStatus = EquipmentStatus.DOWN },
                    label = { Text("Down") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.activity_down),
                            modifier = Modifier
                                .size(18.dp),
                            tint = MaterialTheme.indicatorColors.red,
                            contentDescription = "Down"
                        )
                    },
                    colors = InputChipDefaults.inputChipColors(
                        containerColor = if (isSystemInDarkTheme()) {
                            Color.Black
                        } else {
                            Color.White
                        },
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    border = BorderStroke(
                        color = MaterialTheme.colorScheme.primary,
                        width = 1.dp
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )

                BioMedTextField(
                    label = "Reason for change",
                    placeholder = "e.g., Water leak, PM due",
                    isNoteCard = true,
                    value = note,
                    onValueChange = { note = it },
                    modifier = Modifier
                        .padding(top = 15.dp)
                        .fillMaxWidth()
                )
            }
        },
        modifier = modifier.wrapContentSize(),
        shape = RoundedCornerShape(25.dp),
        containerColor = if (isSystemInDarkTheme()) {
            Color.Black
        } else {
            Color.White
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun BioMedChangeStatusDialogPreview() {
    BioMedTheme {
        BioMedChangeStatusDialog()
    }
}

@Preview(showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedChangeStatusDialogDarkPreview() {
    BioMedTheme {
        BioMedChangeStatusDialog()
    }
}