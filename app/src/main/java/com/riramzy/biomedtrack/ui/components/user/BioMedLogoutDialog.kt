package com.riramzy.biomedtrack.ui.components.user

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
fun BioMedLogoutDialog(
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {}
) {
    val isDark = isSystemInDarkTheme()

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = if (isDark) MaterialTheme.colorScheme.onSecondary else Color.White,
        icon = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logout),
                    contentDescription = "Logout Alert",
                    modifier = Modifier.size(36.dp)
                )
            }
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Log Out",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    color = if (isDark) Color.White else Color.Black
                )
            }
        },
        text = {
            Text(
                text = "Are you sure you want to log out of BioMedTrack? You will need to enter your hospital credentials again to sign in.",
                style = MaterialTheme.typography.bodyMedium,
                fontSize = 14.sp,
                color = if (isDark) Color.White.copy(0.7f) else Color.Black.copy(0.7f),
                textAlign = TextAlign.Start
            )
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = if (isDark) Color.White.copy(0.8f) else Color.Black.copy(0.8f)
                ),
                modifier = Modifier.padding(end = 4.dp)
            ) {
                Text(text = "Cancel", fontWeight = FontWeight.Bold)
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text(text = "Log Out", fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true, device = "id:pixel_9")
@Composable
fun BioMedLogoutDialogPreview() {
    BioMedTheme {
        BioMedLogoutDialog()
    }
}

@Preview(showBackground = true, showSystemUi = true, device = "id:pixel_9",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedLogoutDialogDarkPreview() {
    BioMedTheme {
        BioMedLogoutDialog()
    }
}