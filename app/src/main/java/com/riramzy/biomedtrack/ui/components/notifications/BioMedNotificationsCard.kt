package com.riramzy.biomedtrack.ui.components.notifications

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.components.custom.BioMedStatusIndicator
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.ui.theme.indicatorColors

@Composable
fun BioMedNotificationsCard(
    modifier: Modifier = Modifier,
    status: String = "Assigned",
    isRead: Boolean = false
) {
    Card(
        modifier = modifier
            .width(386.dp)
            .height(124.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isRead) {
                if (isSystemInDarkTheme()) {
                    MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
                } else {
                    MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
                }
            } else {
                if (isSystemInDarkTheme()) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.primaryContainer
                }
            }
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 15.dp,
                        end = 15.dp,
                        top = 10.dp,
                        bottom = 10.dp
                    ),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .size(34.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = when(status) {
                            "Online" -> MaterialTheme.indicatorColors.green
                            "Down" -> MaterialTheme.indicatorColors.red
                            "Service" -> MaterialTheme.indicatorColors.yellow
                            "Log" -> if (isSystemInDarkTheme()) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.primary
                            }
                            else -> if (isSystemInDarkTheme()) {
                                MaterialTheme.colorScheme.tertiaryContainer
                            } else {
                                MaterialTheme.colorScheme.tertiary
                            }
                        }
                    )
                ) {
                    Image(
                        painter = when(status) {
                            "Online" -> painterResource(id = R.drawable.activity_online)
                            "Down" -> painterResource(R.drawable.activity_down)
                            "Service" -> painterResource(R.drawable.activity_service)
                            "Assigned" -> painterResource(R.drawable.profile)
                            else -> painterResource(R.drawable.activity_log)
                        },
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(2f)
                        .padding(start = 15.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Status Changed to Online",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSystemInDarkTheme()) {
                            Color.White
                        } else {
                            Color.Black
                        }
                    )

                    Text(
                        text = "Fresenius 4008S - 4545885N70",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Normal,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSystemInDarkTheme()) {
                            Color.White
                        } else {
                            Color.Black
                        }
                    )

                    BioMedStatusIndicator(
                        modifier = Modifier.padding(top = 5.dp),
                        status = status,
                        changeable = false
                    )

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.padding(top = 5.dp)
                    ) {
                        Text(
                            text = "Dialysis Unit",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSystemInDarkTheme()) {
                                Color.White
                            } else {
                                Color.Black
                            }
                        )

                        Text(
                            text = "By Ramsey Ibrahim",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSystemInDarkTheme()) {
                                Color.White
                            } else {
                                Color.Black
                            }
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Top
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "12m ago",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Normal,
                            style = MaterialTheme.typography.labelSmall,
                            textAlign = TextAlign.End,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        if (!isRead) {
                            Box(
                                modifier = Modifier
                                    .padding(start = 5.dp)
                                    .clip(CircleShape)
                                    .size(10.dp)
                                    .background(
                                        if (isSystemInDarkTheme()) {
                                            MaterialTheme.colorScheme.primary
                                        } else {
                                            MaterialTheme.colorScheme.primary
                                        }
                                    )
                            )
                        }
                    }

                    Text(
                        text = "13/3/2026",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = false, device = "id:pixel_9")
@Composable
fun BioMedNotificationsCardPreview() {
    BioMedTheme {
        BioMedNotificationsCard()
    }
}

@Preview(showBackground = true, showSystemUi = false, device = "id:pixel_9",
    backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedNotificationsCardDarkPreview() {
    BioMedTheme {
        BioMedNotificationsCard()
    }
}