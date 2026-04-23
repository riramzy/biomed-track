package com.riramzy.biomedtrack.ui.components.user

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun BioMedProfileSheet(
    role: String = "Supervisor"
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BioMedUserHeader(
            withMoreButton = false,
            role = role
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BioMedProfileSelection(selectionText = "My Profile", selectionIcon = R.drawable.profile)
            BioMedProfileSelection(
                selectionText = "Notifications Preferences",
                selectionIcon = R.drawable.profile_notifications
            )
            BioMedProfileSelection(
                selectionText = "Change Password",
                selectionIcon = R.drawable.profile_password
            )

            if (role == "Supervisor" || role == "Admin") {
                BioMedProfileSelection(
                    selectionText = "Manage Users",
                    selectionIcon = R.drawable.profile_manage
                )

            }

            if (role == "Admin") {
                BioMedProfileSelection(
                    selectionText = "Import Equipment",
                    selectionIcon = R.drawable.profile_import
                )
            }
        }

        BioMedProfileSelection(selectionText = "Logout", selectionIcon = R.drawable.logout)
    }
}

@Preview(showBackground = true, device = "id:pixel_9")
@Composable
fun BioMedProfileSheetPreview() {
    BioMedTheme {
        BioMedProfileSheet()
    }
}

@Preview(showBackground = true, device = "id:pixel_9",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF000000
)
@Composable
fun BioMedProfileSheetDarkPreview() {
    BioMedTheme {
        BioMedProfileSheet()
    }
}