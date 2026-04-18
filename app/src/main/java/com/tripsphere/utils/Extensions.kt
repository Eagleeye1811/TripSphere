package com.tripsphere.utils

import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun LocalDate.daysUntil(): Long = ChronoUnit.DAYS.between(LocalDate.now(), this)

fun LocalDate.isOngoing(endDate: LocalDate): Boolean {
    val today = LocalDate.now()
    return !today.isBefore(this) && !today.isAfter(endDate)
}

fun Double.formatCurrency(): String = "$${"%.2f".format(this)}"

fun Double.toPercentage(total: Double): Float {
    if (total <= 0) return 0f
    return (this / total * 100).toFloat().coerceIn(0f, 100f)
}
