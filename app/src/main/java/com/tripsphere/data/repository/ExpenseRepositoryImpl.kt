package com.tripsphere.data.repository

import com.tripsphere.data.local.dao.ExpenseDao
import com.tripsphere.data.local.entity.ExpenseEntity
import com.tripsphere.domain.model.Expense
import com.tripsphere.domain.model.ExpenseCategory
import com.tripsphere.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class ExpenseRepositoryImpl @Inject constructor(
    private val dao: ExpenseDao
) : ExpenseRepository {

    override fun getExpensesForTrip(tripId: Long): Flow<List<Expense>> =
        dao.getExpensesForTrip(tripId).map { it.map(::entityToDomain) }

    override suspend fun insertExpense(expense: Expense) =
        dao.insertExpense(domainToEntity(expense))

    override suspend fun deleteExpense(expense: Expense) =
        dao.deleteExpense(domainToEntity(expense))

    override suspend fun getTotalSpentForTrip(tripId: Long): Double =
        dao.getTotalSpentForTrip(tripId)

    private fun entityToDomain(entity: ExpenseEntity) = Expense(
        id = entity.id,
        tripId = entity.tripId,
        title = entity.title,
        amount = entity.amount,
        category = ExpenseCategory.valueOf(entity.category),
        date = LocalDate.parse(entity.date)
    )

    private fun domainToEntity(expense: Expense) = ExpenseEntity(
        id = expense.id,
        tripId = expense.tripId,
        title = expense.title,
        amount = expense.amount,
        category = expense.category.name,
        date = expense.date.toString()
    )
}
