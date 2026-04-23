package com.riramzy.biomedtrack.ui.components.importing

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.ui.components.custom.BioMedStatusIndicator
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.ui.theme.indicatorColors

@Composable
fun BioMedImportedDataPreviewCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(386.dp)
            .height(110.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .padding(15.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column() {
                Text(
                    text = "42",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isSystemInDarkTheme()) {
                        Color.White
                    } else {
                        Color.Black
                    }
                )

                Text(
                    text = "Total rows found in file",
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BioMedStatusIndicator(
                    status = "42 Valid",
                    color = MaterialTheme.indicatorColors.green,
                    changeable = false
                )

                BioMedStatusIndicator(
                    status = "3 Errors",
                    color = MaterialTheme.indicatorColors.red,
                    changeable = false
                )

                BioMedStatusIndicator(
                    status = "0 Warnings",
                    color = MaterialTheme.indicatorColors.yellow,
                    changeable = false
                )
            }
        }
    }
}

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9")
@Composable
fun BioMedImportedDataPreviewCardPreview() {
    BioMedTheme {
        BioMedImportedDataPreviewCard()
    }
}

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9",
    backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedImportedDataPreviewCardDarkPreview() {
    BioMedTheme {
        BioMedImportedDataPreviewCard()
    }
}