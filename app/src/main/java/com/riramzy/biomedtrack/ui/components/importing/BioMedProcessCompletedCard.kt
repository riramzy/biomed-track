package com.riramzy.biomedtrack.ui.components.importing

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toDrawable
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.ui.theme.indicatorColors

@Composable
fun BioMedProcessCompletedCard(
    modifier: Modifier = Modifier,
    successCount: Int = 42,
    warningCount: Int = 4,
    errorCount: Int = 2,
) {
    Card(
        modifier = modifier
            .width(386.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // --- Large Success Icon ---
            Box(
                modifier = Modifier
                    .size(115.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.indicatorColors.green), // Thick green border color
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.insight_online),
                    contentDescription = "Success",
                    tint = Color.White,
                    modifier = Modifier.size(80.dp)
                )
            }

            Spacer(modifier = Modifier.height(15.dp))

            // --- Title ---
            Text(
                text = "Import Complete",
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = if (isSystemInDarkTheme()) {
                    Color.White
                } else {
                    Color.Black
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- Summary List ---
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.Start
            ) {
                SummaryItem(
                    countText = "$successCount equipment imported successfully",
                    iconRes = R.drawable.activity_online,
                    color = MaterialTheme.indicatorColors.green
                )
                
                SummaryItem(
                    countText = "$warningCount rows skipped successfully",
                    iconRes = R.drawable.warning,
                    color = MaterialTheme.indicatorColors.yellow
                )
                
                SummaryItem(
                    countText = "Failed to import $errorCount rows",
                    iconRes = R.drawable.error,
                    color = MaterialTheme.indicatorColors.red
                )
            }
        }
    }
}

@Composable
private fun SummaryItem(
    countText: String,
    iconRes: Int,
    color: Color
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }

        Text(
            text = countText,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSystemInDarkTheme()) {
                Color.White
            } else {
                Color.Black
            }
        )
    }
}

@Preview(device = "id:pixel_9", showBackground = true)
@Composable
fun BioMedProcessCompletedCardPreview() {
    BioMedTheme {
        BioMedProcessCompletedCard()
    }
}

@Preview(device = "id:pixel_9", showBackground = true, showSystemUi = false,
    backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedProcessCompletedCardDarkPreview() {
    BioMedTheme {
        BioMedProcessCompletedCard()
    }
}
