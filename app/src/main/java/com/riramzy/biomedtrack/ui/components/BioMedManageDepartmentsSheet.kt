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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun BioMedManageDepartmentsSheet() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BioMedUserHeader(
            withMoreButton = false
        )

        Text(
            text = "Manage Departments",
            style = MaterialTheme.typography.titleLarge,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSystemInDarkTheme()) {
                Color.White
            } else {
                Color.Black
            },
            modifier = Modifier
                .fillMaxWidth()
        )

        LazyColumn (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.height(327.dp)
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BioMedToggle(text = "Intensive Care Unit (A)", modifier = Modifier.width(356.dp), isChecked = false)
                    BioMedToggle(text = "Intensive Care Unit (A)", modifier = Modifier.width(356.dp))
                    BioMedToggle(text = "Intensive Care Unit (A)", modifier = Modifier.width(356.dp))
                    BioMedToggle(text = "Intensive Care Unit (A)", modifier = Modifier.width(356.dp))
                    BioMedToggle(text = "Intensive Care Unit (A)", modifier = Modifier.width(356.dp))
                    BioMedToggle(text = "Intensive Care Unit (A)", modifier = Modifier.width(356.dp))
                    BioMedToggle(text = "Intensive Care Unit (A)", modifier = Modifier.width(356.dp))
                    BioMedToggle(text = "Intensive Care Unit (A)", modifier = Modifier.width(356.dp))
                    BioMedToggle(text = "Intensive Care Unit (A)", modifier = Modifier.width(356.dp))
                    BioMedToggle(text = "Intensive Care Unit (A)", modifier = Modifier.width(356.dp))
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            BioMedButton(
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
                },
                modifier = Modifier.padding(end = 10.dp)
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

@Preview(showBackground = true, device = "id:pixel_9")
@Composable
fun BioMedManageDepartmentsSheetPreview() {
    BioMedTheme {
        BioMedManageDepartmentsSheet()
    }
}

@Preview(showBackground = true, device = "id:pixel_9",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF000000
)
@Composable
fun BioMedManageDepartmentsSheetDarkPreview() {
    BioMedTheme {
        BioMedManageDepartmentsSheet()
    }
}