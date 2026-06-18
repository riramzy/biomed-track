package com.riramzy.biomedtrack.utils

import android.content.Context
import com.riramzy.biomedtrack.R
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

sealed class NotificationHeader {
    data class ResourceHeader(val stringResId: Int) : NotificationHeader()
    data class StringHeader(val text: String) : NotificationHeader()
}

object Timestamps {
    fun Long.toRelativeTime(context: Context): String {
        val now = System.currentTimeMillis()
        val diff = now - this

        if (diff < 0) {
            val absoluteDiff = kotlin.math.abs(diff)
            val days = absoluteDiff / (1000 * 60 * 60 * 24)
            return if (days > 0) {
                context.resources.getQuantityString(
                    R.plurals.time_days_future,
                    days.toInt(),
                    days.toInt()
                )
            } else {
                context.getString(R.string.status_scheduled)
            }
        }

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            minutes < 1 -> context.getString(R.string.time_now)
            minutes < 60 -> context.resources.getQuantityString(
                R.plurals.time_mins_ago,
                minutes.toInt(),
                minutes.toInt()
            )

            hours < 24 -> context.resources.getQuantityString(
                R.plurals.time_hours_ago,
                hours.toInt(),
                hours.toInt()
            )

            days < 7 -> context.resources.getQuantityString(
                R.plurals.time_days_ago,
                days.toInt(),
                days.toInt()
            )
            else -> this.toDateString()
        }
    }

    fun Long.toDateString(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date(this))
    }

    fun Long.getGroupHeader(): NotificationHeader {
        return try {
            val date = Instant.ofEpochMilli(this)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            val today = LocalDate.now()

            when (date) {
                today -> NotificationHeader.ResourceHeader(R.string.header_today)
                today.minusDays(1) -> NotificationHeader.ResourceHeader(R.string.header_yesterday)
                else -> NotificationHeader.StringHeader(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            }
        } catch(_: Exception) {
            NotificationHeader.ResourceHeader(R.string.header_earlier)
        }
    }

    fun parseDateToLong(dateString: String, nullable: Boolean = false): Long {
        if (dateString.isBlank() || dateString.equals("none", ignoreCase = true)) {
            return if (nullable) 0L else System.currentTimeMillis()
        }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        return try {
            dateFormat.parse(dateString)?.time ?: System.currentTimeMillis()
        } catch (e: Exception) {
            e.printStackTrace()
            if (nullable) 0L else System.currentTimeMillis()
        }
    }

    fun getStartOfWeek(offsetWeeks: Int = 0): Long {
        val now = LocalDate.now().plusWeeks(offsetWeeks.toLong())
        val monday = now.with(java.time.DayOfWeek.MONDAY)
        return monday.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun getEndOfWeek(offsetWeeks: Int = 0): Long {
        val now = LocalDate.now().plusWeeks(offsetWeeks.toLong())
        val sunday = now.with(java.time.DayOfWeek.SUNDAY)
        return sunday.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    fun Long.toDayName(): String {
        val date = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
        return date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    }

    fun Long.toDayMonthString(): String {
        val date = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
        return date.format(DateTimeFormatter.ofPattern("dd MMM", Locale.getDefault()))
    }

    fun Long.isOverdue(): Boolean {
        val dueDate = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
        val today = LocalDate.now()
        return dueDate.isBefore(today)
    }
}