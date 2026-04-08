package com.riramzy.biomedtrack.ui.components.custom

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
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults.cardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun BioMedNavBar(
    modifier: Modifier = Modifier,
    withActionButton: Boolean = true,
    isActionButtonText: Boolean = true,
    actionButtonText: String = "Add",
    actionButtonIcon: Int = R.drawable.add,
    selectedPage: String = "Scheduler",
    onActionButtonClick: () -> Unit = {},
    onDashboardClick: () -> Unit = {},
    onSchedulerClick: () -> Unit = {},
    onInventoryClick: () -> Unit = {},
    onReportsClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
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
            when (selectedPage) {
                "Dashboard" -> {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BioMedNavItemExpanded(itemName = selectedPage, icon = R.drawable.dashboard)
                        BioMedNavItem(icon = R.drawable.scheduler, onNavItemClick = { onSchedulerClick() })
                        BioMedNavItem(icon = R.drawable.inventory, onNavItemClick = { onInventoryClick() })
                        BioMedNavItem(icon = R.drawable.reports, onNavItemClick = { onReportsClick() })
                    }
                }
                "Scheduler" -> {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BioMedNavItem(R.drawable.dashboard, onNavItemClick = { onDashboardClick() })
                        BioMedNavItemExpanded(itemName = selectedPage, icon = R.drawable.scheduler)
                        BioMedNavItem(icon = R.drawable.inventory, onNavItemClick = { onInventoryClick() })
                        BioMedNavItem(icon = R.drawable.reports, onNavItemClick = { onReportsClick() })
                    }
                }
                "Inventory" -> {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BioMedNavItem(R.drawable.dashboard, onNavItemClick = { onDashboardClick() })
                        BioMedNavItem(icon = R.drawable.scheduler, onNavItemClick = { onSchedulerClick() })
                        BioMedNavItemExpanded(itemName = selectedPage, icon = R.drawable.inventory)
                        BioMedNavItem(icon = R.drawable.reports, onNavItemClick = { onReportsClick() })
                    }
                }
                "Reports" -> {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BioMedNavItem(R.drawable.dashboard, onNavItemClick = { onDashboardClick() })
                        BioMedNavItem(icon = R.drawable.scheduler, onNavItemClick = { onSchedulerClick() })
                        BioMedNavItem(icon = R.drawable.inventory, onNavItemClick = { onInventoryClick() })
                        BioMedNavItemExpanded(itemName = selectedPage, icon = R.drawable.reports)
                    }
                }
                else -> {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BioMedNavItem(R.drawable.dashboard, onNavItemClick = { onDashboardClick() })
                        BioMedNavItem(icon = R.drawable.scheduler, onNavItemClick = { onSchedulerClick() })
                        BioMedNavItem(icon = R.drawable.inventory, onNavItemClick = { onInventoryClick() })
                        BioMedNavItem(icon = R.drawable.reports, onNavItemClick = { onReportsClick() })
                    }
                }
            }
        }

        if (withActionButton) {
            if (isActionButtonText) {
                TextButton (
                    onClick = {},
                    modifier = Modifier
                        .height(54.dp)
                        .padding(start = 8.dp),
                    colors = buttonColors(
                        containerColor = if (isSystemInDarkTheme()) {
                            MaterialTheme.colorScheme.secondaryContainer
                        } else {
                            MaterialTheme.colorScheme.primaryContainer
                        }
                    )
                ) {
                    Text(
                        text = actionButtonText,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp),
                        textAlign = TextAlign.Center,
                        color = if (isSystemInDarkTheme()) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    )
                }
            } else {
                IconButton(
                    onClick = { onActionButtonClick() },
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .size(54.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = if (isSystemInDarkTheme()) {
                            MaterialTheme.colorScheme.secondaryContainer
                        } else {
                            MaterialTheme.colorScheme.primaryContainer
                        }
                    )
                ) {
                    Icon(
                        painter = painterResource(id = actionButtonIcon),
                        modifier = Modifier.size(34.dp),
                        tint = if (isSystemInDarkTheme()) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.primary
                        },
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Composable
fun BioMedNavItem(
    icon: Int,
    onNavItemClick: () -> Unit = {}
) {
    IconButton(
        onClick = { onNavItemClick() },
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
            modifier = Modifier
                .size(30.dp),
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
    itemName: String = "Dashboard",
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
                text = itemName,
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