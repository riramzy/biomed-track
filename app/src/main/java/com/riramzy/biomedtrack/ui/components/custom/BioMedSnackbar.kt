package com.riramzy.biomedtrack.ui.components.custom

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun BioMedSnackbar(
    modifier: Modifier = Modifier,
    snackbarData: SnackbarData? = null,
    isError: Boolean = false
) {
    Card(
        modifier = modifier
            .heightIn(min = 65.dp)
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(15.dp),
        shape = RoundedCornerShape(25.dp),
        colors = cardColors(
            containerColor = if (isError) {
                MaterialTheme.colorScheme.tertiaryContainer
            } else {
                MaterialTheme.colorScheme.primaryContainer
            }
        )
    ) {
        Row(
            modifier = Modifier
                .padding(15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(3f)
            ) {
                Icon(
                    painter = painterResource(if (isError) R.drawable.warning else R.drawable.activity_online),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = if (isError) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                )

                Text(
                    text = snackbarData?.visuals?.message ?: "Message",
                    modifier = Modifier.padding(start = 15.dp),
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isError) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
                )
            }

            snackbarData?.visuals?.actionLabel?.let { action ->
                BioMedButton(
                    text = action,
                    onClick = { snackbarData.performAction() },
                    customColor = if (isError) {
                        MaterialTheme.colorScheme.tertiary
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                    customTextColor = if (isError) {
                        MaterialTheme.colorScheme.onTertiary
                    } else {
                        MaterialTheme.colorScheme.onPrimary
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9")
@Composable
fun BioMedSnackbarPreview() {
    BioMedTheme {
        val snackbarData = object : SnackbarData {
            override val visuals = object : SnackbarVisuals {
                override val message: String
                    get() = "Error Message"
                override val actionLabel: String = "Dismiss"
                override val duration: SnackbarDuration = SnackbarDuration.Short
                override val withDismissAction: Boolean = true
            }

            override fun performAction() {
                TODO("Not yet implemented")
            }

            override fun dismiss() {
                TODO("Not yet implemented")
            }
        }

        BioMedSnackbar(
            snackbarData = snackbarData,
        )
    }
}

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9",
    backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedSnackbarDarkPreview() {
    BioMedTheme {
        val snackbarData = object : SnackbarData {
            override val visuals = object : SnackbarVisuals {
                override val message: String
                    get() = "Error Message"
                override val actionLabel: String? = null
                override val duration: SnackbarDuration = SnackbarDuration.Short
                override val withDismissAction: Boolean = false
            }

            override fun performAction() {
                TODO("Not yet implemented")
            }

            override fun dismiss() {
                TODO("Not yet implemented")
            }
        }

        BioMedSnackbar(
            snackbarData = snackbarData,
        )
    }
}