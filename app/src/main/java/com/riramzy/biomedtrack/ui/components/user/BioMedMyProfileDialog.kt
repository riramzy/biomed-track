package com.riramzy.biomedtrack.ui.components.user

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.domain.model.Technician
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.utils.UserRole

@Composable
fun BioMedMyProfileDialog(
    user: Technician,
    onDismiss: () -> Unit = {}
) {
    val isDark = isSystemInDarkTheme()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            shape = RoundedCornerShape(25.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isDark) MaterialTheme.colorScheme.onSecondary else Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(15.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Image(
                        painter = painterResource(R.drawable.profile),
                        contentDescription = null,
                        modifier = Modifier
                            .size(34.dp)
                    )

                    Text(
                        text = "My Profile",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White else Color.Black
                    )
                }


                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White else Color.Black
                    )
                    BioMedUserRolePill(
                        role = user.role.name,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProfileMetaRow(label = "Employee ID", value = user.employeeId, isDark = isDark)
                    ProfileMetaRow(label = "Email Address", value = user.email, isDark = isDark)
                    ProfileMetaRow(
                        label = "Departments Access",
                        value = if (user.assignedDepartments.isEmpty()) "None" else user.
                        assignedDepartments.joinToString { it.name },
                        isDark = isDark
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                BioMedButton(
                    text = "Close",
                    onClick = onDismiss,
                    customColor = MaterialTheme.colorScheme.primaryContainer.copy(0.5f),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun ProfileMetaRow(label: String, value: String, isDark: Boolean) {
    Column {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = if (isDark) Color.White.copy(0.9f) else Color.Black.copy(0.8f)
        )
    }
}

@Preview(device = "id:pixel_9", showBackground = true, showSystemUi = true)
@Composable
fun BioMedMyProfileDialogPreview() {
    BioMedTheme {
        BioMedMyProfileDialog(
            user = Technician(
                id = "1",
                name = "Ramzy Ibrahim",
                email = "john.mclean@examplepetstore.com",
                role = UserRole.ADMIN,
                assignedDepartments = emptyList(),
                employeeId = "1",
                isActive = true
            )
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    device = "id:pixel_9", showSystemUi = true, showBackground = true
)
@Composable
fun BioMedMyProfileDialogDarkPreview() {
    BioMedTheme {
        BioMedMyProfileDialog(
            user = Technician(
                id = "1",
                name = "Ramzy Ibrahim",
                email = "john.mclean@examplepetstore.com",
                role = UserRole.ADMIN,
                assignedDepartments = emptyList(),
                employeeId = "1",
                isActive = true
            )
        )
    }
}