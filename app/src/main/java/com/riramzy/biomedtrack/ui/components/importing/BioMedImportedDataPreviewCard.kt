package com.riramzy.biomedtrack.ui.components.importing

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
    modifier: Modifier = Modifier,
    totalRows: Int = 42,
    validRows: Int = 42,
    errorRows: Int = 3,
    warningRows: Int = 0
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = totalRows.toString(),
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
                    status = "$validRows Valid",
                    color = MaterialTheme.indicatorColors.green,
                    changeable = false,
                    onStatusClicked = {}
                )

                BioMedStatusIndicator(
                    status = "$warningRows Warnings",
                    color = MaterialTheme.indicatorColors.yellow,
                    changeable = false,
                    onStatusClicked = {}
                )

                BioMedStatusIndicator(
                    status = "$errorRows Errors",
                    color = MaterialTheme.indicatorColors.red,
                    changeable = false,
                    onStatusClicked = {}
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