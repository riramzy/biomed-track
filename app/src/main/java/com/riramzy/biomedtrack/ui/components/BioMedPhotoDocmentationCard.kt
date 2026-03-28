package com.riramzy.biomedtrack.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun BioMedPhotoDocumentationCard(
    modifier: Modifier = Modifier,
    title: String = "Photo Documentation",
    description: String = "Take photos of installation reports or documentations"
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 20.dp,
                bottom = 15.dp
            )
    ) {
        Text(
            text = title,
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
        ) {
            Card(
                modifier = Modifier
                    .width(328.dp)
                    .height(165.dp),
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
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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

                    BioMedButton(
                        text = "Add Photo",
                        modifier = Modifier
                            .padding(bottom = 8.dp)
                    )

                    Text(
                        text = description,
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

@Preview(device = "id:pixel_9", showBackground = true)
@Composable
fun BioMedPhotoDocumentationCardPreview() {
    BioMedTheme {
        BioMedPhotoDocumentationCard()
    }
}

@Preview(device = "id:pixel_9", showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedPhotoDocumentationCardDarkPreview() {
    BioMedTheme {
        BioMedPhotoDocumentationCard()
    }
}