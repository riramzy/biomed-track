package com.riramzy.biomedtrack.ui.screens.dashboard

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
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
fun DashboardShimmer(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            ShimmerBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .shimmerEffect()
            )
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ShimmerBox(modifier = Modifier.weight(1f).height(80.dp).shimmerEffect())
                    ShimmerBox(modifier = Modifier.weight(1f).height(80.dp).shimmerEffect())
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ShimmerBox(modifier = Modifier.weight(1f).height(80.dp).shimmerEffect())
                    ShimmerBox(modifier = Modifier.weight(1f).height(80.dp).shimmerEffect())
                }
            }
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                ShimmerBox(modifier = Modifier.width(150.dp).height(24.dp).shimmerEffect()) // Section Title
                repeat(3) {
                    ShimmerBox(modifier = Modifier.fillMaxWidth().height(120.dp).shimmerEffect())
                }
            }
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                ShimmerBox(modifier = Modifier.width(180.dp).height(24.dp).shimmerEffect()) // Section Title
                repeat(2) {
                    ShimmerBox(modifier = Modifier.fillMaxWidth().height(90.dp).shimmerEffect())
                }
            }
        }
    }
}

@Preview
@Composable
fun DashboardShimmerPreview() {
    BioMedTheme {
        DashboardShimmer()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showSystemUi = false, showBackground = true, backgroundColor = 0xFF000000
)
@Composable
fun DashboardShimmerDarkPreview() {
    BioMedTheme {
        DashboardShimmer()
    }
}
