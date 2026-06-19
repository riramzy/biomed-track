package com.riramzy.biomedtrack.ui.components.equipment

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
    val isDark = isSystemInDarkTheme()
    var selectedStatus by remember { mutableStateOf<EquipmentStatus?>(null) }
    var note by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) MaterialTheme.colorScheme.onSecondary else Color.White
            ),
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(15.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Image(
                        painter = painterResource(R.drawable.status_change),
                        contentDescription = null,
                        modifier = Modifier
                            .size(34.dp),
                        colorFilter = ColorFilter.tint(
                            if (isDark) Color.White else Color.Black
                        )
                    )

                    Text(
                        text = stringResource(R.string.field_status_placeholder, equipmentName),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White else Color.Black
                    )

                    Text(
                        text = stringResource(R.string.select_new_status_format, equipmentName),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = if (isDark) Color.White else Color.Black
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    InputChip(
                        selected = selectedStatus == EquipmentStatus.ONLINE,
                        onClick = { selectedStatus = EquipmentStatus.ONLINE },
                        label = { Text(stringResource(R.string.status_online)) },
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
                            containerColor = if (isDark) MaterialTheme.colorScheme.onSecondary else Color.White,
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        border = BorderStroke(
                            color = MaterialTheme.colorScheme.primary,
                            width = 1.dp
                        ),
                        shape = CircleShape,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    InputChip(
                        selected = selectedStatus == EquipmentStatus.SERVICE,
                        onClick = { selectedStatus = EquipmentStatus.SERVICE },
                        label = { Text(stringResource(R.string.status_service)) },
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
                            containerColor = if (isDark) MaterialTheme.colorScheme.onSecondary else Color.White,
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        border = BorderStroke(
                            color = MaterialTheme.colorScheme.primary,
                            width = 1.dp
                        ),
                        shape = CircleShape,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    InputChip(
                        selected = selectedStatus == EquipmentStatus.DOWN,
                        onClick = { selectedStatus = EquipmentStatus.DOWN },
                        label = { Text(stringResource(R.string.status_down)) },
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
                            containerColor = if (isDark) MaterialTheme.colorScheme.onSecondary else Color.White,
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        border = BorderStroke(
                            color = MaterialTheme.colorScheme.primary,
                            width = 1.dp
                        ),
                        shape = CircleShape,
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    BioMedTextField(
                        label = stringResource(R.string.reason_for_change),
                        placeholder = stringResource(R.string.reason_for_change_placeholder),
                        isNoteCard = true,
                        value = note,
                        onValueChange = { note = it },
                        modifier = Modifier
                            .padding(top = 15.dp)
                            .fillMaxWidth()
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    BioMedButton(
                        text = stringResource(R.string.confirm),
                        onClick = {
                            selectedStatus?.let {
                                onConfirm(it, note)
                            }
                        },
                        customColor = MaterialTheme.colorScheme.primary,
                        customTextColor = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.weight(1f),
                    )

                    BioMedButton(
                        text = stringResource(R.string.dismiss),
                        onClick = { onDismiss() },
                        customColor = MaterialTheme.colorScheme.primaryContainer,
                        customTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true, locale = "ar")
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