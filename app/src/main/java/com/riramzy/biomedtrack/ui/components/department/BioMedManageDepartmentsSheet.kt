package com.riramzy.biomedtrack.ui.components.department

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.domain.model.Department
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.components.custom.BioMedToggle
import com.riramzy.biomedtrack.ui.components.user.BioMedUserHeader
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.UserRole

@Composable
fun BioMedManageDepartmentsSheet(
    user: Technician,
    departments: List<Department> = emptyList(),
    onSave: (List<Department>) -> Unit = {},
    onCancel: () -> Unit = {}
) {
    var selectedDepartments by remember { mutableStateOf(user.assignedDepartments.toSet()) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BioMedUserHeader(
            withMoreButton = false,
            username = user.name,
            role = user.role.name,
            email = user.email,
            employeeId = user.employeeId
        )

        Text(
            text = "Manage Departments",
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

        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            for(dept in departments) {
                BioMedToggle(
                    text = dept.name,
                    modifier = Modifier
                        .fillMaxWidth(),
                    isChecked = selectedDepartments.contains(dept),
                    onCheckedChange = { checked ->
                        selectedDepartments = if (checked) {
                            selectedDepartments + dept
                        } else {
                            selectedDepartments - dept
                        }
                    }
                )

                Spacer(Modifier.height(10.dp))
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
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
                modifier = Modifier.padding(end = 10.dp),
                onClick = { onSave(selectedDepartments.toList()) }
            )

            BioMedButton(
                text = "Cancel",
                customColor = if (isSystemInDarkTheme()) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    MaterialTheme.colorScheme.primaryContainer
                },
                customTextColor = if (isSystemInDarkTheme()) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                    MaterialTheme.colorScheme.onPrimaryContainer
                },
                onClick = onCancel
            )
        }
    }
}

@Preview(showBackground = true, device = "id:pixel_9")
@Composable
fun BioMedManageDepartmentsSheetPreview() {
    BioMedTheme {
        BioMedManageDepartmentsSheet(
            user = Technician(
                id = "1",
                name = "Khaled",
                email = "john.quincy.adams@examplepetstore.com",
                role = UserRole.ADMIN,
                assignedDepartments = listOf(
                    Department(
                        id = "1",
                        name = "Department 1",
                        totalEquipment = 15,
                    ),
                    Department(
                        id = "2",
                        name = "Department 2",
                        totalEquipment = 10,
                    ),
                    Department(
                        id = "3",
                        name = "Department 3",
                        totalEquipment = 5,
                    )
                ),
                employeeId = "1",
                isActive = true
            )
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_9",
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    backgroundColor = 0xFF000000
)
@Composable
fun BioMedManageDepartmentsSheetDarkPreview() {
    BioMedTheme {
        BioMedManageDepartmentsSheet(
            user = Technician(
                id = "1",
                name = "Khaled",
                email = "john.quincy.adams@examplepetstore.com",
                role = UserRole.ADMIN,
                assignedDepartments = listOf(
                    Department(
                        id = "1",
                        name = "Department 1",
                        totalEquipment = 15,
                    ),
                    Department(
                        id = "2",
                        name = "Department 2",
                        totalEquipment = 10,
                    ),
                    Department(
                        id = "3",
                        name = "Department 3",
                        totalEquipment = 5,
                    )
                ),
                employeeId = "1",
                isActive = true
            )
        )
    }
}