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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun BioMedUsersInsightCard(
    modifier: Modifier = Modifier,
    numberOfUsers: Int = 12,
    usersRole: String = "Technicians"
) {
    Card(
        modifier = modifier
            .width(86.dp)
            .height(78.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSystemInDarkTheme()) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.primary
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = numberOfUsers.toString(),
                style = MaterialTheme.typography.titleMedium,
                color = if (isSystemInDarkTheme()) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.primaryContainer
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Black
            )

            Text(
                text = usersRole,
                style = MaterialTheme.typography.titleMedium,
                color = if (isSystemInDarkTheme()) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.primaryContainer
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Preview(device = "id:pixel_9", showBackground = true)
@Composable
fun BioMedUsersInsightCardPreview() {
    BioMedTheme {
        BioMedUsersInsightCard()
    }
}

@Preview(device = "id:pixel_9", showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF000000
)
@Composable
fun BioMedUsersInsightCardDarkPreview() {
    BioMedTheme {
        BioMedUsersInsightCard()
    }
}