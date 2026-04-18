package com.tripsphere.data.local.dao

import androidx.room.*
import com.tripsphere.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses WHERE tripId = :tripId ORDER BY date DESC")
    fun getExpensesForTrip(tripId: Long): Flow<List<ExpenseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity)

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM expenses WHERE tripId = :tripId")
    suspend fun getTotalSpentForTrip(tripId: Long): Double
}
