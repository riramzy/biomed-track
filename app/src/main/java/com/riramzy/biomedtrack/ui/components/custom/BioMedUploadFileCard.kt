package com.riramzy.biomedtrack.ui.components.custom

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun BioMedUploadFileCard(
    modifier: Modifier = Modifier,
    onBrowseClick: () -> Unit = {}
) {
    val borderColor = if (isSystemInDarkTheme()) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.primary
    }

    val dashPathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

    Box(
        modifier = modifier
            .width(380.dp)
            .height(200.dp)
            .drawBehind {
                drawRoundRect(
                    color = borderColor,
                    style = Stroke(
                        width = 2.dp.toPx(),
                        pathEffect = dashPathEffect
                    ),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(25.dp.toPx())
                )
            }
            .clip(RoundedCornerShape(25.dp))
            .background(
                if (isSystemInDarkTheme()) {
                    MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
                } else {
                    MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.excel),
                contentDescription = null,
                modifier = Modifier.size(45.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Select your excel file",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black
            )

            Text(
                text = "Supports .xlsx and .xls formats",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(15.dp))

            BioMedButton(
                text = "Browse Files",
                modifier = Modifier.width(136.dp),
                customColor = MaterialTheme.colorScheme.primary,
                customTextColor = MaterialTheme.colorScheme.onPrimary,
                customTextSize = 11,
                onClick = onBrowseClick
            )
        }
    }
}

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9")
@Composable
fun BioMedUploadFileCardPreview() {
    BioMedTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            BioMedUploadFileCard()
        }
    }
}

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF000000
)
@Composable
fun BioMedUploadFileCardDarkPreview() {
    BioMedTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            BioMedUploadFileCard()
        }
    }
}