package com.tripsphere.presentation.screens.trip

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tripsphere.domain.model.Expense
import com.tripsphere.domain.model.ExpenseCategory
import com.tripsphere.presentation.components.LoadingScreen
import com.tripsphere.presentation.ui.theme.*
import com.tripsphere.presentation.viewmodel.ActiveTripViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveTripScreen(
    tripId: Long,
    onNavigateBack: () -> Unit,
    viewModel: ActiveTripViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddExpense by remember { mutableStateOf(false) }
    var expenseToEdit by remember { mutableStateOf<Expense?>(null) }
    var selectedCategoryFilter by remember { mutableStateOf<ExpenseCategory?>(null) }
    var showEditBudget by remember { mutableStateOf(false) }

    LaunchedEffect(tripId) { viewModel.loadTrip(tripId) }

    if (uiState.isLoading) { LoadingScreen(); return }

    val trip = uiState.trip ?: return

    // ── Dialogs ──────────────────────────────────────────────────────────────

    if (showAddExpense) {
        ExpenseFormDialog(
            title = "Add Expense",
            confirmLabel = "Add",
            onDismiss = { showAddExpense = false },
            onConfirm = { title, amount, category ->
                viewModel.addExpense(tripId, title, amount, category)
                showAddExpense = false
            }
        )
    }

    expenseToEdit?.let { editing ->
        ExpenseFormDialog(
            title = "Edit Expense",
            confirmLabel = "Save",
            initialTitle = editing.title,
            initialAmount = "%.2f".format(editing.amount),
            initialCategory = editing.category,
            onDismiss = { expenseToEdit = null },
            onConfirm = { title, amount, category ->
                viewModel.updateExpense(editing, title, amount, category)
                expenseToEdit = null
            }
        )
    }

    if (showEditBudget) {
        EditBudgetDialog(
            currentBudget = trip.budget,
            onDismiss = { showEditBudget = false },
            onConfirm = { newBudget ->
                viewModel.updateBudget(newBudget)
                showEditBudget = false
            }
        )
    }

    // ── Limit reached overlay ─────────────────────────────────────────────────
    if (uiState.limitReachedEvent) {
        BudgetLimitReachedDialog(
            spent = uiState.totalSpent,
            budget = trip.budget,
            onDismiss = { viewModel.dismissLimitReached() },
            onIncreaseBudget = {
                viewModel.dismissLimitReached()
                showEditBudget = true
            }
        )
    }

    // ── Budget warning banner ─────────────────────────────────────────────────
    val warningLevel = uiState.budgetWarningLevel
    val snackMessage = when {
        warningLevel >= 1f -> "⚠️ Budget limit reached!"
        warningLevel >= 0.9f -> "⚠️ 90% of budget used!"
        warningLevel >= 0.8f -> "⚠️ 80% of budget used!"
        else -> null
    }

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        // ── Header ────────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(TripBlueDark, TripBlue)))
                .padding(top = 48.dp, start = 8.dp, end = 20.dp, bottom = 16.dp)
        ) {
            IconButton(onClick = onNavigateBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(Icons.Default.ArrowBack, null, tint = Color.White)
            }
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Manage Expenses",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    "${trip.destination} · ${trip.startDate.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${trip.startDate.year}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        // ── Warning banner ────────────────────────────────────────────────────
        if (snackMessage != null) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = if (warningLevel >= 1f) Error.copy(alpha = 0.18f) else Warning.copy(alpha = 0.15f)
            ) {
                Text(
                    snackMessage,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (warningLevel >= 1f) Error else Warning,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // ── Content ───────────────────────────────────────────────────────────
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Budget ring
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        BudgetRing(
                            spent = uiState.totalSpent,
                            total = trip.budget,
                            modifier = Modifier.size(160.dp)
                        )
                        Spacer(Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "Total Budget",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        "$${"%.0f".format(trip.budget)}",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    IconButton(
                                        onClick = { showEditBudget = true },
                                        modifier = Modifier.size(22.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Edit,
                                            contentDescription = "Edit budget limit",
                                            tint = TripBlue,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "Remaining",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                val remaining = trip.budget - uiState.totalSpent
                                Text(
                                    "$${"%.0f".format(remaining.coerceAtLeast(0.0))}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = if (remaining <= 0) Error else Success
                                )
                            }
                        }
                    }
                }
            }

            // Category filter chips
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedCategoryFilter == null,
                            onClick = { selectedCategoryFilter = null },
                            label = { Text("All", color = if (selectedCategoryFilter == null) Color.White else MaterialTheme.colorScheme.onSurface) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TripBlue,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                    items(ExpenseCategory.values()) { cat ->
                        FilterChip(
                            selected = selectedCategoryFilter == cat,
                            onClick = {
                                selectedCategoryFilter = if (selectedCategoryFilter == cat) null else cat
                            },
                            label = {
                                Text(
                                    "${cat.emoji} ${cat.label}",
                                    color = if (selectedCategoryFilter == cat) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TripBlue,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            // Today's itinerary
            if (uiState.todayItinerary.isNotEmpty()) {
                item {
                    Text(
                        "Today's Itinerary",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                items(uiState.todayItinerary) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = TripBlue.copy(alpha = 0.12f),
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Event, null, tint = TripBlue, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text(
                                    item.activity,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    item.time,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Expenses header
            item {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Expenses",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        "${uiState.expenses.size} record${if (uiState.expenses.size != 1) "s" else ""}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Expense list — swipe left to delete, long-press to edit
            val filteredExpenses = if (selectedCategoryFilter != null)
                uiState.expenses.filter { it.category == selectedCategoryFilter }
            else uiState.expenses

            if (filteredExpenses.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No expenses yet. Tap + to add!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                items(filteredExpenses, key = { it.id }) { expense ->
                    SwipeableExpenseItem(
                        expense = expense,
                        onDelete = { viewModel.deleteExpense(expense) },
                        onEdit = { expenseToEdit = expense }
                    )
                }
            }

            // CRUD hint
            item {
                if (filteredExpenses.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Info,
                            null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            "Swipe left to delete · Long-press to edit",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }

        // ── Add Expense FAB row ───────────────────────────────────────────────
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.background
        ) {
            Button(
                onClick = { showAddExpense = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TripAccent)
            ) {
                Icon(Icons.Default.Add, null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Add Expense", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Swipeable expense item (swipe-left = delete, long-press = edit)
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeableExpenseItem(
    expense: Expense,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) { onDelete(); true } else false
        },
        positionalThreshold = { it * 0.4f }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val bgColor by animateColorAsState(
                targetValue = when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.EndToStart -> Color(0xFFD32F2F)
                    else -> Color.Transparent
                },
                animationSpec = tween(200),
                label = "expense_swipe_bg"
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bgColor, RoundedCornerShape(12.dp))
                    .padding(end = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Delete, null, tint = Color.White, modifier = Modifier.size(22.dp))
                        Text("Delete", color = Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true
    ) {
        ExpenseItemCard(expense = expense, onEdit = onEdit)
    }
}

@Composable
private fun ExpenseItemCard(expense: Expense, onEdit: () -> Unit) {
    val categoryColors = mapOf(
        ExpenseCategory.FOOD to Color(0xFFFF6F00),
        ExpenseCategory.TRANSPORT to Color(0xFF1565C0),
        ExpenseCategory.STAY to Color(0xFF7C4DFF),
        ExpenseCategory.ACTIVITIES to Color(0xFF00BFA5),
        ExpenseCategory.SHOPPING to Color(0xFFC62828),
        ExpenseCategory.OTHER to Color(0xFF78909C)
    )
    val color = categoryColors[expense.category] ?: TripBlue

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(expense.id) {
                detectTapGestures(onLongPress = { onEdit() })
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = color.copy(alpha = 0.18f),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(expense.category.emoji, fontSize = 20.sp)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    expense.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "${expense.category.label} · ${expense.date}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "-$${"%.2f".format(expense.amount)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Long-press to edit",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.size(13.dp).padding(top = 2.dp)
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Unified Add / Edit dialog
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpenseFormDialog(
    title: String,
    confirmLabel: String,
    initialTitle: String = "",
    initialAmount: String = "",
    initialCategory: ExpenseCategory = ExpenseCategory.FOOD,
    onDismiss: () -> Unit,
    onConfirm: (String, Double, ExpenseCategory) -> Unit
) {
    var expenseTitle by remember { mutableStateOf(initialTitle) }
    var amount by remember { mutableStateOf(initialAmount) }
    var selectedCategory by remember { mutableStateOf(initialCategory) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = expenseTitle,
                    onValueChange = { expenseTitle = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TripBlue,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount ($)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal
                    ),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TripBlue,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
                Text("Category", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                // Category grid — 3 columns
                val categories = ExpenseCategory.values()
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    categories.toList().chunked(3).forEach { row ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            row.forEach { cat ->
                                val selected = selectedCategory == cat
                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (selected) TripBlue else MaterialTheme.colorScheme.surfaceVariant,
                                    onClick = { selectedCategory = cat }
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(cat.emoji, fontSize = 18.sp)
                                        Text(
                                            cat.label,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                            // Fill remaining cells if row is incomplete
                            repeat(3 - row.size) { Spacer(Modifier.weight(1f)) }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amt = amount.toDoubleOrNull() ?: 0.0
                    if (expenseTitle.isNotBlank() && amt > 0) onConfirm(expenseTitle, amt, selectedCategory)
                },
                colors = ButtonDefaults.buttonColors(containerColor = TripAccent),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(confirmLabel, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Edit budget limit dialog
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditBudgetDialog(
    currentBudget: Double,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var input by remember { mutableStateOf(if (currentBudget > 0) "%.0f".format(currentBudget) else "") }
    val isValid = input.toDoubleOrNull()?.let { it > 0 } == true

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Edit, null, tint = TripBlue, modifier = Modifier.size(22.dp))
                Text("Set Budget Limit", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Enter your total budget for this trip. Expenses will be tracked from \$0 up to this limit.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = input,
                    onValueChange = { input = it },
                    label = { Text("Budget ($)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal
                    ),
                    singleLine = true,
                    isError = input.isNotEmpty() && !isValid,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TripBlue,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
                if (input.isNotEmpty() && !isValid) {
                    Text(
                        "Please enter a valid amount greater than 0",
                        style = MaterialTheme.typography.labelSmall,
                        color = Error
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { input.toDoubleOrNull()?.let { onConfirm(it) } },
                enabled = isValid,
                colors = ButtonDefaults.buttonColors(containerColor = TripBlue),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Set Limit", fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Budget limit reached dialog
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun BudgetLimitReachedDialog(
    spent: Double,
    budget: Double,
    onDismiss: () -> Unit,
    onIncreaseBudget: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Error.copy(alpha = 0.15f), RoundedCornerShape(32.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🚨", fontSize = 30.sp)
                }
                Spacer(Modifier.height(12.dp))
                Text(
                    "Budget Limit Reached!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Error,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "You've spent $${"%.0f".format(spent)} — your full budget of $${"%.0f".format(budget)} has been used up.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Warning.copy(alpha = 0.12f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "💡 Consider reviewing your expenses or increasing the budget limit.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Warning,
                        modifier = Modifier.padding(12.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onIncreaseBudget,
                colors = ButtonDefaults.buttonColors(containerColor = TripBlue),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Edit, null, tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Increase Budget", fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                Text("Got it, I'll review expenses", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Budget ring
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun BudgetRing(spent: Double, total: Double, modifier: Modifier = Modifier) {
    val percent = if (total > 0) (spent / total).toFloat().coerceIn(0f, 1f) else 0f
    val animatedPercent by animateFloatAsState(
        targetValue = percent,
        animationSpec = tween(1000, easing = EaseOutCubic),
        label = "ring"
    )
    val ringColor = when {
        percent >= 1f -> Error
        percent >= 0.9f -> Warning
        else -> TripBlue
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = 20.dp.toPx()
            val padding = stroke / 2
            drawArc(
                color = Color(0xFF1E2538),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(padding, padding),
                size = Size(size.width - stroke, size.height - stroke),
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
            drawArc(
                color = ringColor,
                startAngle = -90f,
                sweepAngle = 360f * animatedPercent,
                useCenter = false,
                topLeft = Offset(padding, padding),
                size = Size(size.width - stroke, size.height - stroke),
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "$${"%.0f".format(spent)}",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "${(percent * 100).toInt()}% of budget",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "Spent",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}
