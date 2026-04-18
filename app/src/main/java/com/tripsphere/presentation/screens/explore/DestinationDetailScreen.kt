package com.tripsphere.presentation.screens.explore

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.draw.clip
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.tripsphere.domain.model.Destination
import com.tripsphere.domain.model.Place
import com.tripsphere.presentation.ui.theme.*
import com.tripsphere.presentation.viewmodel.NearbyPlacesState
import com.tripsphere.presentation.viewmodel.PlacesViewModel
import com.tripsphere.presentation.viewmodel.WeatherUiState
import com.tripsphere.presentation.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationDetailScreen(
    destination: Destination,
    onPlanTrip: () -> Unit,
    onNavigateBack: () -> Unit,
    weatherViewModel: WeatherViewModel = hiltViewModel(),
    placesViewModel: PlacesViewModel = hiltViewModel()
) {
    val weatherState by weatherViewModel.weatherState.collectAsState()
    val nearbyState by placesViewModel.nearbyState.collectAsState()

    LaunchedEffect(destination.id) {
        weatherViewModel.loadWeather(destination.latitude, destination.longitude)
        if (destination.latitude != 0.0 || destination.longitude != 0.0) {
            placesViewModel.loadNearbyPlaces(destination.latitude, destination.longitude)
        }
    }

    // Pinch-to-zoom state for hero image
    var imageScale by remember { mutableStateOf(1f) }
    var imageOffset by remember { mutableStateOf(Offset.Zero) }
    val transformableState = rememberTransformableState { zoomChange, panChange, _ ->
        imageScale = (imageScale * zoomChange).coerceIn(1f, 3f)
        if (imageScale > 1f) {
            imageOffset += panChange
        } else {
            imageOffset = Offset.Zero
        }
    }
    // Reset offset when scale returns to 1
    LaunchedEffect(imageScale) {
        if (imageScale <= 1f) imageOffset = Offset.Zero
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            // Hero image with pinch-to-zoom
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                ) {
                    AsyncImage(
                        model = destination.imageUrl,
                        contentDescription = destination.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer(
                                scaleX = imageScale,
                                scaleY = imageScale,
                                translationX = imageOffset.x,
                                translationY = imageOffset.y
                            )
                            .transformable(state = transformableState),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color.Transparent, Color(0x99000000)),
                                    startY = 120f
                                )
                            )
                    )
                    if (imageScale > 1f) {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = Color.Black.copy(alpha = 0.5f)
                        ) {
                            Text(
                                "Pinch to zoom · ${String.format("%.1f", imageScale)}x",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // Content card
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            destination.name,
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.LocationOn, null,
                                tint = TripBlue, modifier = Modifier.size(16.dp)
                            )
                            Text(
                                destination.country,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        // Tags row
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = TripBlue.copy(alpha = 0.1f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.CalendarMonth, null,
                                        tint = TripBlue, modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        "Best: ${destination.bestTimeToVisit}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TripBlue
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFFFF3E0)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Star, null,
                                        tint = Color(0xFFF57C00), modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        "${destination.rating} Rating",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFFF57C00)
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        // ── Live Weather Card ────────────────────────────────────
                        WeatherCard(weatherState = weatherState)

                        Spacer(Modifier.height(16.dp))

                        Text("About", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            destination.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            lineHeight = MaterialTheme.typography.bodyLarge.lineHeight
                        )

                        Spacer(Modifier.height(16.dp))

                        // Budget estimate
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.AccountBalanceWallet, null,
                                    tint = TripBlue, modifier = Modifier.size(24.dp)
                                )
                                Spacer(Modifier.width(12.dp))
                                Column {
                                    Text(
                                        "Budget Estimate",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = TextSecondary
                                    )
                                    Text(
                                        destination.budgetEstimate,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = TripBlue
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Top Attractions",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            TextButton(onClick = { }) {
                                Text("See All", color = TripBlue)
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        destination.topAttractions.take(5).forEach { attraction ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Place, null,
                                    tint = TripAccent, modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    attraction,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextPrimary
                                )
                            }
                            Divider(color = TextHint.copy(alpha = 0.2f))
                        }

                        // ── Nearby Places (Foursquare) ────────────────────────
                        Spacer(Modifier.height(24.dp))
                        NearbyPlacesSection(nearbyState = nearbyState)
                    }
                }
            }
        }

        // Top bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 8.dp, end = 8.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(12.dp))
            ) {
                Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
            }

            Text(
                "Destination Details",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            IconButton(
                onClick = { },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(12.dp))
            ) {
                Icon(Icons.Default.Share, null, tint = Color.White)
            }
        }

        // Bottom CTA
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Button(
                onClick = onPlanTrip,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(27.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TripBlue)
            ) {
                Icon(Icons.Default.AddCircle, null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text(
                    "Plan Trip",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun WeatherCard(weatherState: WeatherUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF0277BD).copy(alpha = 0.08f)
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp, Color(0xFF0277BD).copy(alpha = 0.2f)
        )
    ) {
        when (weatherState) {
            is WeatherUiState.Loading -> {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = TripBlue
                    )
                    Text(
                        "Loading weather…",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }

            is WeatherUiState.Success -> {
                val w = weatherState.weather
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(w.icon, fontSize = 32.sp)
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "${w.temperature}°C · ${w.description}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                        Text(
                            "Wind ${w.windspeed} km/h · ${if (w.isDay) "Daytime" else "Night"}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = TripBlue.copy(alpha = 0.12f)
                    ) {
                        Text(
                            "LIVE",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = TripBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            is WeatherUiState.Error -> {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.WifiOff, null,
                        tint = TextHint, modifier = Modifier.size(16.dp)
                    )
                    Text(
                        "Weather unavailable",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextHint
                    )
                }
            }

            else -> {}
        }
    }
}

