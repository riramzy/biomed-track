package com.riramzy.biomedtrack.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun BioMedNavBar(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(270.dp)
            .height(54.dp),
        shape = RoundedCornerShape(50.dp),
        colors = cardColors(
            containerColor = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.onSecondary
            } else {
                MaterialTheme.colorScheme.primaryContainer
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BioMedNavItemExpanded(R.drawable.dashboard)
            BioMedNavItem(icon = R.drawable.scheduler)
            BioMedNavItem(icon = R.drawable.inventory)
            BioMedNavItem(icon = R.drawable.reports)
        }
    }
}

@Composable
fun BioMedNavItem(
    icon: Int,
) {
    IconButton(
        onClick = { /*TODO*/ },
        modifier = Modifier
            .size(40.dp),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
    ) {
        Icon(
            painter = painterResource(id = icon),
            modifier = Modifier.size(30.dp),
            tint = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.primary
            } else {
                Color.White
            },
            contentDescription = null,
        )
    }
}

@Composable
fun BioMedNavItemExpanded(
    icon: Int
) {
    Card(
        modifier = Modifier
            .height(40.dp)
            .wrapContentWidth(),
        shape = CircleShape,
        colors = cardColors(
            containerColor = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.primary
            } else {
                Color.White
            }
        ),
    ) {
        Row(
            modifier = Modifier
                .height(40.dp)
                .wrapContentWidth()
                .padding(6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                modifier = Modifier.size(30.dp),
                tint = if (isSystemInDarkTheme()) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.primary
                },
                contentDescription = null,
            )

            Text(
                text = "Dashboard",
                color = if (isSystemInDarkTheme()) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.primary
                },
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}

@Preview(device = "spec:width=411dp,height=891dp", showBackground = true)
@Composable
fun BioMedNavBarPreview() {
    BioMedTheme {
        BioMedNavBar()
    }
}

@Preview(device = "spec:width=411dp,height=891dp", showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
fun BioMedNavBarDarkPreview() {
    BioMedTheme {
        BioMedNavBar()
    }
}