package com.tripsphere.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tripsphere.domain.model.Expense
import com.tripsphere.domain.model.ExpenseCategory
import com.tripsphere.domain.model.Itinerary
import com.tripsphere.domain.model.Trip
import com.tripsphere.domain.usecase.*
import com.tripsphere.utils.NotificationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class ActiveTripUiState(
    val isLoading: Boolean = true,
    val trip: Trip? = null,
    val expenses: List<Expense> = emptyList(),
    val todayItinerary: List<Itinerary> = emptyList(),
    val totalSpent: Double = 0.0,
    val budgetWarningLevel: Float = 0f,
    val limitReachedEvent: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ActiveTripViewModel @Inject constructor(
    private val getTripByIdUseCase: GetTripByIdUseCase,
    private val getExpensesUseCase: GetExpensesUseCase,
    private val addExpenseUseCase: AddExpenseUseCase,
    private val deleteExpenseUseCase: DeleteExpenseUseCase,
    private val getItineraryUseCase: GetItineraryUseCase,
    private val updateTripUseCase: UpdateTripUseCase,
    private val notificationHelper: NotificationHelper
) : ViewModel() {

    private val _uiState = MutableStateFlow(ActiveTripUiState())
    val uiState: StateFlow<ActiveTripUiState> = _uiState.asStateFlow()

    // Track last alerted threshold to avoid duplicate notifications
    private var lastAlertedThreshold = 0f

    fun loadTrip(tripId: Long) {
        viewModelScope.launch {
            try {
                val trip = getTripByIdUseCase(tripId)
                _uiState.update { it.copy(trip = trip, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }

        viewModelScope.launch {
            getExpensesUseCase(tripId).collect { expenses ->
                val total = expenses.sumOf { it.amount }
                val budget = _uiState.value.trip?.budget ?: 1.0
                val warningLevel = (total / budget).toFloat()
                val prevWarningLevel = _uiState.value.budgetWarningLevel

                // Show in-app limit dialog when first crossing 100%
                val shouldTriggerLimitDialog = warningLevel >= 1f && prevWarningLevel < 1f

                // Send notification at 80%, 90%, 100% thresholds
                checkAndSendBudgetAlert(warningLevel, total, budget)

                _uiState.update {
                    it.copy(
                        expenses = expenses,
                        totalSpent = total,
                        budgetWarningLevel = warningLevel,
                        limitReachedEvent = if (shouldTriggerLimitDialog) true else it.limitReachedEvent
                    )
                }
            }
        }

        viewModelScope.launch {
            getItineraryUseCase(tripId).collect { itineraries ->
                val todayDayNumber = calculateTodayDayNumber()
                val todayItems = itineraries.filter { it.day == todayDayNumber }
                _uiState.update { it.copy(todayItinerary = todayItems) }
            }
        }
    }

    private fun checkAndSendBudgetAlert(warningLevel: Float, spent: Double, budget: Double) {
        val tripName = _uiState.value.trip?.destination ?: return
        val threshold = when {
            warningLevel >= 1f -> 1f
            warningLevel >= 0.9f -> 0.9f
            warningLevel >= 0.8f -> 0.8f
            else -> return
        }
        if (threshold > lastAlertedThreshold) {
            lastAlertedThreshold = threshold
            notificationHelper.sendBudgetAlert(tripName, spent, budget)
        }
    }

    private fun calculateTodayDayNumber(): Int {
        val trip = _uiState.value.trip ?: return 1
        val today = LocalDate.now()
        val daysBetween = java.time.temporal.ChronoUnit.DAYS.between(trip.startDate, today) + 1
        return daysBetween.toInt().coerceAtLeast(1)
    }

    fun addExpense(tripId: Long, title: String, amount: Double, category: ExpenseCategory) {
        if (title.isBlank() || amount <= 0) return
        viewModelScope.launch {
            addExpenseUseCase(
                Expense(
                    tripId = tripId,
                    title = title,
                    amount = amount,
                    category = category,
                    date = LocalDate.now()
                )
            )
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            deleteExpenseUseCase(expense)
        }
    }

    /**
     * Update an existing expense by re-inserting it with the same id.
     * The DAO uses OnConflictStrategy.REPLACE so this acts as an upsert.
     */
    fun updateExpense(original: Expense, title: String, amount: Double, category: ExpenseCategory) {
        if (title.isBlank() || amount <= 0) return
        viewModelScope.launch {
            addExpenseUseCase(original.copy(title = title, amount = amount, category = category))
        }
    }

    fun dismissLimitReached() {
        _uiState.update { it.copy(limitReachedEvent = false) }
    }

    fun updateBudget(newBudget: Double) {
        if (newBudget <= 0) return
        val trip = _uiState.value.trip ?: return
        viewModelScope.launch {
            val updated = trip.copy(budget = newBudget)
            updateTripUseCase(updated)
            // Reset alert threshold so notifications fire again on the new budget
            lastAlertedThreshold = 0f
            _uiState.update { it.copy(trip = updated, limitReachedEvent = false) }
        }
    }
}
