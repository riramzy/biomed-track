package com.riramzy.biomedtrack.ui.components.importing

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.ui.theme.indicatorColors

enum class LogType { SUCCESS, WARNING, ERROR }

data class ImportLog(
    val message: String,
    val type: LogType
)

@Composable
fun BioMedImportingProgressCard(
    progress: Float,
    logs: List<ImportLog>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(386.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy()
        )
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- Circular Progress Section ---
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(top = 15.dp)
                    .size(115.dp)
            ) {
                // Background Track (White)
                CircularProgressIndicator(
                    progress = { 1f },
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    trackColor = Color.White,
                    strokeWidth = 12.dp,
                    strokeCap = StrokeCap.Round,
                )
                // Progress Arc (Primary Blue)
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = Color.White,
                    strokeWidth = 12.dp,
                    strokeCap = StrokeCap.Round,
                )
                // Percentage Text
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp,
                    color = if (isSystemInDarkTheme()) {
                        Color.White
                    } else {
                        Color.Black
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Title & Subtitle ---
            Text(
                text = "Importing Equipment",
                style = MaterialTheme.typography.headlineSmall,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (isSystemInDarkTheme()) {
                    Color.White
                } else {
                    Color.Black
                }
            )
            Text(
                text = "Please do not close the app",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = if (isSystemInDarkTheme()) {
                    Color.White
                } else {
                    Color.Black
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // --- Live Log Section ---
            Column(
                modifier = Modifier
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Live Log",
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSystemInDarkTheme()) {
                        Color.White
                    } else {
                        Color.Black
                    },
                    modifier = Modifier.padding(bottom = 15.dp)
                )

                // List of logs
                logs.forEach { log ->
                    ImportLogItem(log)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun ImportLogItem(
    log: ImportLog
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        val (iconRes, color) = when (log.type) {
            LogType.SUCCESS -> R.drawable.activity_online to MaterialTheme.indicatorColors.green
            LogType.WARNING -> R.drawable.warning to MaterialTheme.indicatorColors.yellow
            LogType.ERROR -> R.drawable.error to MaterialTheme.indicatorColors.red
        }

        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }

        Text(
            text = log.message,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSystemInDarkTheme()) {
                Color.White
            } else {
                Color.Black
            }
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_9")
@Composable
fun BioMedImportingProgressCardPreview() {
    BioMedTheme {
        BioMedImportingProgressCard(
            progress = 0.2f,
            logs = listOf(
                ImportLog("Imported Fresenius 4008S - 4545885N70", LogType.SUCCESS),
                ImportLog("4 rows skipped successfully", LogType.WARNING),
                ImportLog("Failed to import 2 rows", LogType.ERROR)
            )
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_9", backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedImportingProgressCardDarkPreview() {
    BioMedTheme {
        BioMedImportingProgressCard(
            progress = 0.4f,
            logs = listOf(
                ImportLog("Imported Fresenius 4008S - 4545885N70", LogType.SUCCESS),
                ImportLog("4 rows skipped successfully", LogType.WARNING),
                ImportLog("Failed to import 2 rows", LogType.ERROR)
            )
        )
    }
}