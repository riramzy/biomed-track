package com.riramzy.biomedtrack.ui.components.custom

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun BioMedProgressIndicator(
    steps: List<String>,
    currentStep: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        steps.forEachIndexed { index, label ->
            val isCompleted = index < currentStep
            val isActive = index == currentStep
            val isReached = index <= currentStep

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(5.dp)
                            .background(
                                when {
                                    index == 0 -> Color.Transparent
                                    isReached -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.primaryContainer
                                }
                            )
                    )

                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(
                                if (isReached) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.primaryContainer
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(
                                painter = painterResource(R.drawable.activity_online),
                                contentDescription = null,
                                tint = if (isSystemInDarkTheme()) {
                                    MaterialTheme.colorScheme.primaryContainer
                                } else {
                                    Color.White
                                },
                                modifier = Modifier.size(18.dp)
                            )
                        } else {
                            Text(
                                text = (index + 1).toString(),
                                color = if (isActive) MaterialTheme.colorScheme.onPrimary
                                else MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(5.dp)
                            .background(
                                when {
                                    index == steps.size - 1 -> Color.Transparent
                                    isCompleted -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.primaryContainer
                                }
                            )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9")
@Composable
fun BioMedProgressIndicatorPreview() {
    val steps = listOf("Select File", "Preview", "Import")
    BioMedTheme {
        BioMedProgressIndicator(steps = steps, currentStep = 3)
    }
}

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF000000
)
@Composable
fun BioMedProgressIndicatorDarkPreview() {
    val steps = listOf("Select File", "Preview", "Import")
    BioMedTheme {
        BioMedProgressIndicator(steps = steps, currentStep = 3)
    }
}