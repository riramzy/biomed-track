package com.riramzy.biomedtrack.ui.components.custom

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.domain.model.ChecklistItem
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun BioMedMultipleItemsSelector(
    modifier: Modifier = Modifier,
    items: List<ChecklistItem> = emptyList(),
    onToggle: (ChecklistItem) -> Unit = {},
    onAddNewItem: (String) -> Unit = {},
    onRemoveItem: (ChecklistItem) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.log_maint_tasks_completed),
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
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    IconButton(
                        onClick = {
                            onToggle(item)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(25.dp)
                            .weight(4f),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                painter = if (item.isChecked) painterResource(R.drawable.checked) else painterResource(
                                    R.drawable.unchecked
                                ),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .size(18.dp)
                            )

                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelLarge,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                color = if (isSystemInDarkTheme()) {
                                    MaterialTheme.colorScheme.onSecondaryContainer
                                } else {
                                    Color.Black
                                }
                            )
                        }
                    }

                    Icon(
                        painter = painterResource(R.drawable.error),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable { onRemoveItem(item) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        var customItemLabel by remember { mutableStateOf("") }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BioMedTextField(
                placeholder = stringResource(R.string.multiple_selector_add_placeholder),
                isWithLabel = false,
                value = customItemLabel,
                onValueChange = { customItemLabel = it },
                modifier = Modifier.weight(3f)
            )

            BioMedButton(
                text = stringResource(R.string.multiple_selector_btn_add),
                withIcon = false,
                customColor = MaterialTheme.colorScheme.primary,
                customTextColor = MaterialTheme.colorScheme.onPrimary,
                onClick = {
                    if (customItemLabel.isNotBlank()) {
                        onAddNewItem(customItemLabel)
                        customItemLabel = ""
                    }
                },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true)
@Composable
fun BioMedMultipleItemsSelectorPreview() {
    BioMedTheme {
        BioMedMultipleItemsSelector(
            items = listOf(
                ChecklistItem(id = "1", label = "Task 1", isChecked = true),
                ChecklistItem(id = "2", label = "Task 2", isChecked = false),
                ChecklistItem(id = "3", label = "Task 3", isChecked = true),
                ChecklistItem(id = "4", label = "Task 4", isChecked = false),
                ChecklistItem(id = "5", label = "Task 5", isChecked = true)
            )
        )
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF000000, locale = "ar"
)
@Composable
fun BioMedMultipleItemsSelectorDarkPreview() {
    BioMedTheme {
        BioMedMultipleItemsSelector(
            items = listOf(
                ChecklistItem(id = "1", label = "Task 1", isChecked = true),
                ChecklistItem(id = "2", label = "Task 2", isChecked = false),
                ChecklistItem(id = "3", label = "Task 3", isChecked = true),
                ChecklistItem(id = "4", label = "Task 4", isChecked = false),
                ChecklistItem(id = "5", label = "Task 5", isChecked = true)
            )
        )
    }
}