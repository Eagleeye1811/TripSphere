package com.tripsphere.presentation.screens.planner

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import com.tripsphere.presentation.ui.theme.*
import com.tripsphere.presentation.viewmodel.MyTripsViewModel
import com.tripsphere.utils.DummyData

// Popular destinations shown as quick-pick cards — drawn from DummyData so IDs are consistent
private val popularPickIds = listOf(2, 3, 5, 1, 7, 8) // Bali, Paris, Kyoto, Santorini, Swiss Alps, Amalfi

@Composable
fun PlannerScreen(
    onStartPlanning: (destination: String, imageUrl: String) -> Unit,
    onTripClick: (Long) -> Unit,
    viewModel: MyTripsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val popularDestinations = remember {
        DummyData.destinations.filter { it.id in popularPickIds }
            .sortedBy { popularPickIds.indexOf(it.id) }
    }

    val recentTrips = (uiState.ongoingTrips + uiState.upcomingTrips).distinctBy { it.id }.take(3)

    val navBarBottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 100.dp + navBarBottomPadding)
    ) {
        // ── Hero Header ──────────────────────────────────────────────────────
        item {
            PlannerHeader(
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it },
                onSearch = {
                    if (searchQuery.isNotBlank()) {
                        focusManager.clearFocus()
                        onStartPlanning(searchQuery, "")
                    }
                }
            )
        }

        // ── Search suggestion chips ──────────────────────────────────────────
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
                            if (dest != suggestions.last()) HorizontalDivider(color = TextHint.copy(alpha = 0.15f))
                        }
                    }
                }
            }
        }

        // ── How It Works ─────────────────────────────────────────────────────
        item {
            Spacer(Modifier.height(28.dp))
            PlannerSectionTitle("How It Works")
            Spacer(Modifier.height(12.dp))
            HowItWorksRow()
        }

        // ── Popular Destinations ─────────────────────────────────────────────
        item {
            Spacer(Modifier.height(28.dp))
            PlannerSectionTitle("Popular Destinations")
            Text(
                "Tap to start planning instantly",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
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
                        bestTime = dest.bestTimeToVisit,
                        onClick = { onStartPlanning("${dest.name}, ${dest.country}", dest.imageUrl) }
                    )
                }
            }
        }

        // ── My Itineraries ───────────────────────────────────────────────────
        if (recentTrips.isNotEmpty()) {
            item {
                Spacer(Modifier.height(28.dp))
                PlannerSectionTitle("My Itineraries")
                Text(
                    "Continue where you left off",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(12.dp))
            }
            items(recentTrips, key = { it.id }) { trip ->
                MyItineraryCard(
                    trip = trip,
                    onClick = { onTripClick(trip.id) },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(Modifier.height(12.dp))
            }
        }

        // ── Empty state when no trips ────────────────────────────────────────
        if (recentTrips.isEmpty() && uiState.completedTrips.isEmpty()) {
            item {
                Spacer(Modifier.height(28.dp))
                EmptyPlannerState()
            }
        }
    }
}

// ─── Header ───────────────────────────────────────────────────────────────────

@Composable
private fun PlannerHeader(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(TripBlueDark, TripBlue, Color(0xFF2196F3))
                )
            )
    ) {
        // Background decorative circles
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
                .padding(start = 20.dp, end = 20.dp, top = 24.dp, bottom = 28.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.18f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.TravelExplore, null, tint = Color.White, modifier = Modifier.size(22.dp))
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        "Trip Planner",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        "Where to next?",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
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
                        onValueChange = onSearchChange,
                        placeholder = {
                            Text(
                                "Search destinations…",
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
                        keyboardActions = KeyboardActions(onSearch = { onSearch() })
                    )
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = onSearch, modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Default.ArrowForward, null, tint = TripBlue)
                        }
                    }
                }
            }
        }
    }
}

// ─── How It Works row ─────────────────────────────────────────────────────────

@Composable
private fun HowItWorksRow() {
    val steps = listOf(
        Triple(Icons.Default.Search, "Pick Destination", Color(0xFF1565C0)),
        Triple(Icons.Default.DateRange, "Set Dates", Color(0xFF00897B)),
        Triple(Icons.Default.EditNote, "Build Itinerary", Color(0xFFE65100)),
        Triple(Icons.Default.FlightTakeoff, "Travel!", Color(0xFF6A1B9A))
    )
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        items(steps.size) { i ->
            val (icon, label, color) = steps[i]
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(color.copy(alpha = 0.12f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, null, tint = color, modifier = Modifier.size(26.dp))
                }
                if (i < steps.lastIndex) {
                    // small arrow in between
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    label,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 11.sp
                )
            }
        }
    }
}

// ─── Popular Destination Card ─────────────────────────────────────────────────

@Composable
private fun PopularDestinationCard(
    name: String,
    country: String,
    imageUrl: String,
    budgetEstimate: String,
    bestTime: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(170.dp)
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
                    .height(220.dp),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(
                        Brush.verticalGradient(
                            0.3f to Color.Transparent,
                            1.0f to Color.Black.copy(alpha = 0.82f)
                        )
                    )
            )
            // "Plan" chip
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
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = Color.White.copy(0.8f), modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(2.dp))
                    Text(country, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(0.8f))
                }
                Spacer(Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(shape = RoundedCornerShape(10.dp), color = Color.White.copy(alpha = 0.18f)) {
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
}

// ─── My Itinerary Card ────────────────────────────────────────────────────────

@Composable
private fun MyItineraryCard(
    trip: Trip,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = trip.imageUrl.ifEmpty { "https://images.unsplash.com/photo-1488646953014-85cb44e25828?w=300" },
                contentDescription = null,
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    trip.destination.substringBefore(","),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(3.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CalendarToday, null, tint = TextSecondary, modifier = Modifier.size(13.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(
                        "${trip.startDate} → ${trip.endDate}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
                Spacer(Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = TripBlue.copy(alpha = 0.1f)
                ) {
                    Text(
                        "View Itinerary",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = TripBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Icon(Icons.Default.ChevronRight, null, tint = TextHint, modifier = Modifier.size(22.dp))
        }
    }
}

// ─── Empty State ──────────────────────────────────────────────────────────────

@Composable
private fun EmptyPlannerState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    Brush.radialGradient(
                        listOf(TripBlue.copy(0.15f), TripBlue.copy(0.04f))
                    ),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.TravelExplore, null, tint = TripBlue, modifier = Modifier.size(52.dp))
        }
        Spacer(Modifier.height(20.dp))
        Text(
            "No itineraries yet",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Search a destination above or tap one of the popular picks to start building your perfect trip",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

// ─── Section title helper ─────────────────────────────────────────────────────

@Composable
private fun PlannerSectionTitle(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 0.dp)
    )
}
