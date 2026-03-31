package com.riramzy.biomedtrack.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun BioMedProfileSelection(
    modifier: Modifier = Modifier,
    selectionText: String = "Logout",
    selectionIcon: Int = R.drawable.logout
) {
    Card(
        modifier = modifier
            .width(328.dp)
            .height(47.dp),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.primaryContainer
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(34.dp)
                        .background(
                            color = if (isSystemInDarkTheme()) {
                                when (selectionText) {
                                    "Notifications Preferences" -> Color(0xFF00B3FF)
                                    "My Profile" -> MaterialTheme.colorScheme.tertiaryContainer
                                    "Change Password" -> Color.Black
                                    "Manage Users" -> Color(0xFF7D7D7D)
                                    "Import Equipment" -> Color.Green
                                    "Logout" -> Color.Red
                                    else -> Color.White
                                }
                            } else {
                                when (selectionText) {
                                    "Notifications Preferences" -> Color(0xFF00B3FF)
                                    "My Profile" -> MaterialTheme.colorScheme.tertiary
                                    "Change Password" -> Color.Black
                                    "Manage Users" -> Color(0xFF7D7D7D)
                                    "Import Equipment" -> Color.Green
                                    "Logout" -> Color.Red
                                    else -> Color.White
                                }
                            }
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(selectionIcon),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }

                Text(
                    text = selectionText,
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSystemInDarkTheme()) {
                        Color.White
                    } else {
                        Color.Black
                    },
                    modifier = Modifier.padding(start = 10.dp)
                )
            }

            IconButton(
                modifier = Modifier
                    .size(24.dp),
                onClick = {}
            ) {
                Icon(
                    painter = painterResource(R.drawable.go),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp),
                    tint = if (isSystemInDarkTheme()) {
                        Color.White
                    } else {
                        Color.Black
                    }
                )
            }
        }
    }
}

@Preview(device = "id:pixel_9", showBackground = true)
@Composable
fun BioMedProfileSelectionPreview() {
    BioMedTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BioMedProfileSelection(selectionText = "Notifications Preferences", selectionIcon = R.drawable.profile_notifications)
            BioMedProfileSelection(selectionText = "My Profile", selectionIcon = R.drawable.profile)
            BioMedProfileSelection(selectionText = "Change Password", selectionIcon = R.drawable.profile_password)
            BioMedProfileSelection(selectionText = "Manage Users", selectionIcon = R.drawable.profile_manage)
            BioMedProfileSelection(selectionText = "Import Equipment", selectionIcon = R.drawable.profile_import)
            BioMedProfileSelection(selectionText = "Logout", selectionIcon = R.drawable.logout)
        }
    }
}

@Preview(device = "id:pixel_9", showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF000000
)
@Composable
fun BioMedProfileSelectionDarkPreview() {
    BioMedTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BioMedProfileSelection(selectionText = "Notifications Preferences", selectionIcon = R.drawable.profile_notifications)
            BioMedProfileSelection(selectionText = "My Profile", selectionIcon = R.drawable.profile)
            BioMedProfileSelection(selectionText = "Change Password", selectionIcon = R.drawable.profile_password)
            BioMedProfileSelection(selectionText = "Manage Users", selectionIcon = R.drawable.profile_manage)
            BioMedProfileSelection(selectionText = "Import Equipment", selectionIcon = R.drawable.profile_import)
            BioMedProfileSelection(selectionText = "Logout", selectionIcon = R.drawable.logout)
        }
    }
}