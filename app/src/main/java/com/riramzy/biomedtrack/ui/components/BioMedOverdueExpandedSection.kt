package com.riramzy.biomedtrack.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.ui.theme.indicatorColors

@Composable
fun BioMedOverdueExpandedSection(
    modifier: Modifier = Modifier,
    overdueEquipment: Int = 3
) {
    Card(
        modifier = modifier
            .width(356.dp)
            .height(327.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.indicatorColors.red.copy(0.2f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = "Overdue Maintenance ($overdueEquipment)",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF980000),
                    modifier = Modifier.padding(bottom = 15.dp)
                )

                for (overdueEquipment in 1..overdueEquipment) {
                    BioMedOverdueEquipmentCard(
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }
            }
        }
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true)
@Composable
fun BioMedOverdueExpandedSectionPreview() {
    BioMedTheme {
        BioMedOverdueExpandedSection()
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedOverdueExpandedSectionDarkPreview() {
    BioMedTheme {
        BioMedOverdueExpandedSection()
    }
}