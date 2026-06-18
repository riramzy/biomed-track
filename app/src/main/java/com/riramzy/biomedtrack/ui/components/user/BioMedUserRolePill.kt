package com.riramzy.biomedtrack.ui.components.user

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import com.riramzy.biomedtrack.ui.theme.roleColors

@Composable
fun BioMedUserRolePill(
    role: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(120.dp)
            .wrapContentHeight(),
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (role) {
                "ADMIN" -> {
                    MaterialTheme.roleColors.admin
                }

                "SUPERVISOR" -> {
                    MaterialTheme.roleColors.supervisor
                }

                else -> {
                    if (isSystemInDarkTheme()) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.secondary
                    }
                }
            }
        )
    ) {
        val localizedRole = when (role.uppercase()) {
            "ADMIN" -> stringResource(R.string.role_admin)
            "SUPERVISOR" -> stringResource(R.string.role_supervisor)
            "TECHNICIAN" -> stringResource(R.string.role_technician)
            else -> role
        }

        Text(
            text = localizedRole,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 10.dp,
                    end = 10.dp,
                    top = 2.dp,
                    bottom = 2.dp
                ),
            color = Color.White
        )
    }
}

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9", locale = "ar")
@Composable
fun BioMedUserRolePillPreview() {
    BioMedTheme {
        BioMedUserRolePill(
            role = "Admin"
        )
    }
}

@Preview(showSystemUi = false, showBackground = true, device = "id:pixel_9",
    backgroundColor = 0xFF000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedUserRolePillDarkPreview() {
    BioMedTheme {
        BioMedUserRolePill(
            role = "Supervisor"
        )
    }
}