// ── Nearby Places Section (Foursquare) ────────────────────────────────────────

@Composable
private fun NearbyPlacesSection(nearbyState: NearbyPlacesState) {
    Text(
        "Nearby Places",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold
    )
    Spacer(Modifier.height(12.dp))

    when (nearbyState) {
        is NearbyPlacesState.Loading -> {
            Box(Modifier.fillMaxWidth().height(140.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = TripBlue)
            }
        }
        is NearbyPlacesState.Error -> {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f))
            ) {
                Text(
                    nearbyState.message,
                    modifier = Modifier.padding(14.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        is NearbyPlacesState.Success -> {
            if (nearbyState.places.isEmpty()) {
                Text("No places found nearby.", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            } else {
                // Category filter chips
                val categories = listOf("All") + nearbyState.places.map { it.category }.distinct().take(5)
                var selected by remember { mutableStateOf("All") }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 12.dp)
                ) {
                    items(categories.size) { idx ->
                        FilterChip(
                            selected = selected == categories[idx],
                            onClick = { selected = categories[idx] },
                            label = { Text(categories[idx], style = MaterialTheme.typography.labelMedium) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TripBlue,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                val filtered = if (selected == "All") nearbyState.places
                               else nearbyState.places.filter { it.category == selected }

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(end = 8.dp)
                ) {
                    items(filtered.size) { idx -> NearbyPlaceCard(filtered[idx]) }
                }
            }
        }
        else -> Unit
    }
}

@Composable
private fun NearbyPlaceCard(place: Place) {
    Card(
        modifier = Modifier.width(200.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            // Photo
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(TripBlue.copy(alpha = 0.12f))
            ) {
                if (place.photoUrl != null) {
                    AsyncImage(
                        model = place.photoUrl,
                        contentDescription = place.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        Icons.Default.Place, null,
                        modifier = Modifier.align(Alignment.Center).size(40.dp),
                        tint = TripBlue.copy(alpha = 0.5f)
                    )
                }

                // Open badge
                place.isOpenNow?.let { open ->
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        shape = RoundedCornerShape(6.dp),
                        color = if (open) Color(0xFF4CAF50) else Color(0xFFF44336)
                    ) {
                        Text(
                            if (open) "Open" else "Closed",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    place.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = TripBlue.copy(alpha = 0.1f),
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Text(
                        place.category,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = TripBlue
                    )
                }
                if (place.description.isNotBlank()) {
                    Spacer(Modifier.height(4.dp))
                    Text(
                        place.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        maxLines = 3,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                        lineHeight = MaterialTheme.typography.bodySmall.lineHeight
                    )
                }
                Spacer(Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Default.NearMe, null, tint = TextHint, modifier = Modifier.size(12.dp))
                    Text(
                        if (place.distanceMeters >= 1000)
                            "${"%.1f".format(place.distanceMeters / 1000.0)} km"
                        else "${place.distanceMeters} m",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextHint
                    )
                }
            }
        }
    }
}
