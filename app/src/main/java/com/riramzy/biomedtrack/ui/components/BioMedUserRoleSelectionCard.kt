package com.riramzy.biomedtrack.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun BioMedUserRoleSelectionCard(
    modifier: Modifier = Modifier,
    role: String = "Supervisor",
    isSelected: Boolean = false
) {
    Card(
        modifier = modifier
            .width(328.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                if (isSystemInDarkTheme()) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.primary
                }
            } else {
                if (isSystemInDarkTheme()) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
                }
            }
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .padding(15.dp)
        ) {
            RadioButton(
                selected = isSelected,
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

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .padding(start = 15.dp)
            ) {
                Text(
                    text = role,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSystemInDarkTheme()) {
                        if (isSelected) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    } else {
                        if (isSelected) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    }
                )

                Text(
                    text = when (role) {
                        "Supervisor" -> "Full access to all department, can assign tasks and generate reports"
                        "Admin" -> "Full system access including users management and equipment administration"
                        "Technician" -> "Can view all equipment and log maintenance in assigned departments only"
                        else -> ""
                    },
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 15.sp,
                    color = if (isSystemInDarkTheme()) {
                        if (isSelected) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    } else {
                        if (isSelected) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    }
                )
            }
        }
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true)
@Composable
fun BioMedUserRoleSelectionCardPreview() {
    BioMedTheme {
        BioMedUserRoleSelectionCard()
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true,
    backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedUserRoleSelectionCardDarkPreview() {
    BioMedTheme {
        BioMedUserRoleSelectionCard()
    }
}