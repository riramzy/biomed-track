package com.riramzy.biomedtrack.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun BioMedInsightCard(
    modifier: Modifier = Modifier,
    value: String,
    status: String = "Online"
) {
    Card(
        modifier = modifier
            .size(170.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.onSecondary
            } else {
                MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
            }
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = when(status) {
                        "Online" -> "Healthy"
                        "Down" -> "Currently Down"
                        "Service" -> "Due Service"
                        else -> "Total Equipment"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = if (isSystemInDarkTheme()) {
                        Color.White
                    } else {
                        Color.Black
                    }
                )

                Icon(
                    painter = when(status) {
                        "Online" -> painterResource(R.drawable.insight_online)
                        "Down" -> painterResource(R.drawable.insight_down)
                        "Service" -> painterResource(R.drawable.insight_service)
                        else -> painterResource(R.drawable.inventory)
                    },
                    modifier = Modifier
                        .size(20.dp),
                    tint = when(status) {
                        "Online" -> MaterialTheme.indicatorColors.green
                        "Down" -> MaterialTheme.indicatorColors.red
                        "Service" -> MaterialTheme.indicatorColors.yellow
                        else -> if (isSystemInDarkTheme()) {
                            Color.White
                        } else {
                            Color.Black
                        }
                    },
                    contentDescription = null
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 15.dp,
                        end = 15.dp,
                        top = 10.dp,
                        bottom = 40.dp
                        ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    fontSize = 32.sp,
                    color = when(status) {
                        "Online" -> MaterialTheme.indicatorColors.green
                        "Down" -> MaterialTheme.indicatorColors.red
                        "Service" -> MaterialTheme.indicatorColors.yellow
                        else -> if (isSystemInDarkTheme()) {
                            Color.White
                        } else {
                            Color.Black
                        }
                    },
                )
                Text(
                    text = when(status) {
                        "Online" -> "Operating normally"
                        "Down" -> "Requires repair"
                        "Service" -> "Needs attention"
                        else -> "Across all departments"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    color = if (isSystemInDarkTheme()) {
                        Color.White.copy(0.5f)
                    } else {
                        Color.Black.copy(0.5f)
                    }
                )
            }
        }
    }
}

@Preview(
    device = "spec:width=411dp,height=891dp", showSystemUi = false,
    showBackground = true
)
@Composable
fun BioMedInsightCardPreview() {
    BioMedTheme {
        BioMedInsightCard(
            value = "220",
            status = "Service"
        )
    }
}

@Preview(
    device = "spec:width=411dp,height=891dp", showSystemUi = false,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedInsightCardDarkPreview() {
    BioMedTheme {
        BioMedInsightCard(
            value = "23",
            status = "Down"
        )
    }
}