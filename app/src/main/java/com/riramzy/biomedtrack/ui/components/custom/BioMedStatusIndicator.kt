package com.riramzy.biomedtrack.ui.components.custom

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.ui.theme.indicatorColors
import com.riramzy.biomedtrack.utils.EquipmentStatus

@Composable
fun BioMedStatusIndicator(
    modifier: Modifier = Modifier,
    status: String = "Assigned",
    color: Color = MaterialTheme.indicatorColors.green,
    changeable: Boolean = true,
    onStatusClicked: () -> Unit = {}
) {
    val statusColor = when (status) {
        EquipmentStatus.ONLINE.name -> MaterialTheme.indicatorColors.green
        EquipmentStatus.DOWN.name -> MaterialTheme.indicatorColors.red
        EquipmentStatus.SERVICE.name -> MaterialTheme.indicatorColors.yellow
        "LOG" -> if (isSystemInDarkTheme()) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.secondary
        }

        "ASSIGNED" -> if (isSystemInDarkTheme()) {
            MaterialTheme.colorScheme.tertiaryContainer
        } else {
            MaterialTheme.colorScheme.tertiary
        }

        else -> if (isSystemInDarkTheme()) {
            color
        } else {
            color
        }
    }
    val statusText = when (status) {
        EquipmentStatus.ONLINE.name -> stringResource(R.string.status_online)
        EquipmentStatus.DOWN.name -> stringResource(R.string.status_down)
        EquipmentStatus.SERVICE.name -> stringResource(R.string.status_service)
        "LOG" -> stringResource(R.string.status_log)
        "ASSIGNED" -> stringResource(R.string.status_assigned)
        else -> status
    }

    Card(
        modifier = modifier
            .wrapContentHeight()
            .wrapContentWidth()
            .heightIn(min = 25.dp)
            .widthIn(min = 100.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = statusColor
        ),
        onClick = { onStatusClicked() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 6.dp, vertical = 4.dp)
                .width(90.dp)
        ) {
            Text(
                text = statusText,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White,
                textAlign = TextAlign.Center,
            )

            if (changeable) {
                IconButton(
                    onClick = { onStatusClicked() },
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(10.dp)
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
}

@Preview(showBackground = true, showSystemUi = false,
    device = "id:pixel_9", locale = "ar"
)
@Composable
fun BioMedStatusIndicatorPreview() {
    BioMedTheme {
        BioMedStatusIndicator(status = "LOG", onStatusClicked = {})
    }
}

@Preview(showBackground = true, showSystemUi = false,
    device = "id:pixel_9",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedStatusIndicatorDarkPreview() {
    BioMedTheme {
        BioMedStatusIndicator(status = "ASSIGNED", onStatusClicked = {})
    }
}
