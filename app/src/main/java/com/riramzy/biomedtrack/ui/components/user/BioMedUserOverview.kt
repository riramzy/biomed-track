package com.riramzy.biomedtrack.ui.components.user

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun BioMedUserCard(
    modifier: Modifier = Modifier,
    username: String,
    role: String
) {
    Card(
        modifier = modifier
            .width(356.dp)
            .height(62.dp),
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.onSecondary
            } else {
                MaterialTheme.colorScheme.primaryContainer
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Welcome, $username",
                fontSize = 19.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSystemInDarkTheme()) {
                    Color.White
                } else {
                    Color.Black
                }
            )

            BioMedUserRolePill(
                role = role
            )
        }
    }
}

@Preview(device = "spec:width=411dp,height=891dp", showSystemUi = false, showBackground = true)
@Composable
fun BioMedUserCardPreview() {
    BioMedTheme {
        BioMedUserCard(
            username = "Khaled",
            role = "Admin"
        )
    }
}

@Preview(device = "spec:width=411dp,height=891dp", showSystemUi = false, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedUserCardDarkPreview() {
    BioMedTheme {
        BioMedUserCard(
            username = "Khaled",
            role = "Admin"
        )
    }
}