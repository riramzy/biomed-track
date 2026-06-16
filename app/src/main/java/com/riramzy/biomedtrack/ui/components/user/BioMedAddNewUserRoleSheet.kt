package com.riramzy.biomedtrack.ui.components.user

import android.content.res.Configuration
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var employeeId by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedRole: UserRole? by remember { mutableStateOf(null) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.add_user_title),
            style = MaterialTheme.typography.titleLarge,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSystemInDarkTheme()) {
                Color.White
            } else {
                Color.Black
            },
            modifier = Modifier.fillMaxWidth()
        )

        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BioMedTextField(
                    label = stringResource(R.string.add_user_field_first_name),
                    placeholder = stringResource(R.string.field_name_placeholder),
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
                    label = stringResource(R.string.add_user_field_last_name),
                    placeholder = stringResource(R.string.field_name_placeholder),
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
                    label = stringResource(R.string.add_user_field_employee_id),
                    placeholder = stringResource(R.string.add_user_validation_employee_id),
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
                    label = stringResource(R.string.add_user_field_email),
                    placeholder = stringResource(R.string.login_email_placeholder),
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
                    label = stringResource(R.string.add_user_field_password),
                    placeholder = stringResource(R.string.login_password_placeholder),
                    isNoteCard = false,
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
                    title = stringResource(R.string.add_user_field_role),
                    selectedItem = selectedRole?.name
                        ?: stringResource(R.string.add_user_field_role_placeholder),
                    onItemSelected = { selectedRole = UserRole.valueOf(it) },
                    placeholder = stringResource(R.string.add_user_field_role_placeholder),
                    items = UserRole.entries.map { it.name },
                    modifier = Modifier.padding(
                        start = 20.dp,
                        end = 20.dp,
                        bottom = 15.dp
                    )
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BioMedButton(
                text = stringResource(R.string.confirm),
                customColor = MaterialTheme.colorScheme.primary,
                customTextColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.weight(1f),
                onClick = {
                    when {
                        firstName.isBlank() -> {
                            Toast.makeText(
                                context,
                                R.string.add_user_validation_first_name,
                                Toast.LENGTH_SHORT
                            ).show()
                            return@BioMedButton
                        }
                        lastName.isBlank() -> {
                            Toast.makeText(
                                context,
                                R.string.add_user_validation_last_name,
                                Toast.LENGTH_SHORT
                            ).show()
                            return@BioMedButton
                        }
                        employeeId.isBlank() -> {
                            Toast.makeText(
                                context,
                                R.string.add_user_validation_employee_id,
                                Toast.LENGTH_SHORT
                            ).show()
                            return@BioMedButton
                        }
                        email.isBlank() -> {
                            Toast.makeText(
                                context,
                                R.string.add_user_validation_email,
                                Toast.LENGTH_SHORT
                            ).show()
                            return@BioMedButton
                        }
                        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                            Toast.makeText(
                                context,
                                R.string.add_user_validation_email_format,
                                Toast.LENGTH_SHORT
                            ).show()
                            return@BioMedButton
                        }
                        password.isBlank() -> {
                            Toast.makeText(
                                context,
                                R.string.add_user_validation_password,
                                Toast.LENGTH_SHORT
                            ).show()
                            return@BioMedButton
                        }
                        password.length < 6 -> {
                            Toast.makeText(
                                context, R.string.add_user_validation_password_length, Toast.
                            LENGTH_SHORT).show()
                            return@BioMedButton
                        }
                    }

                    val newUser = Technician(
                        id = "",
                        name = "$firstName $lastName",
                        email = email,
                        role = selectedRole ?: UserRole.TECHNICIAN,
                        assignedDepartments = emptyList(),
                        employeeId = employeeId,
                        isActive = true
                    )

                    onConfirm(newUser, password)
                }
            )

            BioMedButton(
                text = stringResource(R.string.btn_cancel),
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
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9", locale = "ar")
@Composable
fun BioMedAddNewUserSheetPreview() {
    BioMedTheme {
        BioMedAddNewUserSheet()
    }
}

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9",
    backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL, locale = "ar"
)
@Composable
fun BioMedAddNewUserSheetDarkPreview() {
    BioMedTheme {
        BioMedAddNewUserSheet()
    }
}