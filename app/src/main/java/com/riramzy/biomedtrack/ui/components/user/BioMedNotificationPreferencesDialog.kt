package com.riramzy.biomedtrack.ui.components.user

import android.content.Context
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.firebase.messaging.FirebaseMessaging
import com.riramzy.biomedtrack.R
import com.riramzy.biomedtrack.ui.components.custom.BioMedButton
import com.riramzy.biomedtrack.ui.theme.BioMedTheme

@Composable
fun BioMedNotificationPreferencesDialog(
    onDismiss: () -> Unit = {}
) {
    val isDark = isSystemInDarkTheme()
    val context = LocalContext.current
    val sharedPrefs = remember { context.getSharedPreferences("biomed_prefs", Context.MODE_PRIVATE) }

    var criticalAlerts by remember { mutableStateOf(sharedPrefs.getBoolean("critical_alerts_enabled", true)) }
    var taskReminders by remember { mutableStateOf(sharedPrefs.getBoolean("task_reminders_enabled", true)) }

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
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Image(
                        painter = painterResource(R.drawable.profile_notifications),
                        contentDescription = null,
                        modifier = Modifier
                            .size(34.dp)
                    )

                    Text(
                        text = "Notification Preferences",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White else Color.Black
                    )
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                            Text(
                                text = "Critical Alerts",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color.White else Color.Black
                            )

                            Text(
                                text = "Receive status changes & maintenance completions instantly.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 15.sp
                            )
                        }

                        Switch(
                            checked = criticalAlerts,
                            onCheckedChange = { checked ->
                                criticalAlerts = checked
                                sharedPrefs.edit().putBoolean("critical_alerts_enabled", checked).apply()

                                if (checked) {
                                    FirebaseMessaging.getInstance().
                                    subscribeToTopic("admin_alerts")
                                    Toast.makeText(context, "Subscribed to Critical Alerts",
                                        Toast.LENGTH_SHORT).show()
                                } else {
                                    FirebaseMessaging.getInstance().
                                    unsubscribeFromTopic("admin_alerts")
                                    Toast.makeText(context, "Unsubscribed from Critical Alerts", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = SwitchDefaults.colors(
                                uncheckedBorderColor = MaterialTheme.colorScheme.primary,
                                uncheckedThumbColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                            Text(
                                text = "Task Reminders",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color.White else Color.Black
                            )

                            Text(
                                text = "Keep reminders for your daily maintenance scheduling.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 15.sp
                            )
                        }
                        Switch(
                            checked = taskReminders,
                            onCheckedChange = { checked ->
                                taskReminders = checked
                                sharedPrefs.edit().putBoolean("task_reminders_enabled", checked).apply()
                            },
                            colors = SwitchDefaults.colors(
                                uncheckedBorderColor = MaterialTheme.colorScheme.primary,
                                uncheckedThumbColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                BioMedButton(
                    text = "Save",
                    onClick = onDismiss,
                    customColor = MaterialTheme.colorScheme.primary,
                    customTextColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(device = "id:pixel_9", showSystemUi = true, showBackground = true)
@Composable
fun BioMedNotificationPreferencesDialogPreview() {
    BioMedTheme {
        BioMedNotificationPreferencesDialog()
    }
}

@Preview(device = "id:pixel_9", showSystemUi = true, showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun BioMedNotificationPreferencesDialogDarkPreview() {
    BioMedTheme {
        BioMedNotificationPreferencesDialog()
    }
}