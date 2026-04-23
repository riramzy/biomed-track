package com.riramzy.biomedtrack.ui.components.schedule

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.ui.components.equipment.BioMedEquipmentStatusCard
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun BioMedScheduleDayCard(
    modifier: Modifier = Modifier,
    containsSchedules: Boolean = true
) {
    Card(
        onClick = { /*TODO*/ },
        modifier = modifier
            .height(170.dp)
            .width(356.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.secondaryContainer
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "SAT",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    } else {
                        Color.Black
                    }
                )

                Text(
                    text = "28 Mar",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.onSecondaryContainer
                    } else {
                        Color.Black
                    }
                )
            }

            if (containsSchedules) {
                BioMedEquipmentStatusCard(isAbbreviated = true)
            } else {
                Text(
                    text = "No maintenance",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    color = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.primary.copy(0.6f)
                    } else {
                        MaterialTheme.colorScheme.primary.copy(0.6f)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true)
@Composable
fun BioMedScheduleDayCardPreview() {
    BioMedTheme {
        BioMedScheduleDayCard()
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedScheduleDayCardDarkPreview() {
    BioMedTheme {
        BioMedScheduleDayCard()
    }
}