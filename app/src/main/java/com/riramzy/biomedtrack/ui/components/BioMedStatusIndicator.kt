package com.riramzy.biomedtrack.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.ui.theme.indicatorColors

@Composable
fun BioMedStatusIndicator(
    modifier: Modifier = Modifier,
    status: String = "Online"
) {
    Card(
        modifier = modifier
            .width(72.dp)
            .height(19.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (status) {
                "Online" -> MaterialTheme.indicatorColors.green
                "Down" -> MaterialTheme.indicatorColors.red
                "Service" -> MaterialTheme.indicatorColors.yellow
                else -> if (isSystemInDarkTheme()) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.primary
                }
            }
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp)
        ) {
            Text(
                text = status,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 4.dp)
            )

            IconButton(
                onClick = {},
                modifier = Modifier.size(10.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.dropdown),
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

    }
}

@Preview(showBackground = true, showSystemUi = false,
    device = "id:pixel_9"
)
@Composable
fun BioMedStatusIndicatorPreview() {
    BioMedTheme {
        BioMedStatusIndicator(status = "Log")
    }
}

@Preview(showBackground = true, showSystemUi = false,
    device = "id:pixel_9",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedStatusIndicatorDarkPreview() {
    BioMedTheme {
        BioMedStatusIndicator(status = "Log")
    }
}
