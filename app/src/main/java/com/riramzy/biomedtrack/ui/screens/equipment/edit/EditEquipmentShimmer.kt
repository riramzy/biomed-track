package com.riramzy.biomedtrack.ui.screens.equipment.edit

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
fun EditEquipmentShimmer(
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
                ShimmerBox(modifier = Modifier.width(380.dp).height(700.dp).shimmerEffect())
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_9")
@Composable
fun EditEquipmentShimmerPreview() {
    BioMedTheme {
        EditEquipmentShimmer()
    }
}

@Preview(showBackground = true, device = "id:pixel_9", backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun EditEquipmentShimmerDarkPreview() {
    BioMedTheme {
        EditEquipmentShimmer()
    }
}