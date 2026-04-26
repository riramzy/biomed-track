package com.riramzy.biomedtrack.ui.screens.notifications

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
fun NotificationsShimmer(
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.padding(end = 25.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ShimmerBox(Modifier.height(20.dp).width(100.dp).shimmerEffect())
                        ShimmerBox(Modifier.height(20.dp).width(150.dp).shimmerEffect())
                    }

                    ShimmerBox(Modifier.height(32.dp).width(150.dp).shimmerEffect())
                }

                ShimmerBox(Modifier.height(32.dp).shimmerEffect())
            }
        }

        item {
            repeat(15) {
                ShimmerBox(modifier = Modifier.padding(bottom = 10.dp).fillMaxWidth().height(190.dp).shimmerEffect())
            }
        }
    }
}

@Preview(device = "id:pixel_9", showBackground = true)
@Composable
fun NotificationsShimmerPreview() {
    BioMedTheme {
        NotificationsShimmer()
    }
}

@Preview(device = "id:pixel_9", showBackground = true, showSystemUi = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF000000
)
@Composable
fun NotificationsShimmerDarkPreview() {
    BioMedTheme {
        NotificationsShimmer()
    }
}