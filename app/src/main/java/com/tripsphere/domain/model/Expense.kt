package com.tripsphere.domain.model

import java.time.LocalDate

data class Expense(
    val id: Long = 0,
    val tripId: Long,
    val title: String,
    val amount: Double,
    val category: ExpenseCategory,
    val date: LocalDate = LocalDate.now()
)

enum class ExpenseCategory(val label: String, val emoji: String) {
    FOOD("Food", "🍽️"),
    TRANSPORT("Transport", "🚗"),
    STAY("Stay", "🏨"),
    ACTIVITIES("Activities", "🎭"),
    SHOPPING("Shopping", "🛍️"),
    OTHER("Other", "💼")
}
