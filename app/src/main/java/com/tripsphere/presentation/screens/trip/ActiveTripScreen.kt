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

    // ── Budget warning banner ─────────────────────────────────────────────────
    val warningLevel = uiState.budgetWarningLevel
    val snackMessage = when {
        warningLevel >= 1f -> "⚠️ Budget exceeded!"
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
            Surface(modifier = Modifier.fillMaxWidth(), color = Warning.copy(alpha = 0.15f)) {
                Text(
                    snackMessage,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = Warning,
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
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Total Budget", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(
                                    "$${"%.0f".format(trip.budget)}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Remaining", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                val remaining = trip.budget - uiState.totalSpent
                                Text(
                                    "$${"%.0f".format(remaining.coerceAtLeast(0.0))}",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = if (remaining < 0) Error else Success
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
                            label = { Text("All") },
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
                            label = { Text("${cat.emoji} ${cat.label}") },
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
                        fontWeight = FontWeight.Bold
                    )
                }
                items(uiState.todayItinerary) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = TripBlue.copy(alpha = 0.06f))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Event, null, tint = TripBlue, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Column {
                                Text(item.activity, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                                Text(item.time, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
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
                    Text("Expenses", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        "${uiState.expenses.size} record${if (uiState.expenses.size != 1) "s" else ""}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
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
                            color = TextHint
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
                        Icon(Icons.Default.Info, null, tint = TextHint, modifier = Modifier.size(13.dp))
                        Spacer(Modifier.width(5.dp))
                        Text(
                            "Swipe left to delete · Long-press to edit",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextHint
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
                    .padding(horizontal = 20.dp, vertical = 12.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TripAccent)
            ) {
                Icon(Icons.Default.Add, null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Add Expense", fontWeight = FontWeight.Bold)
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
        ExpenseCategory.STAY to Color(0xFF6A1B9A),
        ExpenseCategory.ACTIVITIES to Color(0xFF2E7D32),
        ExpenseCategory.SHOPPING to Color(0xFFC62828),
        ExpenseCategory.OTHER to Color(0xFF37474F)
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
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = color.copy(alpha = 0.12f),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(expense.category.emoji, fontSize = 20.sp)
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(expense.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    "${expense.category.label} · ${expense.date}",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    "-$${"%.2f".format(expense.amount)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                // Edit hint icon
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Long-press to edit",
                    tint = TextHint,
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
                        unfocusedBorderColor = TextHint.copy(alpha = 0.4f)
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
                        unfocusedBorderColor = TextHint.copy(alpha = 0.4f)
                    )
                )
                Text("Category", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
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
                                            color = if (selected) Color.White else TextSecondary
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
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextSecondary) }
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
                color = TextSecondary
            )
            Text("Spent", style = MaterialTheme.typography.labelSmall, color = TextHint)
        }
    }
}
