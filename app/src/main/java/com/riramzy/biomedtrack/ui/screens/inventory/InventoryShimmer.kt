package com.riramzy.biomedtrack.ui.screens.inventory

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
fun InventoryShimmer(
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
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ShimmerBox(Modifier.height(20.dp).width(100.dp).shimmerEffect())
                ShimmerBox(Modifier.height(20.dp).width(250.dp).shimmerEffect())

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ShimmerBox(Modifier.height(32.dp).shimmerEffect())
                    ShimmerBox(Modifier.height(32.dp).shimmerEffect())
                }
            }
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                repeat(10) {
                    ShimmerBox(modifier = Modifier.width(355.dp).height(190.dp).shimmerEffect())
                }
            }
        }
    }
}

@Preview(device = "id:pixel_9", showBackground = true)
@Composable
fun InventoryShimmerPreview() {
    BioMedTheme {
        InventoryShimmer()
    }
}

@Preview(device = "id:pixel_9", showBackground = true, showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF000000
)
@Composable
fun InventoryShimmerDarkPreview() {
    BioMedTheme {
        InventoryShimmer()
    }
}