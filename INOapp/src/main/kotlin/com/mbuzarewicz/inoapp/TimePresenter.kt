package com.mbuzarewicz.inoapp

import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class TimePresenter {

    companion object {
        fun formatToDailyHour(time: Long?): String {
            if (time == null) return "---"
            if (time == 0L) return "---"

            val zoneId = ZoneId.of("Europe/Warsaw")
            val dateTime = Instant.ofEpochMilli(time).atZone(zoneId).toLocalDateTime()
            return dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        }

        fun formatToAbsoluteHoursMinutesSeconds(time: Long?): String {
            if (time == null) return "---"
            if (time == 0L) return "---"

            val seconds = time / 1000
            val duration = Duration.ofSeconds(seconds)
            val hours = duration.toHours()
            val minutes = duration.toMinutes() % 60
            val secs = duration.seconds % 60

            return "%02d:%02d:%02d".format(hours, minutes, secs)
        }
    }
}