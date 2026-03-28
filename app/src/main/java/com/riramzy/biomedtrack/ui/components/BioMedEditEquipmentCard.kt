package com.riramzy.biomedtrack.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun BioMedEditEquipmentCard(
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
        Column {
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
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Edit Equipment",
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
                            text = "Edit the equipment details below",
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 4.dp),
                            color = if (isSystemInDarkTheme()) {
                                MaterialTheme.colorScheme.secondary
                            } else {
                                MaterialTheme.colorScheme.secondary
                            }
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(745.dp)
                    .padding(bottom = 15.dp),
            ) {
                item {
                    BioMedHeader(
                        title = "Equipment Details",
                        height = 45,
                        textSize = 16,
                        modifier = Modifier
                            .padding(
                                start = 20.dp,
                                end = 20.dp,
                                bottom = 15.dp
                            )
                            .clip(
                                shape = RoundedCornerShape(15.dp)
                            )
                    )
                }

                item {
                    BioMedTextField(
                        title = "Name",
                        isNoteCard = false
                    )
                }

                item {
                    BioMedTextField(
                        title = "Model",
                        isNoteCard = false
                    )
                }

                item {
                    BioMedTextField(
                        title = "Serial Number",
                        isNoteCard = false
                    )
                }

                item {
                    BioMedTextField(
                        title = "Manufacturer",
                        isNoteCard = false
                    )
                }

                item {
                    BioMedTextField(
                        title = "Manufacturer",
                        isNoteCard = false
                    )
                }
                item {
                    BioMedTextField(
                        title = "Agent",
                        isNoteCard = false
                    )
                }

                item {
                    BioMedHeader(
                        title = "Classification",
                        height = 45,
                        textSize = 16,
                        modifier = Modifier
                            .padding(
                                start = 20.dp,
                                end = 20.dp,
                                bottom = 15.dp
                            )
                            .clip(
                                shape = RoundedCornerShape(15.dp)
                            )
                    )
                }

                item {
                    BioMedSelector(
                        title = "Category",
                        placeholder = "Dialysis Machine",
                        items = listOf(
                            "Dialysis Machine",
                            "Dialysis Unit",
                            "Dialysis System"
                        )
                    )
                }

                item {
                    BioMedSelector(
                        title = "Department",
                        placeholder = "Dialysis Unit",
                        items = listOf(
                            "Dental",
                            "OR",
                            "ICU",
                            "PICU"
                        )
                    )
                }

                item {
                    BioMedSelector(
                        title = "Current Status",
                        placeholder = "Online",
                        items = listOf(
                            "Down",
                            "Due Service"
                        )
                    )
                }

                item {
                    BioMedHeader(
                        title = "Location and Dates",
                        height = 45,
                        textSize = 16,
                        modifier = Modifier
                            .padding(
                                start = 20.dp,
                                end = 20.dp,
                                bottom = 15.dp
                            )
                            .clip(
                                shape = RoundedCornerShape(15.dp)
                            )
                    )
                }

                item {
                    BioMedSelector(
                        title = "Location",
                        placeholder = "Dialysis Unit",
                        items = listOf(
                            "Dental",
                            "OR",
                            "ICU",
                            "PICU"
                        )
                    )
                }

                item {
                    BioMedDateSelector(
                        title = "Installation Date"
                    )
                }

                item {
                    BioMedDateSelector(
                        title = "Contract End Date"
                    )
                }

                item {
                    BioMedPhotoDocumentationCard(
                        title = "Photo Documentation",
                        description = "Take photos of installation reports or documentations"
                    )
                }
            }

            Row(
                modifier = Modifier
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 15.dp
                    )
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                BioMedButton(
                    modifier = Modifier.padding(end = 10.dp),
                    text = "Save",
                    customColor = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                    customTextColor = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onPrimary
                    }
                )

                BioMedButton(
                    text = "Cancel",
                    customColor = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.primaryContainer
                    },
                    customTextColor = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    } else {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    }
                )
            }
        }
    }
}

@Preview(device = "id:pixel_9", showBackground = true)
@Composable
fun BioMedEditEquipmentCardPreview() {
    BioMedTheme {
        BioMedEditEquipmentCard()
    }
}

@Preview(device = "id:pixel_9", showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedEditEquipmentCardDarkPreview() {
    BioMedTheme {
        BioMedEditEquipmentCard()
    }
}