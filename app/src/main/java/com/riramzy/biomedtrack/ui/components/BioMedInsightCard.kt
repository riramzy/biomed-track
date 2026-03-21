package com.riramzy.biomedtrack.ui.components

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
    icon: Int,
    title: String,
    value: String,
    description: String,
    isTotal: Boolean = true,
    isHealthy: Boolean = false,
    isService: Boolean = false,
    isDown: Boolean = false
) {
    Card(
        modifier = modifier
            .size(170.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(0.3f),
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
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                )

                Icon(
                    painter = painterResource(icon),
                    modifier = Modifier
                        .size(20.dp),
                    tint = if (isService) {
                        MaterialTheme.indicatorColors.yellow
                    } else if (isHealthy) {
                        MaterialTheme.indicatorColors.green
                    } else if (isDown) {
                        MaterialTheme.indicatorColors.red
                    } else {
                        Color.Black
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
                    color = if (isService) {
                        MaterialTheme.indicatorColors.yellow
                    } else if (isHealthy) {
                        MaterialTheme.indicatorColors.green
                    } else if (isDown) {
                        MaterialTheme.indicatorColors.red
                    } else {
                        Color.Black
                    },
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    color = Color.Black.copy(0.5f)
                )
            }
        }
    }
}

@Preview(device = "spec:width=1080px,height=2340px,dpi=640", showSystemUi = false,
    showBackground = true
)
@Composable
fun BioMedInsightCardPreview() {
    BioMedTheme {
        BioMedInsightCard(
            icon = R.drawable.insight_service,
            title = "Due Service",
            isService = true,
            value = "23",
            description = "Needs attention"
        )
    }
}