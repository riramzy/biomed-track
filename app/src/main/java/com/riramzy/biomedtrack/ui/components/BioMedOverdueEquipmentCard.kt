package com.riramzy.biomedtrack.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.ui.theme.indicatorColors

@Composable
fun BioMedOverdueEquipmentCard(
    modifier: Modifier = Modifier,
    status: String = "Service",
) {
    Card(
        modifier = modifier
            .width(328.dp)
            .height(78.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .padding(15.dp)
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(
                        when(status) {
                            "Online" -> MaterialTheme.indicatorColors.green
                            "Down" -> MaterialTheme.indicatorColors.red
                            "Service" -> MaterialTheme.indicatorColors.yellow
                            else -> MaterialTheme.colorScheme.primary
                        }
                    ),
            ) {
                Icon(
                    painter = when(status) {
                        "Online" -> painterResource(id = R.drawable.activity_online)
                        "Down" -> painterResource(id = R.drawable.activity_down)
                        "Service" -> painterResource(id = R.drawable.activity_service)
                        else -> {}
                    } as Painter,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .padding(0.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Fresenius 4008S",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSystemInDarkTheme()) {
                        Color.Black
                    } else {
                        Color.Black
                    }

                )
                Text(
                    text = "4545885N70",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSystemInDarkTheme()) {
                        Color.Black
                    } else {
                        Color.Black
                    }
                )

                Text(
                    text = "Dialysis Unit",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSystemInDarkTheme()) {
                        Color.Black
                    } else {
                        Color.Black
                    }
                )
            }

            Column(
                modifier = Modifier
                    .padding(end = 15.dp),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "20/3/2026",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF980000)

                )

                Text(
                    text = "Hemodialysis",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Black
                )
            }
        }
    }
}

@Preview(device = "id:pixel_9", showBackground = true)
@Composable
fun BioMedOverdueEquipmentCardPreview() {
    BioMedTheme {
        BioMedOverdueEquipmentCard()
    }
}

@Preview(device = "id:pixel_9", showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedOverdueEquipmentCardDarkPreview() {
    BioMedTheme {
        BioMedOverdueEquipmentCard()
    }
}