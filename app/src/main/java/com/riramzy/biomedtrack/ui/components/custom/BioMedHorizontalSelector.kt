package com.riramzy.biomedtrack.ui.components.custom

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun BioMedHorizontalSelector(
    modifier: Modifier = Modifier,
    items: List<String> = emptyList(),
    selectedItem: String? = null,
    onItemSelected: (String) -> Unit = {}
) {
    LazyRow(
        modifier = modifier
            .wrapContentWidth()
            .height(35.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            val isSelected = item == selectedItem

            TextButton(
                onClick = {
                    onItemSelected(item)
                },
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSystemInDarkTheme()) {
                        if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSecondary
                        }
                    } else {
                        if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.primaryContainer
                        }
                    }
                )
            ) {
                Text(
                    text = item,
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
            }
        }
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true)
@Composable
fun BioMedHorizontalSelectorPreview() {
    BioMedTheme {
        BioMedHorizontalSelector(
            items = listOf("All", "Dialysis Unit", "OR", "ICU", "PCU"),
            selectedItem = "OR"
        )
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedHorizontalSelectorDarkPreview() {
    BioMedTheme {
        BioMedHorizontalSelector(
            items = listOf("All", "Dialysis Unit", "OR", "ICU", "PCU"),
            selectedItem = "All"
        )
    }
}
