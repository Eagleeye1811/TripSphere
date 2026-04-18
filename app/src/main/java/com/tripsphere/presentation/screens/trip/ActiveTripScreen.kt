package com.tripsphere.presentation.screens.trip

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
    var selectedCategoryFilter by remember { mutableStateOf<ExpenseCategory?>(null) }

    LaunchedEffect(tripId) { viewModel.loadTrip(tripId) }

    if (uiState.isLoading) { LoadingScreen(); return }

    val trip = uiState.trip ?: return

    if (showAddExpense) {
        AddExpenseDialog(
            onDismiss = { showAddExpense = false },
            onAdd = { title, amount, category ->
                viewModel.addExpense(tripId, title, amount, category)
                showAddExpense = false
            }
        )
    }

    // Budget warning snackbar
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
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(TripBlueDark, TripBlue)))
                .padding(top = 48.dp, start = 8.dp, end = 20.dp, bottom = 16.dp)
        ) {
            IconButton(onClick = onNavigateBack, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(Icons.Default.ArrowBack, null, tint = Color.White)
            }
            Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Trip Expenses", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                Text("${trip.destination} · ${trip.startDate.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${trip.startDate.year}", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
            }
            IconButton(onClick = { }, modifier = Modifier.align(Alignment.CenterEnd)) {
                Icon(Icons.Default.MoreVert, null, tint = Color.White)
            }
        }

        // Warning banner
        if (snackMessage != null) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Warning.copy(alpha = 0.15f)
            ) {
                Text(
                    snackMessage,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = Warning,
                    fontWeight = FontWeight.Bold
                )
            }
        }

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
                                Text("Total Budget", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                Text("$${"%.0f".format(trip.budget)}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = TextPrimary)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Remaining", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
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
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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
                            onClick = { selectedCategoryFilter = if (selectedCategoryFilter == cat) null else cat },
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
                    Text("Today's Itinerary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
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

            // Recent expenses header
            item {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Recent Expenses", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    TextButton(onClick = { }) { Text("View Insights", color = TripBlue, style = MaterialTheme.typography.labelMedium) }
                }
            }

            // Expense list
            val filteredExpenses = if (selectedCategoryFilter != null)
                uiState.expenses.filter { it.category == selectedCategoryFilter }
            else uiState.expenses

            if (filteredExpenses.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No expenses yet. Tap + to add!", style = MaterialTheme.typography.bodyMedium, color = TextHint)
                    }
                }
            } else {
                items(filteredExpenses, key = { it.id }) { expense ->
                    ExpenseItem(
                        expense = expense,
                        onDelete = { viewModel.deleteExpense(expense) }
                    )
                }
            }
        }

        // Add expense button
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

@Composable
private fun BudgetRing(
    spent: Double,
    total: Double,
    modifier: Modifier = Modifier
) {
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
            val rect = androidx.compose.ui.geometry.Rect(
                padding, padding, size.width - padding, size.height - padding
            )

            // Background ring
            drawArc(
                color = Color(0xFFE0E0E0),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(padding, padding),
                size = Size(size.width - stroke, size.height - stroke),
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
            // Progress ring
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
                color = TextPrimary
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

@Composable
private fun ExpenseItem(
    expense: Expense,
    onDelete: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("MMM d, HH:mm")
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
        modifier = Modifier.fillMaxWidth(),
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
                modifier = Modifier.size(40.dp)
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
            Text(
                "-$${"%.2f".format(expense.amount)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddExpenseDialog(
    onDismiss: () -> Unit,
    onAdd: (String, Double, ExpenseCategory) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(ExpenseCategory.FOOD) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Expense", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
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
                    singleLine = true
                )
                // Category selector
                Text("Category", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(ExpenseCategory.values()) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text("${cat.emoji} ${cat.label}", style = MaterialTheme.typography.labelSmall) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TripBlue,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amt = amount.toDoubleOrNull() ?: 0.0
                    if (title.isNotBlank() && amt > 0) onAdd(title, amt, selectedCategory)
                },
                colors = ButtonDefaults.buttonColors(containerColor = TripAccent)
            ) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextSecondary) }
        }
    )
}
