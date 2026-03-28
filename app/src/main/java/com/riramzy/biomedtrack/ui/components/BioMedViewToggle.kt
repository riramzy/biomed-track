package com.riramzy.biomedtrack.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun BioMedViewToggle(
    modifier: Modifier = Modifier
) {
    var weekOrList by remember { mutableStateOf("List") }

    Card(
        modifier = modifier
            .height(31.dp)
            .width(220.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.primaryContainer.copy(0.5f)
            }
        )
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Week View",
                style = MaterialTheme.typography.labelLarge,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .padding(2.dp)
                    .clip(
                        RoundedCornerShape(25.dp)
                    )
                    .background(
                        color = if (weekOrList == "Week") {
                            MaterialTheme.colorScheme.onSecondary
                        } else {
                            Color.Unspecified
                        }
                    )
                    .clickable {
                        weekOrList = "Week"
                    },
                textAlign = TextAlign.Center,
                color = if (isSystemInDarkTheme()) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                    MaterialTheme.colorScheme.onPrimaryContainer
                }
            )

            Text(
                text = "List View",
                style = MaterialTheme.typography.labelLarge,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .padding(2.dp)
                    .clip(
                        RoundedCornerShape(25.dp)
                    )
                    .background(
                        color = if (weekOrList == "List") {
                            MaterialTheme.colorScheme.onSecondary
                        } else {
                            Color.Unspecified
                        }
                    )
                    .clickable {
                        weekOrList = "List"
                    },
                textAlign = TextAlign.Center,
                color = if (isSystemInDarkTheme()) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                    MaterialTheme.colorScheme.onPrimaryContainer
                }
            )
        }
    }
}

@Preview(device = "id:pixel_9", showBackground = true)
@Composable
fun BioMedViewTogglePreview() {
    BioMedTheme {
        BioMedViewToggle()
    }
}

@Preview(device = "id:pixel_9", showBackground = true, showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedViewToggleDarkPreview() {
    BioMedTheme {
        BioMedViewToggle()
    }
}