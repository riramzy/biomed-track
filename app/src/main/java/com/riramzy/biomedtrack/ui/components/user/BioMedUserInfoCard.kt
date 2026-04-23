package com.riramzy.biomedtrack.ui.components.user

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.components.custom.BioMedToggle
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun BioMedUserInfoCard(
    modifier: Modifier = Modifier,
    username: String = "Bruce Wayne",
    role: String = "Technician",
    departments: List<String>? = listOf("ICU", "OPD", "PCU", "Dialysis Unit"),
    onManageDepartmentsClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .width(386.dp)
            .height(200.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
            } else {
                MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BioMedUserHeader(
                username = username,
                role = role
            )

            if (role == "Technician") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    departments?.forEach {
                        BioMedDepartmentPill(
                            departmentName = it
                        )
                    }
                }
            }

            if (role == "Supervisor") {
                Text(
                    text = "Access to all departments",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp),
                    textAlign = TextAlign.Start
                )
            }

            if (role == "Admin") {
                Text(
                    text = "Full system access",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp),
                    textAlign = TextAlign.Start
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (role == "Admin") Arrangement.End else Arrangement.SpaceBetween
            ) {
                if (role == "Supervisor" || role == "Technician") {
                    BioMedButton(
                        modifier = Modifier
                            .width(235.dp)
                            .height(35.dp),
                        text = "Manage Departments",
                        customTextSize = 12,
                        textAlignment = TextAlign.Start,
                        customTextColor = if (isSystemInDarkTheme()) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.primary
                        },
                        onClick = { onManageDepartmentsClick() }
                    )
                }

                Card(
                    modifier = Modifier
                        .width(114.dp)
                        .height(35.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSystemInDarkTheme()) {
                            MaterialTheme.colorScheme.secondaryContainer
                        } else {
                            MaterialTheme.colorScheme.primaryContainer
                        }
                    )
                ) {
                    BioMedToggle(
                        text = "Active"
                    )
                }
            }
        }
    }
}

@Composable
fun BioMedDepartmentPill(
    modifier: Modifier = Modifier,
    departmentName: String = "ICU"
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.primaryContainer
            }
        )
    ) {
        Text(
            text = departmentName,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true)
@Composable
fun BioMedUserInfoCardPreview() {
    BioMedTheme {
        BioMedUserInfoCard()
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF000000
)
@Composable
fun BioMedUserInfoCardDarkPreview() {
    BioMedTheme {
        BioMedUserInfoCard()
    }
}