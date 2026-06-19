package com.riramzy.biomedtrack.ui.components.custom

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun BioMedButton(
    modifier: Modifier = Modifier,
    text: String = "Button",
    textAlignment: TextAlign = TextAlign.Center,
    withIcon: Boolean = false,
    icon: Int? = R.drawable.filter,
    customColor: Color? = null,
    customTextColor: Color? = null,
    customTextSize: Int? = null,
    onClick: () -> Unit = {},
    isEnabled: Boolean = true
) {
    var isSelected by remember { mutableStateOf(false) }

    if (withIcon) {
        IconButton(
            onClick = { onClick() },
            modifier = modifier
                .widthIn(min = 100.dp)
                .heightIn(min = 50.dp)
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
            ),
            enabled = isEnabled
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
                                    MaterialTheme.colorScheme.onSecondary
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
                    text = text,
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
            onClick = { onClick() },
            modifier = modifier
                .widthIn(min = 100.dp)
                .heightIn(min = 50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = customColor
                    ?: if (isSystemInDarkTheme()) {
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
            ),
            enabled = isEnabled
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontSize = customTextSize?.sp ?: 10.sp,
                fontWeight = FontWeight.Bold,
                color = customTextColor
                    ?: if (isSystemInDarkTheme()) {
                        if (isSelected) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        }
                    } else {
                        if (isSelected) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onSecondaryContainer
                        }
                    },
                textAlign = textAlignment
            )
        }
    }
}

@Composable
fun BioMedEditButton(
    modifier: Modifier = Modifier,
    icon: Int? = R.drawable.edit,
    onClick: () -> Unit
) {
    IconButton(
        onClick = { onClick() },
        modifier = modifier
            .width(100.dp)
            .height(35.dp)
            .clip(shape = RoundedCornerShape(25.dp)),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(id = icon!!),
                contentDescription = null,
                modifier = Modifier
                    .size(22.dp),
                tint = MaterialTheme.colorScheme.onSecondary
            )

            Text(
                text = stringResource(R.string.edit),
                style = MaterialTheme.typography.labelLarge,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@Composable
fun BioMedDeleteButton(
    modifier: Modifier = Modifier,
    icon: Int? = R.drawable.delete,
    onClick: () -> Unit
) {
    IconButton(
        onClick = { onClick() },
        modifier = modifier
            .width(100.dp)
            .height(35.dp)
            .clip(shape = RoundedCornerShape(25.dp)),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.error
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                painter = painterResource(id = icon!!),
                contentDescription = null,
                modifier = Modifier
                    .size(22.dp),
                tint = MaterialTheme.colorScheme.onError
            )

            Text(
                text = stringResource(R.string.delete_dialog_title),
                style = MaterialTheme.typography.labelLarge,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onError
            )
        }
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true)
@Composable
fun BioMedButtonPreviewBioMed() {
    BioMedTheme {
        Column {
            BioMedEditButton(
                icon = R.drawable.edit,
                onClick = { }
            )
        }
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF000000
)
@Composable
fun BioMedButtonDarkPreviewBioMed() {
    BioMedTheme {
        Column {
            BioMedEditButton(
                icon = R.drawable.edit,
                onClick = { }
            )
        }
    }
}