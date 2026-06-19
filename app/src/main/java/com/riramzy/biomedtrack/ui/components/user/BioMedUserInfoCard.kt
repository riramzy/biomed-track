package com.riramzy.biomedtrack.ui.components.user

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.components.custom.BioMedToggle
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.UserRole
import com.riramzy.biomedtrack.utils.getLocalizedDepartmentName

@Composable
fun BioMedUserInfoCard(
    modifier: Modifier = Modifier,
    user: Technician,
    onManageDepartmentsClick: () -> Unit = {},
    isUserActive: Boolean = true,
    onActiveToggle: (Boolean) -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(15.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BioMedUserHeader(
                username = user.name,
                role = user.role.name,
                email = user.email,
                employeeId = user.employeeId,
                withMoreButton = true,
                onMoreClick = onMoreClick
            )

            Spacer(Modifier.height(10.dp))

            if (user.role == UserRole.TECHNICIAN) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    user.assignedDepartments.forEach {
                        BioMedDepartmentPill(
                            departmentName = it.name
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))
            }

            if (user.role == UserRole.SUPERVISOR) {
                Text(
                    text = stringResource(R.string.user_card_access_all_depts),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp),
                    textAlign = TextAlign.Start
                )

                Spacer(Modifier.height(10.dp))
            }

            if (user.role == UserRole.ADMIN) {
                Text(
                    text = stringResource(R.string.user_card_full_system_access),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp),
                    textAlign = TextAlign.Start
                )

                Spacer(Modifier.height(10.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (user.role == UserRole.ADMIN) Arrangement.End else Arrangement.SpaceBetween
            ) {
                if (user.role == UserRole.SUPERVISOR || user.role == UserRole.TECHNICIAN) {
                    BioMedButton(
                        modifier = Modifier
                            .width(235.dp),
                        text = stringResource(R.string.user_card_btn_manage_departments),
                        customTextSize = 12,
                        textAlignment = TextAlign.Start,
                        customTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        customColor = MaterialTheme.colorScheme.primaryContainer,
                        onClick = { onManageDepartmentsClick() }
                    )
                }

                BioMedToggle(
                    text = stringResource(R.string.user_card_toggle_active),
                    isChecked = isUserActive,
                    onCheckedChange = onActiveToggle
                )
            }
        }
    }
}

@Composable
fun BioMedDepartmentPill(
    modifier: Modifier = Modifier,
    departmentName: String = "ICU"
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(25.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Text(
            text = getLocalizedDepartmentName(departmentName),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true, locale = "ar")
@Composable
fun BioMedUserInfoCardPreview() {
    BioMedTheme {
        BioMedUserInfoCard(
            user = Technician(
                id = "1",
                name = "Mina",
                email = "william.henry.harrison@example-pet-store.com",
                role = UserRole.SUPERVISOR,
                assignedDepartments = emptyList(),
                employeeId = "1",
                isActive = true,
            )
        )
    }
}

@Preview(device = "id:pixel_9", showSystemUi = false, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF000000, locale = "ar"
)
@Composable
fun BioMedUserInfoCardDarkPreview() {
    BioMedTheme {
        BioMedUserInfoCard(
            user = Technician(
                id = "1",
                name = "Mina",
                email = "william.henry.harrison@example-pet-store.com",
                role = UserRole.TECHNICIAN,
                assignedDepartments = listOf(
                    Department(
                        id = "1",
                        name = "Dialysis Unit",
                        totalEquipment = 34
                    )
                ),
                employeeId = "1",
                isActive = true
            )
        )
    }
}