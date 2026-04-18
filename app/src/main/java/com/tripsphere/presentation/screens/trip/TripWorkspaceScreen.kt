package com.tripsphere.presentation.screens.trip

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.tripsphere.domain.model.Itinerary
import com.tripsphere.presentation.ui.theme.*
import com.tripsphere.presentation.viewmodel.NearbyPlacesState
import com.tripsphere.presentation.viewmodel.PlacesViewModel
import com.tripsphere.presentation.viewmodel.TripWorkspaceViewModel
import com.tripsphere.utils.LocationHelper
import dagger.hilt.android.EntryPointAccessors
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripWorkspaceScreen(
    destination: String,
    startDate: String,
    endDate: String,
    budget: Double,
    imageUrl: String,
    onNavigateToOverview: (Long) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: TripWorkspaceViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Itinerary", "Expenses", "Notes", "Map")

    val parsedStart = remember { LocalDate.parse(startDate) }
    val parsedEnd = remember { LocalDate.parse(endDate) }
    val tripDays = ChronoUnit.DAYS.between(parsedStart, parsedEnd).toInt().coerceAtLeast(1)

    LaunchedEffect(uiState.savedTripId) {
        uiState.savedTripId?.let { id ->
            onNavigateToOverview(id)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header
        Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            AsyncImage(
                model = imageUrl.ifEmpty {
                    "https://images.unsplash.com/photo-1488646953014-85cb44e25828?w=800"
                },
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(listOf(Color(0x44000000), Color(0xCC000000))))
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                }
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
            ) {
                Text(destination, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
                Text("$startDate → $endDate · Budget: $${"%.0f".format(budget)}", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.8f))
            }
        }

        // Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = TripBlue,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                    color = TripBlue
                )
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            title,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (selectedTab == index) TripBlue else TextSecondary
                        )
                    }
                )
            }
        }

        // Tab content
        when (selectedTab) {
            0 -> ItineraryTab(
                tripDays = tripDays,
                itineraryByDay = uiState.itineraryByDay,
                onAddItem = { day, time, activity, notes ->
                    viewModel.addItinerary(day, time, activity, notes)
                },
                onRemoveItem = viewModel::removeItinerary,
                onMoveItem = { day, item, direction -> viewModel.moveItinerary(day, item, direction) }
            )
            1 -> ExpensePlanningTab()
            2 -> NotesTab(
                notes = uiState.notes,
                onNotesChange = viewModel::updateNotes
            )
            3 -> MapPreviewTab(destination = destination)
        }

        Spacer(modifier = Modifier.weight(1f))
    }

    // FAB to save
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        ExtendedFloatingActionButton(
            onClick = {
                viewModel.saveTrip(destination, parsedStart, parsedEnd, budget, imageUrl)
            },
            containerColor = TripBlue,
            icon = { Icon(Icons.Default.Save, null, tint = Color.White) },
            text = {
                if (uiState.isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Save Trip", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
private fun ItineraryTab(
    tripDays: Int,
    itineraryByDay: Map<Int, List<Itinerary>>,
    onAddItem: (Int, String, String, String) -> Unit,
    onRemoveItem: (Itinerary) -> Unit,
    onMoveItem: (Int, Itinerary, Int) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedDay by remember { mutableIntStateOf(1) }

    if (showAddDialog) {
        AddItineraryDialog(
            day = selectedDay,
            onDismiss = { showAddDialog = false },
            onAdd = { time, activity, notes ->
                onAddItem(selectedDay, time, activity, notes)
                showAddDialog = false
            }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(tripDays) { dayIndex ->
            val day = dayIndex + 1
            val items = itineraryByDay[day] ?: emptyList()

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Day $day",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TripBlue
                        )
                        IconButton(
                            onClick = {
                                selectedDay = day
                                showAddDialog = true
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Add, null, tint = TripBlue)
                        }
                    }

                    if (items.isEmpty()) {
                        Text(
                            "No activities yet. Tap + to add",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextHint,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        items.forEachIndexed { index, item ->
                            DraggableItineraryItem(
                                item = item,
                                canMoveUp = index > 0,
                                canMoveDown = index < items.size - 1,
                                onMoveUp = { onMoveItem(day, item, -1) },
                                onMoveDown = { onMoveItem(day, item, 1) },
                                onRemove = { onRemoveItem(item) }
                            )
                            if (index < items.size - 1) {
                                Divider(color = TextHint.copy(alpha = 0.15f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DraggableItineraryItem(
    item: Itinerary,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onRemove: () -> Unit
) {
    var dragOffsetY by remember { mutableStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(0, dragOffsetY.roundToInt()) }
            .zIndex(if (isDragging) 1f else 0f)
            .background(
                if (isDragging) TripBlue.copy(alpha = 0.06f)
                else Color.Transparent,
                RoundedCornerShape(8.dp)
            )
            .pointerInput(item.id) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { isDragging = true },
                    onDragEnd = {
                        when {
                            dragOffsetY < -40f && canMoveUp -> onMoveUp()
                            dragOffsetY > 40f && canMoveDown -> onMoveDown()
                        }
                        dragOffsetY = 0f
                        isDragging = false
                    },
                    onDragCancel = {
                        dragOffsetY = 0f
                        isDragging = false
                    },
                    onDrag = { _, dragAmount ->
                        dragOffsetY += dragAmount.y
                    }
                )
            }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Drag handle
        Icon(
            Icons.Default.DragHandle, null,
            tint = if (isDragging) TripBlue else TextHint.copy(alpha = 0.5f),
            modifier = Modifier.size(20.dp).padding(end = 4.dp)
        )

        Surface(
            shape = RoundedCornerShape(6.dp),
            color = TripBlue.copy(alpha = 0.12f),
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Text(
                item.time,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                color = TripBlue,
                fontWeight = FontWeight.Bold
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(item.activity, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
            if (item.notes.isNotEmpty()) {
                Text(item.notes, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
        }
        // Up/down arrows (visible when dragging)
        if (isDragging) {
            Column {
                if (canMoveUp) {
                    Icon(Icons.Default.KeyboardArrowUp, null, tint = TripBlue, modifier = Modifier.size(18.dp))
                }
                if (canMoveDown) {
                    Icon(Icons.Default.KeyboardArrowDown, null, tint = TripBlue, modifier = Modifier.size(18.dp))
                }
            }
        } else {
            IconButton(onClick = onRemove, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.Delete, null, tint = TextHint, modifier = Modifier.size(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddItineraryDialog(
    day: Int,
    onDismiss: () -> Unit,
    onAdd: (String, String, String) -> Unit
) {
    var time by remember { mutableStateOf("") }
    var activity by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Activity – Day $day", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Time (e.g. 09:00 AM)") },
                    leadingIcon = { Icon(Icons.Default.Schedule, null, tint = TripBlue) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = activity,
                    onValueChange = { activity = it },
                    label = { Text("Activity") },
                    leadingIcon = { Icon(Icons.Default.Event, null, tint = TripBlue) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (time.isNotBlank() && activity.isNotBlank()) onAdd(time, activity, notes) },
                colors = ButtonDefaults.buttonColors(containerColor = TripBlue)
            ) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextSecondary) }
        }
    )
}

@Composable
private fun ExpensePlanningTab() {
    val categories = listOf(
        "🏨 Stay" to "",
        "🍽️ Food" to "",
        "🚗 Transport" to "",
        "🎭 Activities" to ""
    )
    var values by remember { mutableStateOf(categories.map { it.second }) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Plan Expected Costs", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text("Enter estimated spending per category", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
        items(categories.size) { index ->
            OutlinedTextField(
                value = values[index],
                onValueChange = { newVal ->
                    values = values.toMutableList().also { it[index] = newVal }
                },
                label = { Text(categories[index].first) },
                leadingIcon = { Icon(Icons.Default.AttachMoney, null, tint = TripBlue) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal
                ),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TripBlue,
                    unfocusedBorderColor = TextHint.copy(alpha = 0.4f)
                )
            )
        }

        // Total
        item {
            val total = values.sumOf { it.toDoubleOrNull() ?: 0.0 }
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = TripBlue.copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Estimate", fontWeight = FontWeight.Bold, color = TripBlue)
                    Text("$${"%.2f".format(total)}", fontWeight = FontWeight.Bold, color = TripBlue, fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
private fun NotesTab(notes: String, onNotesChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Trip Notes", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = notes,
            onValueChange = onNotesChange,
            placeholder = { Text("Jot down anything — packing list, reminders, ideas...") },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TripBlue,
                unfocusedBorderColor = TextHint.copy(alpha = 0.4f)
            )
        )
    }
}

@Composable
private fun MapPreviewTab(
    destination: String,
    placesViewModel: PlacesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val nearbyState by placesViewModel.nearbyState.collectAsState()

    val locationHelper = remember {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            LocationHelperEntryPoint::class.java
        ).locationHelper()
    }

    var userLatLng by remember { mutableStateOf<com.google.android.gms.maps.model.LatLng?>(null) }
    val defaultLatLng = remember { com.google.android.gms.maps.model.LatLng(20.0, 78.9) }

    val cameraPositionState = com.google.maps.android.compose.rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(defaultLatLng, 13f)
    }

    LaunchedEffect(Unit) {
        val loc = locationHelper.getLastKnownLocation()
        if (loc != null) {
            val ll = com.google.android.gms.maps.model.LatLng(loc.latitude, loc.longitude)
            userLatLng = ll
            cameraPositionState.position =
                com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(ll, 14f)
            placesViewModel.loadNearbyPlaces(loc.latitude, loc.longitude)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // ── Google Map ────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            com.google.maps.android.compose.GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = com.google.maps.android.compose.MapProperties(isMyLocationEnabled = locationHelper.hasPermission()),
                uiSettings = com.google.maps.android.compose.MapUiSettings(zoomControlsEnabled = true)
            ) {
                // User location marker
                userLatLng?.let { ll ->
                    com.google.maps.android.compose.Marker(
                        state = com.google.maps.android.compose.MarkerState(position = ll),
                        title = "You are here"
                    )
                }

                // Nearby place markers
                if (nearbyState is NearbyPlacesState.Success) {
                    (nearbyState as NearbyPlacesState.Success).places.forEach { place ->
                        if (place.latitude != 0.0 || place.longitude != 0.0) {
                            com.google.maps.android.compose.Marker(
                                state = com.google.maps.android.compose.MarkerState(
                                    position = com.google.android.gms.maps.model.LatLng(
                                        place.latitude, place.longitude
                                    )
                                ),
                                title = place.name,
                                snippet = place.category
                            )
                        }
                    }
                }
            }

            // Loading overlay
            if (nearbyState is NearbyPlacesState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(24.dp),
                    strokeWidth = 2.dp,
                    color = TripBlue
                )
            }
        }

        // ── Bottom sheet: nearby places list ─────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Nearby Places",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                // Open in Maps deep-link
                userLatLng?.let { ll ->
                    TextButton(onClick = {
                        val uri = Uri.parse("geo:${ll.latitude},${ll.longitude}?q=${Uri.encode(destination)}")
                        val intent = Intent(Intent.ACTION_VIEW, uri).apply { setPackage("com.google.android.apps.maps") }
                        if (intent.resolveActivity(context.packageManager) != null) context.startActivity(intent)
                        else context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com/?q=${Uri.encode(destination)}")))
                    }) {
                        Text("Open Maps", color = TripBlue, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            when (val state = nearbyState) {
                is NearbyPlacesState.Loading -> {
                    Box(Modifier.fillMaxWidth().height(80.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = TripBlue)
                    }
                }
                is NearbyPlacesState.Error -> {
                    Text(
                        state.message,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                is NearbyPlacesState.Success -> {
                    if (state.places.isEmpty()) {
                        Text("No places found nearby.", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                    } else {
                        state.places.take(5).forEach { place ->
                            NearbyPlaceRow(place)
                        }
                    }
                }
                else -> Unit
            }
        }
    }
}

@Composable
private fun NearbyPlaceRow(place: com.tripsphere.domain.model.Place) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        AsyncImage(
            model = place.photoUrl,
            contentDescription = null,
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(TripBlue.copy(alpha = 0.1f)),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(place.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, maxLines = 1)
            Text(place.category, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            Text(
                "${place.distanceMeters}m away" + (if (place.address.isNotBlank()) " · ${place.address}" else ""),
                style = MaterialTheme.typography.labelSmall,
                color = TextHint,
                maxLines = 1
            )
        }
        place.rating?.let { r ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, null, tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
                Text("${"%.1f".format(r)}", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@dagger.hilt.EntryPoint
@dagger.hilt.InstallIn(dagger.hilt.components.SingletonComponent::class)
interface LocationHelperEntryPoint {
    fun locationHelper(): LocationHelper
}
