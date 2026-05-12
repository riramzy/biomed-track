package com.riramzy.biomedtrack.ui.components.custom

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun BioMedRadioButton(
    modifier: Modifier = Modifier,
    icon: Int,
    iconColor: Color,
    onClick: () -> Unit = {},
    isSelected: Boolean = false
) {
    if (isSelected) {
        Box(
            modifier = modifier
                .clip(CircleShape)
                .size(24.dp)
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
    } else {
        Box(
            modifier = modifier
                .clip(CircleShape)
                .size(24.dp)
                .border(
                    border = BorderStroke(
                        width = 2.dp,
                        color = iconColor,
                    ),
                    shape = CircleShape
                )
                .background(Color.Transparent)
                .clickable { onClick() },
            contentAlignment = Alignment.Center,
        ) {}
    }
}

@Preview
@Composable
fun BioMedRadioButtonPreview() {
    BioMedTheme {
        BioMedRadioButton(
            modifier = Modifier,
            icon = com.riramzy.biomedtrack.R.drawable.activity_online,
            iconColor = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF000000
)
@Composable
fun BioMedRadioButtonDarkPreview() {
    BioMedTheme {
        BioMedRadioButton(
            modifier = Modifier,
            icon = com.riramzy.biomedtrack.R.drawable.activity_online,
            iconColor = MaterialTheme.colorScheme.primary
        )
    }
}