package com.riramzy.biomedtrack.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.ui.theme.indicatorColors

@Composable
fun BioMedPhotoDocumentationCard(
    modifier: Modifier = Modifier,
    capturedPhotoUri: String? = null,
    addPhotoClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.photo_doc_title),
            style = MaterialTheme.typography.labelLarge,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.onSecondaryContainer
            } else {
                MaterialTheme.colorScheme.onPrimaryContainer
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor =  if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.primaryContainer.copy(0.4f)
                    }
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.primaryContainer
                    }
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (capturedPhotoUri != null) {
                        AsyncImage(
                            model = capturedPhotoUri,
                            contentDescription = "Captured Photo",
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .size(40.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.camera),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .size(40.dp),
                            contentScale = ContentScale.Fit,
                            colorFilter = ColorFilter.tint(
                                MaterialTheme.colorScheme.primary
                            )
                        )
                    }

                    BioMedButton(
                        text = if (capturedPhotoUri != null) stringResource(R.string.photo_doc_change_photo) else stringResource(
                            R.string.photo_doc_add_photo
                        ),
                        modifier = Modifier
                            .width(120.dp)
                            .padding(bottom = 8.dp),
                        onClick = { addPhotoClick() }
                    )

                    if (capturedPhotoUri != null) {
                        Box(
                            modifier
                                .background(
                                    color = MaterialTheme.indicatorColors.green,
                                    shape = CircleShape
                                )
                        ) {
                            Text(
                                text = stringResource(R.string.photo_doc_photo_captured_successfully),
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 15.dp, vertical = 2.dp)
                            )
                        }
                    } else {
                        Text(
                            text = stringResource(R.string.photo_doc_description),
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.secondary,
                            textAlign = TextAlign.Center

                        )
                    }
                }
            }
        }
    }
}

@Preview(device = "id:pixel_9", showBackground = true)
@Composable
fun BioMedPhotoDocumentationCardPreview() {
    BioMedTheme {
        BioMedPhotoDocumentationCard()
    }
}

@Preview(device = "id:pixel_9", showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF000000, locale = "ar"
)
@Composable
fun BioMedPhotoDocumentationCardDarkPreview() {
    BioMedTheme {
        BioMedPhotoDocumentationCard()
    }
}