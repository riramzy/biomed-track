package com.riramzy.biomedtrack.ui.components.user

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.UserRole

@Composable
fun BioMedProfileSheet(
    user: Technician,
    onMyProfileClick: () -> Unit = {},
    onNotificationsPreferencesClick: () -> Unit = {},
    onChangePasswordClick: () -> Unit = {},
    onManageUsersClick: () -> Unit = {},
    onImportEquipmentClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
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
            role = user.role.name,
            username = user.name,
            email = user.email,
            employeeId = user.employeeId
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BioMedProfileSelection(
                selectionText = stringResource(R.string.profile_title_my_profile),
                selectionIcon = R.drawable.profile,
                selectionId = "profile",
                onClick = onMyProfileClick
            )

            BioMedProfileSelection(
                selectionText = stringResource(R.string.profile_title_notifications_preferences),
                selectionIcon = R.drawable.profile_notifications,
                selectionId = "notifications",
                onClick = onNotificationsPreferencesClick
            )

            BioMedProfileSelection(
                selectionText = stringResource(R.string.profile_title_change_password),
                selectionIcon = R.drawable.profile_password,
                selectionId = "password",
                onClick = onChangePasswordClick
            )

            if (user.role == UserRole.SUPERVISOR || user.role == UserRole.ADMIN) {
                BioMedProfileSelection(
                    selectionText = stringResource(R.string.profile_title_manage_users),
                    selectionIcon = R.drawable.profile_manage,
                    selectionId = "manage",
                    onClick = onManageUsersClick
                )

            }

            if (user.role == UserRole.ADMIN) {
                BioMedProfileSelection(
                    selectionText = stringResource(R.string.profile_title_import_equipment),
                    selectionIcon = R.drawable.profile_import,
                    selectionId = "import",
                    onClick = onImportEquipmentClick
                )
            }
        }

        BioMedProfileSelection(
            selectionText = stringResource(R.string.profile_title_logout),
            selectionIcon = R.drawable.logout,
            selectionId = "logout",
            onClick = onLogoutClick
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_9", locale = "ar")
@Composable
fun BioMedProfileSheetPreview() {
    BioMedTheme {
        BioMedProfileSheet(
            user = Technician(
                id = "1",
                name = "Khaled",
                email = "william.henry.harrison@example-pet-store.com",
                role = UserRole.ADMIN,
                assignedDepartments = emptyList(),
                employeeId = "1",
                isActive = true,
            )
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_9",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF000000, locale = "ar"
)
@Composable
fun BioMedProfileSheetDarkPreview() {
    BioMedTheme {
        BioMedProfileSheet(
            user = Technician(
                id = "1",
                name = "Khaled",
                email = "william.henry.harrison@example-pet-store.com",
                role = UserRole.ADMIN,
                assignedDepartments = emptyList(),
                employeeId = "1",
                isActive = true,
            )
        )
    }
}