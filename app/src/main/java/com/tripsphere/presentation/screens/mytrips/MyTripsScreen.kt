package com.tripsphere.presentation.screens.mytrips

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.tripsphere.domain.model.Trip
import com.tripsphere.presentation.components.EmptyStateView
import com.tripsphere.presentation.components.LoadingScreen
import com.tripsphere.presentation.ui.theme.*
import com.tripsphere.presentation.viewmodel.MyTripsViewModel
import com.tripsphere.utils.DummyData
import java.time.format.DateTimeFormatter

// IDs from DummyData — Bali, Paris, Kyoto, Santorini, Swiss Alps, Amalfi
private val popularPickIds = listOf(2, 3, 5, 1, 7, 8)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTripsScreen(
    onTripClick: (Long) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToCreateTrip: () -> Unit = {},
    onStartPlanning: (destination: String, imageUrl: String) -> Unit = { _, _ -> },
    viewModel: MyTripsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    if (uiState.isLoading) { LoadingScreen(); return }

    val popularDestinations = remember {
        DummyData.destinations.filter { it.id in popularPickIds }
            .sortedBy { popularPickIds.indexOf(it.id) }
    }

    val allTrips = uiState.ongoingTrips + uiState.upcomingTrips + uiState.completedTrips

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 120.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp) // spacing handled per-item
    ) {
        // ── Hero Header + Search ───────────────────────────────────────────────
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(TripBlueDark, TripBlue)))
            ) {
                // Decorative circles
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .offset(x = 220.dp, y = (-40).dp)
                        .background(Color.White.copy(alpha = 0.06f), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .offset(x = (-30).dp, y = 60.dp)
                        .background(Color.White.copy(alpha = 0.05f), CircleShape)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                "My Trips",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                if (allTrips.isEmpty()) "Plan your first adventure"
                                else "${allTrips.size} trip${if (allTrips.size != 1) "s" else ""} planned",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.75f)
                            )
                        }
                        // New trip FAB
                        FloatingActionButton(
                            onClick = onNavigateToCreateTrip,
                            containerColor = Color.White,
                            contentColor = TripBlue,
                            elevation = FloatingActionButtonDefaults.elevation(4.dp),
                            modifier = Modifier.size(46.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "New Trip", modifier = Modifier.size(22.dp))
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // Search bar
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 8.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Search, null, tint = TripBlue, modifier = Modifier.size(22.dp))
                            Spacer(Modifier.width(8.dp))
                            TextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = {
                                    Text(
                                        "Search destinations to plan…",
                                        color = TextHint,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                },
                                modifier = Modifier.weight(1f),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(onSearch = {
                                    if (searchQuery.isNotBlank()) {
                                        focusManager.clearFocus()
                                        onStartPlanning(searchQuery, "")
                                    }
                                })
                            )
                            if (searchQuery.isNotBlank()) {
                                IconButton(
                                    onClick = {
                                        focusManager.clearFocus()
                                        onStartPlanning(searchQuery, "")
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = TripBlue)
                                }
                            }
                        }
                    }
                }
            }
        }

        // ── Search suggestions dropdown ────────────────────────────────────────
        if (searchQuery.isNotBlank()) {
            val suggestions = DummyData.destinations.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.country.contains(searchQuery, ignoreCase = true)
            }
            if (suggestions.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .padding(top = 8.dp)
                            .shadow(8.dp, RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                            .padding(vertical = 8.dp)
                    ) {
                        suggestions.forEach { dest ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        focusManager.clearFocus()
                                        searchQuery = ""
                                        onStartPlanning("${dest.name}, ${dest.country}", dest.imageUrl)
                                    }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(Icons.Default.LocationOn, null, tint = TripBlue, modifier = Modifier.size(20.dp))
                                Column {
                                    Text(dest.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                                    Text(dest.country, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                }
                            }
                            if (dest != suggestions.last()) {
                                HorizontalDivider(color = TextHint.copy(alpha = 0.15f))
                            }
                        }
                    }
                }
            }
        }

        // ── Popular Destinations ───────────────────────────────────────────────
        item {
            Spacer(Modifier.height(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Popular Destinations",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        "Tap to start planning instantly",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                items(popularDestinations) { dest ->
                    PopularDestinationCard(
                        name = dest.name,
                        country = dest.country,
                        imageUrl = dest.imageUrl,
                        budgetEstimate = dest.budgetEstimate,
                        onClick = {
                            onStartPlanning("${dest.name}, ${dest.country}", dest.imageUrl)
                        }
                    )
                }
            }
        }

        // ── My Trips section ──────────────────────────────────────────────────
        if (allTrips.isNotEmpty()) {
            item {
                Spacer(Modifier.height(28.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "My Itineraries",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        "${allTrips.size} total",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
                Spacer(Modifier.height(4.dp))
            }

            // Ongoing
            if (uiState.ongoingTrips.isNotEmpty()) {
                item { SectionLabel("🟢  Active", TripAccent) }
                items(uiState.ongoingTrips, key = { it.id }) { trip ->
                    SwipeToDismissTripItem(
                        trip = trip,
                        onDelete = { viewModel.deleteTrip(trip) },
                        onClick = { onTripClick(trip.id) }
                    )
                }
            }

            // Upcoming
            if (uiState.upcomingTrips.isNotEmpty()) {
                item { SectionLabel("🗓  Upcoming", TripBlue) }
                items(uiState.upcomingTrips, key = { it.id }) { trip ->
                    SwipeToDismissTripItem(
                        trip = trip,
                        onDelete = { viewModel.deleteTrip(trip) },
                        onClick = { onTripClick(trip.id) }
                    )
                }
            }

            // Completed
            if (uiState.completedTrips.isNotEmpty()) {
                item { SectionLabel("✅  Completed", Color(0xFF2E7D32)) }
                items(uiState.completedTrips, key = { it.id }) { trip ->
                    SwipeToDismissTripItem(
                        trip = trip,
                        onDelete = { viewModel.deleteTrip(trip) },
                        onClick = { onTripClick(trip.id) }
                    )
                }
            }

            item {
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, null, tint = TextHint, modifier = Modifier.size(13.dp))
                    Spacer(Modifier.width(5.dp))
                    Text(
                        "Swipe left to delete a trip",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextHint
                    )
                }
            }
        } else {
            // Empty state — only trips list is empty, not the whole screen
            item {
                Spacer(Modifier.height(28.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.CardTravel,
                        null,
                        tint = TextHint,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "No itineraries yet",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "Pick a popular destination above or search for any city to get started.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Section label
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SectionLabel(text: String, color: Color) {
    Spacer(Modifier.height(8.dp))
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
    )
    Spacer(Modifier.height(2.dp))
}

// ─────────────────────────────────────────────────────────────────────────────
// Popular destination card (image + name + country + budget chip + "Plan" tag)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun PopularDestinationCard(
    name: String,
    country: String,
    imageUrl: String,
    budgetEstimate: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box {
            AsyncImage(
                model = imageUrl,
                contentDescription = name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp),
                contentScale = ContentScale.Crop
            )
            // Gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .background(
                        Brush.verticalGradient(
                            0.3f to Color.Transparent,
                            1.0f to Color.Black.copy(alpha = 0.82f)
                        )
                    )
            )
            // "Plan" chip — top right
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = TripBlue.copy(alpha = 0.92f),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.DateRange, null, tint = Color.White, modifier = Modifier.size(10.dp))
                    Spacer(Modifier.width(3.dp))
                    Text("Plan", style = MaterialTheme.typography.labelSmall, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
            // Bottom info
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = Color.White.copy(0.8f), modifier = Modifier.size(11.dp))
                    Spacer(Modifier.width(2.dp))
                    Text(country, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(0.8f))
                }
                Spacer(Modifier.height(5.dp))
                Surface(shape = RoundedCornerShape(8.dp), color = Color.White.copy(alpha = 0.18f)) {
                    Text(
                        budgetEstimate,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontSize = 9.sp
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Swipe-to-delete trip item
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDismissTripItem(trip: Trip, onDelete: () -> Unit, onClick: () -> Unit) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) { onDelete(); true } else false
        },
        positionalThreshold = { it * 0.4f }
    )

    Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)) {
        SwipeToDismissBox(
            state = dismissState,
            backgroundContent = {
                val bgColor by animateColorAsState(
                    targetValue = when (dismissState.targetValue) {
                        SwipeToDismissBoxValue.EndToStart -> Color(0xFFD32F2F)
                        else -> Color.Transparent
                    },
                    animationSpec = tween(200),
                    label = "trip_swipe_bg"
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(bgColor, RoundedCornerShape(20.dp))
                        .padding(end = 24.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Delete, null, tint = Color.White, modifier = Modifier.size(24.dp))
                            Text("Delete", color = Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            },
            enableDismissFromStartToEnd = false,
            enableDismissFromEndToStart = true
        ) {
            TripDetailCard(trip = trip, onClick = onClick)
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Rich trip detail card
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun TripDetailCard(trip: Trip, onClick: () -> Unit) {
    val formatter = DateTimeFormatter.ofPattern("d MMM")

    val statusColor = when (trip.status.name) {
        "ONGOING"   -> TripAccent
        "COMPLETED" -> Color(0xFF2E7D32)
        else        -> TripBlue
    }
    val statusLabel = when (trip.status.name) {
        "ONGOING"   -> "Active"
        "COMPLETED" -> "Completed"
        else        -> "Upcoming"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            // Hero image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(155.dp)
            ) {
                AsyncImage(
                    model = trip.imageUrl.ifEmpty { "https://images.unsplash.com/photo-1488646953014-85cb44e25828?w=800" },
                    contentDescription = trip.destination,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                0.3f to Color.Transparent,
                                1.0f to Color.Black.copy(0.65f)
                            )
                        )
                )
                // Status badge
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = statusColor
                ) {
                    Text(
                        statusLabel,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                // Destination name
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp)
                ) {
                    Text(
                        trip.destination.substringBefore(","),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (trip.destination.contains(",")) {
                        Text(
                            trip.destination.substringAfter(",").trim(),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.82f)
                        )
                    }
                }
            }

            // Dates + budget
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, null, tint = TripBlue, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(5.dp))
                    Text(
                        "${formatter.format(trip.startDate)} – ${formatter.format(trip.endDate)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = TripBlue.copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.AccountBalanceWallet, null, tint = TripBlue, modifier = Modifier.size(13.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "$${"%.0f".format(trip.budget)}",
                            style = MaterialTheme.typography.labelMedium,
                            color = TripBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Notes preview
            if (trip.notes.isNotEmpty()) {
                HorizontalDivider(modifier = Modifier.padding(horizontal = 14.dp), color = TextHint.copy(alpha = 0.12f))
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.AutoMirrored.Filled.Notes, null, tint = TextHint, modifier = Modifier.size(13.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(
                        trip.notes.lines().first(),
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // View full itinerary arrow
            HorizontalDivider(modifier = Modifier.padding(horizontal = 14.dp), color = TextHint.copy(alpha = 0.12f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "View full itinerary",
                    style = MaterialTheme.typography.labelMedium,
                    color = TripBlue,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(Icons.Default.ChevronRight, null, tint = TripBlue, modifier = Modifier.size(18.dp))
            }
        }
    }
}
