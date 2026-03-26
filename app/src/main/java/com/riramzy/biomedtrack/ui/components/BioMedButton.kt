package com.riramzy.biomedtrack.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun BioMedButton(
    withIcon: Boolean = false,
    icon: Int? = R.drawable.filter,
    modifier: Modifier = Modifier
) {
    var isSelected: Boolean = false

    if (withIcon) {
        IconButton(
            onClick = { /*TODO*/ },
            modifier = modifier
                .width(100.dp)
                .height(35.dp)
                .clip(shape = RoundedCornerShape(25.dp)),
            colors = IconButtonDefaults.iconButtonColors(
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(shape = CircleShape)
                        .background(
                            color = if (isSystemInDarkTheme()) {
                                if (isSelected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.secondaryContainer
                                }
                            } else {
                                if (isSelected) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    Color.White
                                }
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = icon!!),
                        contentDescription = null,
                        modifier = Modifier
                            .size(18.dp),
                        tint = if (isSystemInDarkTheme()) {
                            if (isSelected) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            }
                        } else {
                            if (isSelected) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onPrimaryContainer
                            }
                        }
                    )
                }

                Text(
                    text = "Filter",
                    style = MaterialTheme.typography.labelLarge,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSystemInDarkTheme()) {
                        if (isSelected) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        }
                    } else {
                        if (isSelected) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        }
                    }
                )
            }
        }
    } else {
        Button(
            onClick = { /*TODO*/ },
            modifier = modifier
                .width(100.dp)
                .height(35.dp),
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
                text = "Button",
                style = MaterialTheme.typography.labelLarge,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSystemInDarkTheme()) {
                    if (isSelected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    }
                } else {
                    if (isSelected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    }
                }
            )
        }
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true)
@Composable
fun BioMedButtonPreview() {
    BioMedTheme {
        BioMedButton()
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedButtonDarkPreview() {
    BioMedTheme {
        BioMedButton()
    }
}