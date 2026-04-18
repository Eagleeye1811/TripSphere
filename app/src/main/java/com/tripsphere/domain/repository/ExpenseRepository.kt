package com.tripsphere.domain.repository

import com.tripsphere.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun getExpensesForTrip(tripId: Long): Flow<List<Expense>>
    suspend fun insertExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
    suspend fun getTotalSpentForTrip(tripId: Long): Double
}
