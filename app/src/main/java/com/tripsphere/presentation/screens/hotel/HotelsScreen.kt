package com.tripsphere.presentation.screens.hotel

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.tripsphere.domain.model.Hotel
import com.tripsphere.presentation.ui.theme.*
import com.tripsphere.presentation.viewmodel.HotelViewModel

private val typeFilters = listOf("All", "Hotel", "Hostel", "Motel", "Guest House")
private val sortOptions  = listOf("Distance", "Stars", "Price", "Name")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelsScreen(
    destinationName: String,
    latitude: Double,
    longitude: Double,
    onNavigateBack: () -> Unit,
    viewModel: HotelViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(destinationName) {
        viewModel.loadHotels(latitude, longitude)
    }
    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) isRefreshing = false
    }

    Scaffold(
        topBar = {
            Column {
                // ── Gradient hero header ──────────────────────────────────────
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(listOf(TripBlue, Color(0xFF7C3AED))),
                            shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
                        )
                        .padding(horizontal = 20.dp)
                        .padding(top = 52.dp, bottom = 20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier
                                .size(38.dp)
                                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                        ) {
                            Icon(Icons.Default.ArrowBack, "Back", tint = Color.White)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Hotels in $destinationName",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            val count = uiState.filtered.size
                            Text(
                                if (uiState.isLoading) "Searching…"
                                else if (count == 0) "No results found"
                                else "$count propert${if (count != 1) "ies" else "y"} found",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                        IconButton(
                            onClick = viewModel::refresh,
                            modifier = Modifier
                                .size(38.dp)
                                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                        ) {
                            Icon(Icons.Default.Refresh, "Refresh", tint = Color.White)
                        }
                    }
                }

                // ── Filter chips ──────────────────────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    typeFilters.forEach { type ->
                        FilterChip(
                            selected = uiState.typeFilter == type,
                            onClick  = { viewModel.setTypeFilter(type) },
                            label    = { Text(type, style = MaterialTheme.typography.labelMedium) },
                            leadingIcon = if (uiState.typeFilter == type) {
                                { Icon(Icons.Default.Check, null, modifier = Modifier.size(14.dp)) }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor   = TripBlue,
                                selectedLabelColor       = Color.White,
                                selectedLeadingIconColor = Color.White
                            )
                        )
                    }
                }

                // ── Sort row ──────────────────────────────────────────────────
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var sortExpanded by remember { mutableStateOf(false) }
                    Box {
                        OutlinedButton(
                            onClick = { sortExpanded = true },
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, TripBlue.copy(alpha = 0.4f))
                        ) {
                            Icon(Icons.Default.Sort, null, modifier = Modifier.size(15.dp), tint = TripBlue)
                            Spacer(Modifier.width(4.dp))
                            Text("Sort: ${uiState.sortBy}", style = MaterialTheme.typography.labelMedium, color = TripBlue)
                            Icon(Icons.Default.ArrowDropDown, null, tint = TripBlue, modifier = Modifier.size(16.dp))
                        }
                        DropdownMenu(expanded = sortExpanded, onDismissRequest = { sortExpanded = false }) {
                            sortOptions.forEach { opt ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            if (uiState.sortBy == opt)
                                                Icon(Icons.Default.Check, null, tint = TripBlue, modifier = Modifier.size(14.dp))
                                            else
                                                Spacer(Modifier.size(14.dp))
                                            Text(opt)
                                        }
                                    },
                                    onClick = { viewModel.setSortBy(opt); sortExpanded = false }
                                )
                            }
                        }
                    }
                }

                HorizontalDivider(color = TextHint.copy(alpha = 0.12f))
            }
        }
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh    = { isRefreshing = true; viewModel.refresh() },
            modifier     = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Background)
        ) {
            when {
                uiState.isLoading && uiState.hotels.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator(color = TripBlue, strokeWidth = 3.dp)
                        Text("Finding best hotels…", color = TextSecondary, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                uiState.error != null && uiState.hotels.isEmpty() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center).padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Default.HotelClass, null, tint = TextHint, modifier = Modifier.size(64.dp))
                        Text(uiState.error!!, style = MaterialTheme.typography.bodyMedium, color = TextSecondary, textAlign = TextAlign.Center)
                        Button(onClick = viewModel::refresh, colors = ButtonDefaults.buttonColors(containerColor = TripBlue)) {
                            Icon(Icons.Default.Refresh, null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Try Again")
                        }
                    }
                }

                uiState.filtered.isEmpty() && !uiState.isLoading -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.SearchOff, null, tint = TextHint, modifier = Modifier.size(52.dp))
                        Text("No hotels match this filter", color = TextSecondary)
                        TextButton(onClick = { viewModel.setTypeFilter("All") }) { Text("Clear filter", color = TripBlue) }
                    }
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        itemsIndexed(uiState.filtered, key = { _, h -> h.id }) { index, hotel ->
                            var visible by remember { mutableStateOf(false) }
                            LaunchedEffect(Unit) { visible = true }
                            AnimatedVisibility(
                                visible = visible,
                                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 4 })
                            ) {
                                HotelCard(hotel = hotel)
                            }
                        }
                        item { Spacer(Modifier.height(16.dp)) }
                    }
                }
            }
        }
    }
}

