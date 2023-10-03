package com.anhquan.straviewer.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object UnitFormatter {
    fun distance(value: Double): String {
        return if (value < 1000) {
            "$value m"
        } else {
            String.format("%.2f", value / 1000) + " km"
        }
    }

    fun duration(seconds: Int): String {
        val h = seconds / 3600
        val m = (seconds - h * 3600) / 60
        val s = (seconds - h * 3600) - m * 60
        return (if (h == 0) "" else "${h}h ") + (if (m == 0) "" else "${m}m ") + "${s}s"
    }

    fun time(time: Date): String {
        val formatter = SimpleDateFormat("hh:mm MM/dd/yyyy", Locale.US)
        return formatter.format(time).toString()
    }
}