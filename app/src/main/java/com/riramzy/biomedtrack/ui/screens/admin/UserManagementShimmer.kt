package com.riramzy.biomedtrack.ui.screens.admin

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.riramzy.biomedtrack.ui.components.custom.ShimmerBox
import com.riramzy.biomedtrack.ui.components.custom.shimmerEffect
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun UserManagementShimmer(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp, top = 20.dp, start = 15.dp, end = 15.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ShimmerBox(Modifier.height(28.dp).width(220.dp).shimmerEffect())
            ShimmerBox(Modifier.height(16.dp).width(280.dp).shimmerEffect())
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp, start = 15.dp, end = 15.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            repeat(4) {
                ShimmerBox(Modifier.width(80.dp).height(80.dp).shimmerEffect())
            }
        }

        ShimmerBox(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp, start = 15.dp, end = 15.dp)
                .height(56.dp)
                .shimmerEffect()
        )

        ShimmerBox(
            Modifier
                .fillMaxWidth()
                .padding(bottom = 15.dp, start = 15.dp, end = 15.dp)
                .height(40.dp)
                .shimmerEffect()
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier.padding(start = 15.dp, end = 15.dp)
        ) {
            repeat(5) {
                ShimmerBox(Modifier.fillMaxWidth().height(100.dp).shimmerEffect())
            }
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_9")
@Composable
fun UserManagementShimmerPreview() {
    BioMedTheme {
        UserManagementShimmer()
    }
}

@Preview(showBackground = true, device = "id:pixel_9", backgroundColor = 0xFF000000, 
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun UserManagementShimmerDarkPreview() {
    BioMedTheme {
        UserManagementShimmer()
    }
}