// ── Hotel Card ─────────────────────────────────────────────────────────────────

@Composable
private fun HotelCard(hotel: Hotel) {
    val (accentBg, accentTint) = hotelTypeColors(hotel.type)

    Card(
        modifier  = Modifier.fillMaxWidth(),
        shape     = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp, pressedElevation = 8.dp),
        colors    = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            // ── Hero image ────────────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                if (hotel.imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = hotel.imageUrl,
                        contentDescription = hotel.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.linearGradient(listOf(accentBg, accentBg.copy(alpha = 0.5f)))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(hotelTypeIcon(hotel.type), null, tint = accentTint, modifier = Modifier.size(56.dp))
                    }
                }

                // Gradient overlay at bottom of image
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                0.0f to Color.Transparent,
                                0.6f to Color.Transparent,
                                1.0f to Color.Black.copy(alpha = 0.65f)
                            )
                        )
                )

                // Distance badge — top-right
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = Color.Black.copy(alpha = 0.55f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(Icons.Default.NearMe, null, tint = Color.White, modifier = Modifier.size(11.dp))
                        Text(
                            formatDistance(hotel.distanceMeters),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Type badge — top-left
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = accentTint.copy(alpha = 0.92f)
                ) {
                    Text(
                        hotel.type,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Hotel name overlay at bottom-left of image
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 14.dp, end = 14.dp, bottom = 12.dp)
                ) {
                    Text(
                        hotel.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    if ((hotel.stars ?: 0) > 0) {
                        Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            repeat(hotel.stars!!.coerceAtMost(5)) {
                                Icon(Icons.Default.Star, null, tint = Color(0xFFFFD600), modifier = Modifier.size(13.dp))
                            }
                            repeat((5 - hotel.stars).coerceAtLeast(0)) {
                                Icon(Icons.Default.StarBorder, null, tint = Color.White.copy(alpha = 0.5f), modifier = Modifier.size(13.dp))
                            }
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                "Unrated",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // ── Details section ───────────────────────────────────────────────
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {

                // Address row
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.LocationOn, null, tint = Color(0xFFEF4444), modifier = Modifier.size(15.dp).padding(top = 1.dp))
                    Text(
                        hotel.address,
                        style    = MaterialTheme.typography.bodySmall,
                        color    = TextSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(12.dp))

                // Price + contact row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Price pill
                    if (hotel.estimatedPriceRange.isNotBlank()) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = TripBlue.copy(alpha = 0.1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.CurrencyRupee, null, tint = TripBlue, modifier = Modifier.size(13.dp))
                                Text(
                                    hotel.estimatedPriceRange,
                                    style      = MaterialTheme.typography.labelSmall,
                                    color      = TripBlue,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Contact icons
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        hotel.phone?.let {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFE8F5E9)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Icon(Icons.Default.Phone, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(13.dp))
                                    Text("Call", style = MaterialTheme.typography.labelSmall, color = Color(0xFF2E7D32), fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                        hotel.website?.let {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = TripBlue.copy(alpha = 0.08f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Icon(Icons.Default.Language, null, tint = TripBlue, modifier = Modifier.size(13.dp))
                                    Text("Website", style = MaterialTheme.typography.labelSmall, color = TripBlue, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── Helpers ────────────────────────────────────────────────────────────────────

private fun formatDistance(meters: Int): String =
    if (meters >= 1000) "${"%.1f".format(meters / 1000.0)} km" else "$meters m"

private fun hotelTypeIcon(type: String) = when (type.lowercase()) {
    "hostel"      -> Icons.Default.Bed
    "motel"       -> Icons.Default.DirectionsCar
    "guest house" -> Icons.Default.Home
    else          -> Icons.Default.Hotel
}

private fun hotelTypeColors(type: String): Pair<Color, Color> = when (type.lowercase()) {
    "hostel"      -> Color(0xFFF3E5F5) to Color(0xFF7B1FA2)
    "motel"       -> Color(0xFFFFF3E0) to Color(0xFFE65100)
    "guest house" -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
    else          -> Color(0xFFE3F2FD) to Color(0xFF1565C0)
}
