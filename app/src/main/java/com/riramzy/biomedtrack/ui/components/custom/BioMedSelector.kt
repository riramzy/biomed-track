package com.riramzy.biomedtrack.ui.components.custom

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BioMedSelector(
    modifier: Modifier = Modifier,
    title: String,
    placeholder: String,
    items: List<String>,
    selectedItem: String = "",
    onItemSelected: (String) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 20.dp,
                bottom = 15.dp
            )
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
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier
                        .width(328.dp)
                        .height(45.dp)
                        .menuAnchor(
                            enabled = true,
                            type = MenuAnchorType.PrimaryNotEditable
                        ),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor =  if (isSystemInDarkTheme()) {
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
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = selectedItem.ifEmpty { placeholder },
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )

                        Icon(
                            painter = painterResource(R.drawable.dropdown),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
                    modifier = Modifier
                        .exposedDropdownSize()
                        .background(
                            color = if (isSystemInDarkTheme()) {
                                MaterialTheme.colorScheme.secondaryContainer
                            } else {
                                MaterialTheme.colorScheme.primaryContainer
                            }
                        ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    items.forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Text(item)
                            },
                            onClick = {
                                onItemSelected(item)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(device = "id:pixel_9", showBackground = true)
@Composable
fun BioMedSelectorPreview() {
    BioMedTheme {
        BioMedSelector(
            title = "Equipment",
            placeholder = "Select Equipment",
            items = listOf(
                "Fresenius 4008S",
                "Gambro AK96",
                "Fresenius 4008 Classic",
                "Gambro AK98",
                "B.Braun Dialog",
            )
        )
    }
}

@Preview(device = "id:pixel_9", showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedSelectorDarkPreview() {
    BioMedTheme {
        BioMedSelector(
            title = "Equipment",
            placeholder = "Select Equipment",
            items = listOf(
                "Fresenius 4008S",
                "Gambro AK96",
                "Fresenius 4008 Classic",
                "Gambro AK98",
                "B.Braun Dialog",
            )
        )
    }
}