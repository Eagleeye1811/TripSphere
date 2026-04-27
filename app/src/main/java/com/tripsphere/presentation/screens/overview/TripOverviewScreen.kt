package com.tripsphere.presentation.screens.overview

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.tripsphere.data.local.DestinationAttractionsDataset
import com.tripsphere.domain.model.Itinerary
import com.tripsphere.presentation.components.BudgetProgressBar
import com.tripsphere.presentation.components.LoadingScreen
import com.tripsphere.presentation.ui.theme.*
import com.tripsphere.presentation.viewmodel.TripOverviewViewModel
import com.tripsphere.utils.DummyData
import java.time.format.DateTimeFormatter

@Composable
fun TripOverviewScreen(
    tripId: Long,
    onNavigateToActiveTrip: () -> Unit,   // repurposed: now goes to MyTrips
    onNavigateToManageExpenses: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: TripOverviewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy")
    var showDeleteConfirm by remember { mutableStateOf(false) }

    LaunchedEffect(tripId) {
        viewModel.loadTrip(tripId)
    }

    // Navigate to MyTrips once the trip is marked ONGOING
    LaunchedEffect(uiState.tripStarted) {
        if (uiState.tripStarted) onNavigateToActiveTrip()
    }

    // Navigate back to MyTrips after deletion
    LaunchedEffect(uiState.tripDeleted) {
        if (uiState.tripDeleted) onNavigateToActiveTrip()
    }

    // Delete confirmation dialog
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            icon = {
                Icon(
                    Icons.Default.DeleteForever,
                    contentDescription = null,
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(32.dp)
                )
            },
            title = { Text("Delete Trip?", fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "This will permanently delete \"${uiState.trip?.destination}\" and all its itinerary. This action cannot be undone.",
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.deleteTrip(); showDeleteConfirm = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Delete", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (uiState.isLoading) { LoadingScreen(); return }

    val trip = uiState.trip ?: run {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Trip not found", color = TextSecondary)
        }
        return
    }

    val navBarBottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 130.dp + navBarBottomPadding)
        ) {
            // Hero image
            item {
                Box(modifier = Modifier.fillMaxWidth().height(260.dp)) {
                    AsyncImage(
                        model = trip.imageUrl.ifEmpty {
                            "https://images.unsplash.com/photo-1488646953014-85cb44e25828?w=800"
                        },
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(listOf(Color(0x33000000), Color(0xBB000000)))
                            )
                    )
                    if (trip.status.name == "ONGOING") {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(16.dp, 48.dp),
                            shape = RoundedCornerShape(6.dp),
                            color = TripAccent
                        ) {
                            Text(
                                "ACTIVE TRIP",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Main content card
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {

                        // ── Trip title + weather chip ──────────────────────
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                trip.destination,
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = TripBlue.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    "16°C",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = TripBlue
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CalendarMonth, null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(
                                "${formatter.format(trip.startDate)} – ${formatter.format(trip.endDate)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }

                        Spacer(Modifier.height(20.dp))

                        // ── Budget ────────────────────────────────────────
                        Text("Budget Overview", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))
                        BudgetProgressBar(spent = 0.0, total = trip.budget)

                        Spacer(Modifier.height(20.dp))

                        // ── Itinerary preview list ────────────────────────
                        if (uiState.itineraries.isNotEmpty()) {
                            Text("Itinerary Preview", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            val itineraryByDay = uiState.itineraries.groupBy { it.day }
                            itineraryByDay.keys.sorted().forEach { day ->
                                val dayItems = itineraryByDay[day] ?: emptyList()
                                val dayColor = when (day % 5) {
                                    1    -> Color(0xFF1565C0)
                                    2    -> Color(0xFFE65100)
                                    3    -> Color(0xFF2E7D32)
                                    4    -> Color(0xFF6A1B9A)
                                    else -> Color(0xFF00695C)
                                }
                                // Day header
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = if (day == itineraryByDay.keys.min()) 0.dp else 12.dp, bottom = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(shape = RoundedCornerShape(6.dp), color = dayColor) {
                                        Text(
                                            "Day $day",
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        "${dayItems.size} place${if (dayItems.size != 1) "s" else ""}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = dayColor
                                    )
                                }
                                // Activities for this day
                                dayItems.forEachIndexed { index, item ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 8.dp, top = 4.dp, bottom = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = dayColor.copy(alpha = 0.1f)
                                        ) {
                                            Text(
                                                item.time,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                                style = MaterialTheme.typography.labelSmall,
                                                color = dayColor,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            item.activity,
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                    if (index < dayItems.size - 1) {
                                        Divider(
                                            modifier = Modifier.padding(start = 8.dp),
                                            color = TextHint.copy(alpha = 0.15f)
                                        )
                                    }
                                }
                                Divider(color = TextHint.copy(alpha = 0.3f), modifier = Modifier.padding(top = 8.dp))
                            }
                        }

                        // ── Notes ─────────────────────────────────────────
                        if (trip.notes.isNotEmpty()) {
                            Spacer(Modifier.height(20.dp))
                            Text("Notes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                            ) {
                                Text(
                                    trip.notes,
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary
                                )
                            }
                        }

                        // ── Itinerary Map ─────────────────────────────────
                        Spacer(Modifier.height(20.dp))
                        Text("Itinerary Map", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        ItineraryMapSection(
                            destination = trip.destination,
                            itineraries = uiState.itineraries
                        )
                    }
                }
            }
        }

        // Top bar (floats above the scroll)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 8.dp, end = 8.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.Default.ArrowBack, null, tint = Color.White)
            }
            Text("Trip Overview", style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
            // Share button — sends trip summary as plain text
            IconButton(onClick = {
                val trip = uiState.trip ?: return@IconButton
                val shareText = buildString {
                    appendLine("✈️ Trip to ${trip.destination}")
                    appendLine("📅 ${formatter.format(trip.startDate)} – ${formatter.format(trip.endDate)}")
                    appendLine("💰 Budget: ${"$"}${trip.budget.toInt()}")
                    if (trip.notes.isNotEmpty()) appendLine("📝 ${trip.notes}")
                    if (uiState.itineraries.isNotEmpty()) {
                        appendLine()
                        appendLine("📋 Itinerary:")
                        uiState.itineraries.groupBy { it.day }.entries.sortedBy { it.key }
                            .forEach { (day, items) ->
                                appendLine("  Day $day:")
                                items.forEach { appendLine("    • ${it.time}  ${it.activity}") }
                            }
                    }
                    appendLine()
                    append("Shared via TripSphere 🌍")
                }
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "My trip to ${trip.destination}")
                    putExtra(Intent.EXTRA_TEXT, shareText)
                }
                context.startActivity(Intent.createChooser(intent, "Share Trip"))
            }) {
                Icon(Icons.Default.Share, null, tint = Color.White)
            }
        }

        // Bottom CTA
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Primary: Start Trip
                Button(
                    onClick = { viewModel.startTrip() },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TripBlue),
                    enabled = uiState.trip?.status?.name != "ONGOING"
                ) {
                    Icon(Icons.Default.FlightTakeoff, null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        if (uiState.trip?.status?.name == "ONGOING") "Trip in Progress" else "Start Trip",
                        fontWeight = FontWeight.Bold
                    )
                }

                // Secondary row: Edit + Manage Expenses
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(22.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TripBlue)
                    ) {
                        Icon(Icons.Default.Edit, null, tint = TripBlue, modifier = Modifier.size(15.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Edit", color = TripBlue, style = MaterialTheme.typography.labelLarge)
                    }
                    OutlinedButton(
                        onClick = onNavigateToManageExpenses,
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(22.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, TripAccent)
                    ) {
                        Icon(Icons.Default.AccountBalanceWallet, null, tint = TripAccent, modifier = Modifier.size(15.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Expenses", color = TripAccent, style = MaterialTheme.typography.labelLarge)
                    }
                    OutlinedButton(
                        onClick = { showDeleteConfirm = true },
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(22.dp),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFD32F2F)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F))
                    ) {
                        Icon(Icons.Default.Delete, null, tint = Color(0xFFD32F2F), modifier = Modifier.size(15.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Delete", color = Color(0xFFD32F2F), style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Itinerary Map Section
// ─────────────────────────────────────────────────────────────────────────────

private val overviewDayColors = listOf(
    Color(0xFF1E88E5),
    Color(0xFFFF7043),
    Color(0xFF43A047),
    Color(0xFF8E24AA),
    Color(0xFFFDD835)
)

private val overviewDayHues = listOf(
    BitmapDescriptorFactory.HUE_AZURE,
    BitmapDescriptorFactory.HUE_ORANGE,
    BitmapDescriptorFactory.HUE_GREEN,
    BitmapDescriptorFactory.HUE_VIOLET,
    BitmapDescriptorFactory.HUE_YELLOW
)

@Composable
private fun ItineraryMapSection(
    destination: String,
    itineraries: List<Itinerary>
) {
    val context = LocalContext.current

    val destLatLng = remember(destination) {
        val match = DummyData.destinations.find {
            destination.contains(it.name, ignoreCase = true) ||
                    it.name.contains(destination.substringBefore(",").trim(), ignoreCase = true)
        }
        if (match != null && (match.latitude != 0.0 || match.longitude != 0.0))
            LatLng(match.latitude, match.longitude)
        else null
    }

    val markerData = remember(destination, itineraries) {
        itineraries.map { item ->
            Triple(
                item.day,
                item,
                DestinationAttractionsDataset.findAttractionByName(destination, item.activity)
            )
        }
    }

    val hasMarkers = markerData.any { it.third != null }
    val itineraryByDay = remember(itineraries) { itineraries.groupBy { it.day } }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            destLatLng ?: LatLng(20.0, 0.0), 12f
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(isMyLocationEnabled = false),
                uiSettings = MapUiSettings(zoomControlsEnabled = true, mapToolbarEnabled = false)
            ) {
                // Destination centre pin — shown only when no itinerary markers are resolved
                if (!hasMarkers && destLatLng != null) {
                    Marker(
                        state = MarkerState(position = destLatLng),
                        title = destination.substringBefore(","),
                        snippet = "Trip destination"
                    )
                }

                // One coloured pin per itinerary activity
                markerData.forEach { (day, item, attraction) ->
                    if (attraction != null) {
                        Marker(
                            state = MarkerState(
                                position = LatLng(attraction.lat, attraction.lon)
                            ),
                            title = item.activity,
                            snippet = "Day $day · ${item.time}",
                            icon = BitmapDescriptorFactory.defaultMarker(
                                overviewDayHues[(day - 1) % overviewDayHues.size]
                            )
                        )
                    }
                }
            }

            // Day-colour legend (top-left overlay)
            if (itineraryByDay.isNotEmpty() && hasMarkers) {
                Card(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.96f)),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
                        itineraryByDay.keys.sorted().forEach { day ->
                            val count = itineraryByDay[day]?.size ?: 0
                            val color = overviewDayColors[(day - 1) % overviewDayColors.size]
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 2.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                )
                                Spacer(Modifier.width(6.dp))
                                Text(
                                    "Day $day · $count place${if (count != 1) "s" else ""}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // "Open Route" button (bottom-right overlay)
            if (itineraries.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(TripBlue)
                        .clickable {
                            val url = buildOverviewRouteUrl(itineraries, destination, destLatLng)
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        }
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Navigation, null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(
                        "Open Route",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

private fun buildOverviewRouteUrl(
    itineraries: List<Itinerary>,
    destination: String,
    fallbackLatLng: LatLng?
): String {
    if (itineraries.isEmpty()) return "https://maps.google.com"

    fun pointFor(item: Itinerary): String {
        val attraction = DestinationAttractionsDataset.findAttractionByName(destination, item.activity)
        return if (attraction != null) "${attraction.lat},${attraction.lon}"
        else Uri.encode("${item.activity}, $destination")
    }

    if (itineraries.size == 1) {
        val attraction = DestinationAttractionsDataset.findAttractionByName(destination, itineraries[0].activity)
        return if (attraction != null)
            "https://www.google.com/maps/search/?api=1&query=${attraction.lat},${attraction.lon}"
        else
            "https://www.google.com/maps/search/?api=1&query=${Uri.encode("${itineraries[0].activity}, $destination")}"
    }

    val origin = pointFor(itineraries.first())
    val dest = pointFor(itineraries.last())
    val waypoints = if (itineraries.size > 2)
        itineraries.subList(1, itineraries.size - 1).joinToString("|") { pointFor(it) }
    else ""

    return buildString {
        append("https://www.google.com/maps/dir/?api=1")
        append("&origin=$origin")
        append("&destination=$dest")
        if (waypoints.isNotEmpty()) append("&waypoints=$waypoints")
        append("&travelmode=walking")
    }
}
