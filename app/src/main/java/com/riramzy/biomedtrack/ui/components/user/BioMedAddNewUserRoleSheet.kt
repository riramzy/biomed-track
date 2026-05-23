package com.riramzy.biomedtrack.ui.components.user

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.components.custom.BioMedSelector
import com.riramzy.biomedtrack.ui.components.custom.BioMedTextField
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.UserRole

@Composable
fun BioMedAddNewUserSheet(
    modifier: Modifier = Modifier,
    onConfirm: (Technician, String) -> Unit = { _, _ -> },
    onCancel: () -> Unit = {}
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var employeeId by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf("Technician") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add New User",
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

        LazyColumn (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.height(420.dp)
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BioMedTextField(
                        label = "First Name",
                        placeholder = "First Name",
                        value = firstName,
                        onValueChange = { firstName = it },
                        isNoteCard = false,
                        modifier = Modifier.padding(
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 15.dp
                        )
                    )

                    BioMedTextField(
                        label = "Last Name",
                        placeholder = "Last Name",
                        value = lastName,
                        onValueChange = { lastName = it },
                        modifier = Modifier.padding(
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 15.dp
                        ),
                        isNoteCard = false
                    )

                    BioMedTextField(
                        label = "Employee ID",
                        placeholder = "Employee ID",
                        value = employeeId,
                        onValueChange = { employeeId = it },
                        modifier = Modifier.padding(
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 15.dp
                        ),
                        isNoteCard = false
                    )

                    BioMedTextField(
                        label = "Email",
                        placeholder = "Email",
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.padding(
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 15.dp
                        ),
                        isNoteCard = false
                    )

                    BioMedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        placeholder = "Enter your password",
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(
                                onClick = { passwordVisible = !passwordVisible },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    painter = painterResource(
                                        if (passwordVisible) {
                                            R.drawable.hide
                                        } else {
                                            R.drawable.view
                                        }
                                    ),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                        modifier = Modifier.padding(
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 15.dp
                        )
                    )

                    BioMedSelector(
                        title = "Role",
                        selectedItem = selectedRole,
                        onItemSelected = { selectedRole = it },
                        placeholder = "Select Role",
                        items = listOf("Supervisor", "Admin", "Technician"),
                        modifier = Modifier.padding(
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 15.dp
                        )
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            BioMedButton(
                text = "Confirm",
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
                onClick = {
                    if (firstName.isNotEmpty() &&
                        lastName.isNotEmpty() &&
                        employeeId.isNotEmpty() &&
                        email.isNotEmpty() &&
                        password.isNotEmpty() &&
                        selectedRole.isNotEmpty()
                    ) {
                        val roleEnum = when (selectedRole) {
                            "Supervisor" -> UserRole.SUPERVISOR
                            "Admin" -> UserRole.ADMIN
                            else -> UserRole.TECHNICIAN
                        }

                        val newUser = Technician(
                            id = "",
                            name = "$firstName $lastName",
                            email = email,
                            role = roleEnum,
                            assignedDepartments = emptyList(),
                            employeeId = employeeId,
                            isActive = true
                        )

                        onConfirm(newUser, password)
                    }
                }
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

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9")
@Composable
fun BioMedAddNewUserSheetPreview() {
    BioMedTheme {
        BioMedAddNewUserSheet()
    }
}

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9",
    backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedAddNewUserSheetDarkPreview() {
    BioMedTheme {
        BioMedAddNewUserSheet()
    }
}