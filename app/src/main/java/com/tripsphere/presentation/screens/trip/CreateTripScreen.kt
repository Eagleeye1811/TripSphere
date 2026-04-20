package com.tripsphere.presentation.screens.trip

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tripsphere.presentation.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTripScreen(
    initialDestination: String = "",
    initialImageUrl: String = "",
    onNavigateToWorkspace: (String, String, String, Double, String) -> Unit,
    onNavigateBack: () -> Unit
) {
    var destination by remember { mutableStateOf(initialDestination) }
    var imageUrl by remember { mutableStateOf(initialImageUrl) }
    var budget by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }

    // Date pickers
    var startDate by remember { mutableStateOf<LocalDate?>(null) }
    var endDate by remember { mutableStateOf<LocalDate?>(null) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

    // Date picker states
    val startDatePickerState = rememberDatePickerState()
    val endDatePickerState = rememberDatePickerState()

    if (showStartDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                Button(onClick = {
                    startDatePickerState.selectedDateMillis?.let { millis ->
                        startDate = java.time.Instant.ofEpochMilli(millis)
                            .atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                    }
                    showStartDatePicker = false
                }) { Text("Confirm") }
            }
        ) {
            DatePicker(state = startDatePickerState)
        }
    }

    if (showEndDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                Button(onClick = {
                    endDatePickerState.selectedDateMillis?.let { millis ->
                        endDate = java.time.Instant.ofEpochMilli(millis)
                            .atZone(java.time.ZoneId.systemDefault()).toLocalDate()
                    }
                    showEndDatePicker = false
                }) { Text("Confirm") }
            }
        ) {
            DatePicker(state = endDatePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(TripBlueDark, TripBlue)))
                .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 24.dp)
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(Icons.Default.ArrowBack, null, tint = Color.White)
            }
            Text(
                "Plan Your Trip",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Where are you going?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // Destination field
            OutlinedTextField(
                value = destination,
                onValueChange = { destination = it },
                label = { Text("Destination") },
                leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = TripBlue) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TripBlue,
                    unfocusedBorderColor = TextHint.copy(alpha = 0.5f)
                )
            )

            // Date pickers row — IntrinsicSize.Max keeps both fields identical height
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Start date
                OutlinedTextField(
                    value = startDate?.format(formatter) ?: "",
                    onValueChange = { },
                    label = { Text("Start Date") },
                    readOnly = true,
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Default.CalendarToday, null, tint = TripBlue)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable { showStartDatePicker = true },
                    shape = RoundedCornerShape(14.dp),
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledLeadingIconColor = TripBlue,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                // End date
                OutlinedTextField(
                    value = endDate?.format(formatter) ?: "",
                    onValueChange = { },
                    label = { Text("End Date") },
                    readOnly = true,
                    singleLine = true,
                    leadingIcon = {
                        Icon(Icons.Default.CalendarMonth, null, tint = TripBlue)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable { showEndDatePicker = true },
                    shape = RoundedCornerShape(14.dp),
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledLeadingIconColor = TripBlue,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            // Budget field
            OutlinedTextField(
                value = budget,
                onValueChange = { budget = it },
                label = { Text("Total Budget ($)") },
                leadingIcon = { Icon(Icons.Default.AttachMoney, null, tint = TripBlue) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TripBlue,
                    unfocusedBorderColor = TextHint.copy(alpha = 0.5f)
                )
            )

            // Duration display
            if (startDate != null && endDate != null) {
                val days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate)
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = TripBlue.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Schedule, null, tint = TripBlue)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "$days day trip",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = TripBlue
                        )
                    }
                }
            }

            if (errorMsg.isNotEmpty()) {
                Text(errorMsg, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    when {
                        destination.isBlank() -> errorMsg = "Please enter a destination"
                        startDate == null -> errorMsg = "Please select a start date"
                        endDate == null -> errorMsg = "Please select an end date"
                        endDate!!.isBefore(startDate) -> errorMsg = "End date must be after start date"
                        budget.isBlank() -> errorMsg = "Please enter your budget"
                        budget.toDoubleOrNull() == null -> errorMsg = "Invalid budget amount"
                        budget.toDouble() <= 0 -> errorMsg = "Budget must be greater than 0"
                        else -> {
                            errorMsg = ""
                            onNavigateToWorkspace(
                                destination,
                                startDate.toString(),
                                endDate.toString(),
                                budget.toDouble(),
                                imageUrl
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TripBlue)
            ) {
                Text(
                    "Continue to Workspace",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, null)
            }
        }
    }
}
