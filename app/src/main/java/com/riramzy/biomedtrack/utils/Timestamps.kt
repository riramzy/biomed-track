package com.riramzy.biomedtrack.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

object Timestamps {
    fun Long.toRelativeTime(): String {
        val now = System.currentTimeMillis()
        val diff = now - this

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            minutes < 1 -> "Now"
            minutes < 60 -> "${minutes}m ago"
            hours < 24 -> "${hours}h ago"
            days < 7 -> "${days}d ago"
            else -> this.toDateString()
        }
    }

    fun Long.toDateString(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(this))
    }

    fun Long.getGroupHeader(): String {
        return try {
            val date = Instant.ofEpochMilli(this)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            val today = LocalDate.now()

            when (date) {
                today -> "Today"
                today.minusDays(1) -> "Yesterday"
                else -> date.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
            }
        } catch(_: Exception) {
            "Earlier"
        }
    }
}