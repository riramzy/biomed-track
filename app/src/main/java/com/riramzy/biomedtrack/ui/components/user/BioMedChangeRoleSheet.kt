package com.riramzy.biomedtrack.ui.components.user

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.UserRole

@Composable
fun BioMedChangeRoleSheet(
    modifier: Modifier = Modifier,
    user: Technician,
    onSave: (UserRole) -> Unit = { _ -> },
    onCancel: () -> Unit = {}
) {
    var selectedRole by remember { mutableStateOf(user.role) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BioMedUserHeader(
            username = user.name,
            role = user.role.name,
            email = user.email,
            employeeId = user.employeeId,
            withMoreButton = false
        )

        Text(
            text = stringResource(id = R.string.change_role_title),
            style = MaterialTheme.typography.titleLarge,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSystemInDarkTheme()) {
                Color.White
            } else {
                Color.Black
            },
            modifier = Modifier
                .fillMaxWidth()
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BioMedUserRoleSelectionCard(
                role = stringResource(R.string.change_role_supervisor),
                description = stringResource(R.string.change_role_supervisor_desc),
                isSelected = selectedRole == UserRole.SUPERVISOR,
                onRoleClick = { selectedRole = UserRole.SUPERVISOR }
            )

            BioMedUserRoleSelectionCard(
                role = stringResource(R.string.change_role_admin),
                description = stringResource(R.string.change_role_admin_desc),
                isSelected = selectedRole == UserRole.ADMIN,
                onRoleClick = { selectedRole = UserRole.ADMIN }
            )

            BioMedUserRoleSelectionCard(
                role = stringResource(R.string.change_role_technician),
                description = stringResource(R.string.change_role_technician_desc),
                isSelected = selectedRole == UserRole.TECHNICIAN,
                onRoleClick = { selectedRole = UserRole.TECHNICIAN }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BioMedButton(
                text = "Save",
                customColor = if (isSystemInDarkTheme()) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.primary
                },
                customTextColor = if (isSystemInDarkTheme()) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onPrimary
                },
                modifier = Modifier.weight(1f),
                onClick = { onSave(selectedRole) }
            )

            BioMedButton(
                text = "Cancel",
                customColor = MaterialTheme.colorScheme.primaryContainer,
                customTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9", locale = "ar")
@Composable
fun BioMedChangeRoleSheetPreview() {
    BioMedTheme {
        BioMedChangeRoleSheet(
            user = Technician(
                id = "1",
                name = "Khaled",
                email = "james.moore.wayne@example-pet-store.com",
                role = UserRole.ADMIN,
                assignedDepartments = emptyList(),
                employeeId = "1",
                isActive = true,
            )
        )
    }
}

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9",
    backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL, locale = "ar"
)
@Composable
fun BioMedChangeRoleSheetDarkPreview() {
    BioMedTheme {
        BioMedChangeRoleSheet(
            user = Technician(
                id = "1",
                name = "Khaled",
                email = "john.quincy.adams@examplepetstore.com",
                role = UserRole.ADMIN,
                assignedDepartments = emptyList(),
                employeeId = "1",
                isActive = true
            )
        )
    }
}