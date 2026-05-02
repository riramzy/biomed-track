package com.riramzy.biomedtrack.ui.components.importing

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.riramzy.biomedtrack.ui.theme.indicatorColors

@Composable
fun BioMedGeneratedFileCard(
    modifier: Modifier = Modifier,
    fileName: String = "hospital_equipment.xlsx",
    fileSize: String = "10.6 MB",
    fileFormat: String = "Excel",
    isUploaded: Boolean = false,
    onDownloadClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onPreviewClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp),
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.primaryContainer.copy(0.5f)
            } else {
                MaterialTheme.colorScheme.primaryContainer.copy(0.5f)
            }
        ),
        onClick = { onPreviewClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = if (fileFormat == "Excel") {
                        painterResource(id = R.drawable.excel)
                    } else {
                        painterResource(id = R.drawable.pdf)
                           },
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                )

                Column(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = fileName,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSystemInDarkTheme()) {
                            Color.White
                        } else {
                            Color.Black
                        }
                    )

                    Text(
                        text = fileSize,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isSystemInDarkTheme()) {
                            Color.White
                        } else {
                            Color.Black
                        }
                    )

                    Row {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(16.dp)
                                .background(
                                    color = if (isSystemInDarkTheme()) {
                                        MaterialTheme.indicatorColors.green
                                    } else {
                                        MaterialTheme.indicatorColors.green
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.activity_online),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(11.dp),
                                tint = Color.White
                            )
                        }

                        Text(
                            text = "File ready for preview",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.indicatorColors.green,
                            modifier = Modifier
                                .padding(start = 5.dp)
                        )
                    }

                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .padding(end = 10.dp)
            ) {
                if (isUploaded) {
                    IconButton(
                        onClick = { onDeleteClick() },
                        modifier = Modifier
                            .size(22.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.indicatorColors.red
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.delete),
                            contentDescription = null,
                            modifier = Modifier
                                .size(16.dp),
                            tint = Color.White
                        )
                    }
                }

                if (!isUploaded) {
                    IconButton(
                        onClick = { onShareClick() },
                        modifier = Modifier
                            .size(22.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = if (isSystemInDarkTheme()) {
                                Color.Blue
                            } else {
                                Color.Blue
                            }
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.share),
                            contentDescription = null,
                            modifier = Modifier
                                .size(16.dp),
                            tint = Color.White
                        )
                    }
                }

                if (!isUploaded) {
                    IconButton(
                        onClick = { onDownloadClick() },
                        modifier = Modifier
                            .size(22.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = if (isSystemInDarkTheme()) {
                                Color.Black
                            } else {
                                Color.Black
                            }
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.download),
                            contentDescription = null,
                            modifier = Modifier
                                .size(16.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = false, device = "id:pixel_9")
@Composable
fun BioMedGeneratedFileCardPreview() {
    BioMedTheme {
        BioMedGeneratedFileCard()
    }
}

@Preview(showBackground = true, showSystemUi = false, device = "id:pixel_9",
    backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedGeneratedFileCardDarkPreview() {
    BioMedTheme {
        BioMedGeneratedFileCard()
    }
}