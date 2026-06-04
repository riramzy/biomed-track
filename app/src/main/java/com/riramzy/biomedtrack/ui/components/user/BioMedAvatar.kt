package com.riramzy.biomedtrack.ui.components.user

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riramzy.biomedtrack.ui.theme.BioMedTheme
import kotlin.math.abs

@Composable
fun BioMedAvatar(
    name: String = "John Doe",
    size: Dp = 25.dp,
    textSize: TextUnit = (size.value * 0.4f).sp,
    textColor: Color = Color.White,
    backgroundColor: Color? = null
) {
    val avatarInitials = remember(name) { getInitials(name) }

    val avatarBackgroundColors = backgroundColor ?: remember(name) {
        val biomedColors = listOf(
            Color(0xFF1E88E5), // Blue
            Color(0xFF43A047), // Green
            Color(0xFFE53935), // Red
            Color(0xFF8E24AA), // Purple
            Color(0xFFF4511E), // Orange
            Color(0xFF00ACC1), // Cyan
            Color(0xFF3949AB), // Indigo
            Color(0xFFD81B60)  // Pink
        )

        if (name.isBlank()) {
            Color.Gray
        } else {
            val index = abs(name.hashCode()) % biomedColors.size
            biomedColors[index]
        }
    }

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(avatarBackgroundColors),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = avatarInitials,
            color = textColor,
            fontSize = textSize,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            softWrap = false,
            textAlign = TextAlign.Center
        )
    }
}

private fun getInitials(name: String): String {
    val trimmed = name.trim()
    if (trimmed.isEmpty()) return "?"

    val parts = trimmed.split(Regex("\\s+"))
    return if (parts.size >= 2) {
        val firstChar = parts[0].firstOrNull()?.uppercaseChar() ?: ""
        val secondChar = parts[1].firstOrNull()?.uppercaseChar() ?: ""
        "$firstChar$secondChar"
    } else {
        trimmed.take(2).uppercase()
    }
}

@Preview
@Composable
fun BioMedAvatarPreview() {
    BioMedTheme {
        BioMedAvatar()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun BioMedAvatarDarkPreview() {
    BioMedTheme {
        BioMedAvatar()
    }
}