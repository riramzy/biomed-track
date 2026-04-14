package com.riramzy.biomedtrack.ui.screens.equipment

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.riramzy.biomedtrack.ui.components.custom.ShimmerBox
import com.riramzy.biomedtrack.ui.components.custom.shimmerEffect
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun EquipmentDetailsShimmer(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ShimmerBox(modifier = Modifier.width(380.dp).height(600.dp).shimmerEffect())
            }
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(15.dp),
            ) {
                ShimmerBox(modifier = Modifier.width(300.dp).height(30.dp).shimmerEffect())
                repeat(5) {
                    ShimmerBox(modifier = Modifier.fillMaxWidth().height(145.dp).shimmerEffect())
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_9")
@Composable
fun EquipmentDetailsShimmerPreview() {
    BioMedTheme {
        EquipmentDetailsShimmer()
    }
}

@Preview(showBackground = true, device = "id:pixel_9",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showSystemUi = false, backgroundColor = 0xFF000000
)
@Composable
fun EquipmentDetailsShimmerDarkPreview() {
    BioMedTheme {
        EquipmentDetailsShimmer()
    }
}