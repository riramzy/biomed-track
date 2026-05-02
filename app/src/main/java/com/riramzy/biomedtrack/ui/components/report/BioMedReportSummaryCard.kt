package com.riramzy.biomedtrack.ui.components.report

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.ui.theme.indicatorColors

@Composable
fun BioMedReportSummaryCard(
    modifier: Modifier = Modifier,
    totalEquipment: Int = 275,
    healthy: Int = 220,
    dueService: Int = 23,
    down: Int = 17,
    logsCount: Int = 40
) {
    Card(
        modifier = modifier
            .wrapContentWidth()
            .height(203.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
            } else {
                MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
            }
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(15.dp)
                .fillMaxSize()
        ) {
            BioMedReportItemCard(
                icon = R.drawable.inventory,
                title = "Total Equipment",
                value = totalEquipment.toString()
            )

            BioMedReportItemCard(
                icon = R.drawable.activity_online,
                title = "Healthy",
                value = healthy.toString()
            )

            BioMedReportItemCard(
                icon = R.drawable.activity_service,
                title = "Service Due",
                value = dueService.toString()
            )

            BioMedReportItemCard(
                icon = R.drawable.activity_down,
                title = "Down",
                value = down.toString()
            )

            Text(
                text = "Reports will include $logsCount maintenance log entries for Down and Service Due Equipment",
                style = MaterialTheme.typography.labelSmall,
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal,
                color = if (isSystemInDarkTheme()) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.primary
                }
            )
        }
    }
}

@Composable
fun BioMedReportItemCard(
    modifier: Modifier = Modifier,
    icon: Int = R.drawable.inventory,
    title: String = "Total Equipment",
    value: String = "275"
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(30.dp)
                    .background(
                        color = if (isSystemInDarkTheme()) {
                            when (title) {
                                "Total Equipment" -> MaterialTheme.colorScheme.onSecondary
                                "Healthy" -> MaterialTheme.indicatorColors.green
                                "Service Due" -> MaterialTheme.indicatorColors.yellow
                                "Down" -> MaterialTheme.indicatorColors.red
                                else -> MaterialTheme.colorScheme.primary
                            }
                        } else {
                            when (title) {
                                "Total Equipment" -> MaterialTheme.colorScheme.secondary
                                "Healthy" -> MaterialTheme.indicatorColors.green
                                "Service Due" -> MaterialTheme.indicatorColors.yellow
                                "Down" -> MaterialTheme.indicatorColors.red
                                else -> MaterialTheme.colorScheme.primary
                            }
                        }
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(18.dp)
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(start = 10.dp),
                color = if (isSystemInDarkTheme()) {
                    Color.White
                } else {
                    Color.Black
                }
            )
        }

        Text(
            text = value,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSystemInDarkTheme()) {
                Color.White
            } else {
                Color.Black
            }
        )
    }
}

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9")
@Composable
fun BioMedReportSummaryCardPreview() {
    BioMedTheme {
        BioMedReportSummaryCard()
    }
}

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF000000
)
@Composable
fun BioMedReportSummaryCardDarkPreview() {
    BioMedTheme {
        BioMedReportSummaryCard()
    }
}