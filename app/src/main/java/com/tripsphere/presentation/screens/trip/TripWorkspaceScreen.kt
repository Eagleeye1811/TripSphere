package com.tripsphere.presentation.screens.trip

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import com.tripsphere.data.local.Attraction
import com.tripsphere.data.local.AttractionCategory
import com.tripsphere.data.local.DestinationAttractionsDataset
import com.tripsphere.data.local.HotelDataset
import com.tripsphere.data.local.HotelEntry
import com.google.android.gms.maps.model.MapStyleOptions
import com.tripsphere.domain.model.ExpenseCategory
import com.tripsphere.domain.model.Itinerary
import com.tripsphere.utils.DummyData
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
            viewModel.clearSavedTripId()   // reset so back-navigation doesn't re-trigger this
            onNavigateToOverview(id)
        }
    }

    // Distribute total budget across categories the first time the screen loads
    LaunchedEffect(budget) {
        viewModel.initializeBudget(budget)
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
                            color = if (selectedTab == index) TripBlue else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
            }
        }

        // ── Tab content (fills remaining space) ───────────────────────────────
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> ItineraryTab(
                    destination = destination,
                    tripDays = tripDays,
                    itineraryByDay = uiState.itineraryByDay,
                    onAddItem = { day, time, activity, notes ->
                        viewModel.addItinerary(day, time, activity, notes)
                    },
                    onRemoveItem = viewModel::removeItinerary,
                    onMoveItem = { day, item, direction -> viewModel.moveItinerary(day, item, direction) },
                    onUpdateItem = { item, time, activity, notes ->
                        viewModel.updateItinerary(item, time, activity, notes)
                    }
                )
                1 -> ExpensePlanningTab(
                    totalBudget = budget,
                    allocations = uiState.expenseAllocations,
                    onUpdateAllocation = viewModel::updateExpenseAllocation
                )
                2 -> NotesTab(
                    notes = uiState.notes,
                    onNotesChange = viewModel::updateNotes
                )
                3 -> MapPreviewTab(destination = destination, itineraryByDay = uiState.itineraryByDay)
            }
        }

        // ── Full-width Save button pinned at the very bottom ───────────────────
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 12.dp
        ) {
            Button(
                onClick = {
                    viewModel.saveTrip(destination, parsedStart, parsedEnd, budget, imageUrl)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TripBlue),
                enabled = !uiState.isSaving
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(10.dp))
                    Text("Saving…", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                } else {
                    Icon(Icons.Default.Save, null, tint = Color.White)
                    Spacer(Modifier.width(10.dp))
                    Text("Save Trip", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
private fun ItineraryTab(
    destination: String,
    tripDays: Int,
    itineraryByDay: Map<Int, List<Itinerary>>,
    onAddItem: (Int, String, String, String) -> Unit,
    onRemoveItem: (Itinerary) -> Unit,
    onMoveItem: (Int, Itinerary, Int) -> Unit,
    onUpdateItem: (Itinerary, String, String, String) -> Unit
) {
    var showSheet by remember { mutableStateOf(false) }
    var selectedDay by remember { mutableIntStateOf(1) }
    var editingItem by remember { mutableStateOf<Itinerary?>(null) }

    if (showSheet) {
        AddAttractionSheet(
            destination = destination,
            day = selectedDay,
            existingItems = itineraryByDay[selectedDay] ?: emptyList(),
            onDismiss = { showSheet = false },
            onAdd = { time, activity, notes ->
                onAddItem(selectedDay, time, activity, notes)
            }
        )
    }

    // Edit dialog
    editingItem?.let { item ->
        EditItineraryDialog(
            item = item,
            onDismiss = { editingItem = null },
            onSave = { time, activity, notes ->
                onUpdateItem(item, time, activity, notes)
                editingItem = null
            }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(tripDays) { dayIndex ->
            val day = dayIndex + 1
            val items = itineraryByDay[day] ?: emptyList()

            // Day colour accent
            val dayColor = dayAccentColor(day)

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(3.dp)
            ) {
                Column {
                    // ── Day header bar ────────────────────────────────────────
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(dayColor.copy(alpha = 0.1f), RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(shape = RoundedCornerShape(8.dp), color = dayColor) {
                                Text(
                                    "Day $day",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            if (items.isNotEmpty()) {
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "${items.size} place${if (items.size > 1) "s" else ""}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = dayColor
                                )
                            }
                        }
                        FilledTonalButton(
                            onClick = { selectedDay = day; showSheet = true },
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = dayColor.copy(alpha = 0.15f),
                                contentColor = dayColor
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Add Place", style = MaterialTheme.typography.labelMedium)
                        }
                    }

                    // ── Planned items ─────────────────────────────────────────
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                        if (items.isEmpty()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedDay = day; showSheet = true }
                                    .background(dayColor.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Default.AddLocationAlt, null, tint = dayColor.copy(alpha = 0.5f), modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Tap to add places for Day $day",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = dayColor.copy(alpha = 0.7f)
                                )
                            }
                        } else {
                            items.forEachIndexed { index, item ->
                                DraggableItineraryItem(
                                    item = item,
                                    dayColor = dayColor,
                                    canMoveUp = index > 0,
                                    canMoveDown = index < items.size - 1,
                                    onMoveUp = { onMoveItem(day, item, -1) },
                                    onMoveDown = { onMoveItem(day, item, 1) },
                                    onRemove = { onRemoveItem(item) },
                                    onEdit = { editingItem = item }
                                )
                                if (index < items.size - 1) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(start = 36.dp),
                                        color = TextHint.copy(alpha = 0.12f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ── Add Attraction Bottom Sheet ────────────────────────────────────────────────

private enum class AddSheetMode { PLACES, HOTELS, CUSTOM }

/** Maps a destination name to the hotel ID prefix stored in HotelDataset. */
private fun getHotelsForDestination(destination: String): List<HotelEntry> {
    val lower = destination.lowercase()
    val prefix = when {
        lower.contains("paris") -> "PAR"
        lower.contains("dubai") -> "DXB"
        lower.contains("tokyo") || lower.contains("mount fuji") || lower.contains("fuji") -> "TYO"
        lower.contains("new york") -> "NYC"
        lower.contains("london") -> "LON"
        lower.contains("singapore") -> "SIN"
        lower.contains("amalfi") || lower.contains("positano") || lower.contains("ravello") || lower.contains("dolomites") -> "AML"
        lower.contains("rome") -> "ROM"
        lower.contains("bangkok") -> "BKK"
        lower.contains("phuket") -> "PHK"
        lower.contains("koh samui") || lower.contains("samui") -> "KSM"
        lower.contains("barcelona") -> "BCN"
        lower.contains("bali") || lower.contains("ubud") || lower.contains("seminyak") -> "BAL"
        lower.contains("istanbul") -> "IST"
        lower.contains("maldives") -> "MDV"
        lower.contains("santorini") -> "SNT"
        lower.contains("kyoto") -> "KYO"
        lower.contains("machu picchu") -> "MPC"
        lower.contains("swiss alps") || lower.contains("zermatt") || lower.contains("interlaken") || lower.contains("st. moritz") -> "SWS"
        lower.contains("amsterdam") -> "AMS"
        lower.contains("prague") -> "PRG"
        lower.contains("banff") || lower.contains("rocky mountains") -> "BNF"
        lower.contains("queenstown") -> "QTN"
        lower.contains("iceland") || lower.contains("reykjavik") -> "ICL"
        lower.contains("tulum") -> "TLM"
        lower.contains("seychelles") -> "SEY"
        lower.contains("maui") -> "MAU"
        lower.contains("norway") || lower.contains("norwegian") || lower.contains("fjord") -> "NOR"
        lower.contains("costa rica") -> "CRC"
        lower.contains("nepal") || lower.contains("himalayas") || lower.contains("everest") || lower.contains("kathmandu") -> "NPL"
        lower.contains("patagonia") || lower.contains("torres del paine") -> "PTG"
        else -> null
    }
    return if (prefix != null) HotelDataset.all.filter { it.id.startsWith(prefix) }
    else emptyList()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddAttractionSheet(
    destination: String,
    day: Int,
    existingItems: List<Itinerary>,
    onDismiss: () -> Unit,
    onAdd: (String, String, String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var sheetMode by remember { mutableStateOf(AddSheetMode.PLACES) }

    // Places state
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<AttractionCategory?>(null) }

    // Hotels state
    var hotelSearch by remember { mutableStateOf("") }
    var selectedHotelType by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current

    val allAttractions = remember(destination) {
        DestinationAttractionsDataset.getAttractionsForDestination(destination)
    }
    val categories = remember(allAttractions) { allAttractions.map { it.category }.distinct() }
    val filteredAttractions = allAttractions.filter { a ->
        (selectedCategory == null || a.category == selectedCategory) &&
            (searchQuery.isBlank() || a.name.contains(searchQuery, ignoreCase = true) ||
                a.description.contains(searchQuery, ignoreCase = true))
    }

    val allHotels = remember(destination) { getHotelsForDestination(destination) }
    val hotelTypes = remember(allHotels) { allHotels.map { it.type }.distinct() }
    val filteredHotels = allHotels.filter { h ->
        (selectedHotelType == null || h.type.equals(selectedHotelType, ignoreCase = true)) &&
            (hotelSearch.isBlank() || h.name.contains(hotelSearch, ignoreCase = true) ||
                h.address.contains(hotelSearch, ignoreCase = true))
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.fillMaxHeight(0.92f)) {

            // ── Header ────────────────────────────────────────────────────────
            Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                Text(
                    "Add to Day $day",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    destination.substringBefore(","),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(12.dp))

                // ── Mode selector tabs ────────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        AddSheetMode.PLACES to "📍 Places",
                        AddSheetMode.HOTELS to "🏨 Hotels",
                        AddSheetMode.CUSTOM to "✏️ Custom"
                    ).forEach { (mode, label) ->
                        FilterChip(
                            selected = sheetMode == mode,
                            onClick = {
                                sheetMode = mode
                                searchQuery = ""
                                hotelSearch = ""
                                focusManager.clearFocus()
                            },
                            label = { Text(label, style = MaterialTheme.typography.labelMedium) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TripBlue,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            HorizontalDivider(color = TextHint.copy(alpha = 0.1f))

            // ── Tab content ───────────────────────────────────────────────────
            when (sheetMode) {

                // ── PLACES ────────────────────────────────────────────────────
                AddSheetMode.PLACES -> {
                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search places…", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)) },
                            leadingIcon = { Icon(Icons.Default.Search, null, tint = TripBlue) },
                            trailingIcon = {
                                if (searchQuery.isNotBlank()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Default.Close, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TripBlue,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )
                        Spacer(Modifier.height(10.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(end = 4.dp)
                        ) {
                            item {
                                FilterChip(
                                    selected = selectedCategory == null,
                                    onClick = { selectedCategory = null },
                                    label = { Text("All") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = TripBlue,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                            items(categories) { cat ->
                                FilterChip(
                                    selected = selectedCategory == cat,
                                    onClick = { selectedCategory = if (selectedCategory == cat) null else cat },
                                    label = { Text("${cat.emoji} ${cat.label}") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = TripBlue,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                    HorizontalDivider(color = TextHint.copy(alpha = 0.1f))
                    if (filteredAttractions.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.SearchOff, null, tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), modifier = Modifier.size(40.dp))
                                Spacer(Modifier.height(8.dp))
                                Text("No places match your search", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(filteredAttractions) { attraction ->
                                val alreadyAdded = existingItems.any {
                                    it.activity.equals(attraction.name, ignoreCase = true)
                                }
                                AttractionPickerCard(
                                    attraction = attraction,
                                    alreadyAdded = alreadyAdded,
                                    onAdd = {
                                        if (!alreadyAdded) {
                                            onAdd(attraction.suggestedTime, attraction.name, attraction.description)
                                            focusManager.clearFocus()
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                // ── HOTELS ────────────────────────────────────────────────────
                AddSheetMode.HOTELS -> {
                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)) {
                        OutlinedTextField(
                            value = hotelSearch,
                            onValueChange = { hotelSearch = it },
                            placeholder = { Text("Search hotels…", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)) },
                            leadingIcon = { Icon(Icons.Default.Search, null, tint = TripBlue) },
                            trailingIcon = {
                                if (hotelSearch.isNotBlank()) {
                                    IconButton(onClick = { hotelSearch = "" }) {
                                        Icon(Icons.Default.Close, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = TripBlue,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )
                        Spacer(Modifier.height(10.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(end = 4.dp)
                        ) {
                            item {
                                FilterChip(
                                    selected = selectedHotelType == null,
                                    onClick = { selectedHotelType = null },
                                    label = { Text("All") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = TripBlue,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                            items(hotelTypes) { type ->
                                val emoji = when (type.lowercase()) {
                                    "hostel" -> "🛏️"
                                    "guest house" -> "🏡"
                                    "motel" -> "🚗"
                                    else -> "🏨"
                                }
                                FilterChip(
                                    selected = selectedHotelType == type,
                                    onClick = { selectedHotelType = if (selectedHotelType == type) null else type },
                                    label = { Text("$emoji $type") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = TripBlue,
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }
                    HorizontalDivider(color = TextHint.copy(alpha = 0.1f))
                    if (allHotels.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(32.dp)
                            ) {
                                Text("🏨", style = MaterialTheme.typography.displaySmall)
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    "No hotels available for this destination yet",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                                Spacer(Modifier.height(4.dp))
                                Text(
                                    "Add a custom activity instead",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    } else if (filteredHotels.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.SearchOff, null, tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), modifier = Modifier.size(40.dp))
                                Spacer(Modifier.height(8.dp))
                                Text("No hotels match your search", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(filteredHotels) { hotel ->
                                val alreadyAdded = existingItems.any {
                                    it.activity.equals("🏨 ${hotel.name}", ignoreCase = true)
                                }
                                HotelPickerCard(
                                    hotel = hotel,
                                    alreadyAdded = alreadyAdded,
                                    onAdd = {
                                        if (!alreadyAdded) {
                                            val starsText = hotel.stars?.let { "$it★" } ?: hotel.type
                                            val notes = "$starsText · ${hotel.estimatedPriceRange} · ${hotel.address}"
                                            onAdd("Check-in", "🏨 ${hotel.name}", notes)
                                            focusManager.clearFocus()
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                // ── CUSTOM ────────────────────────────────────────────────────
                AddSheetMode.CUSTOM -> {
                    CustomActivityForm(
                        day = day,
                        onAdd = { time, activity, notes -> onAdd(time, activity, notes) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HotelPickerCard(
    hotel: HotelEntry,
    alreadyAdded: Boolean,
    onAdd: () -> Unit
) {
    val bgColor = if (alreadyAdded) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    else MaterialTheme.colorScheme.surface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !alreadyAdded, onClick = onAdd),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(if (alreadyAdded) 0.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Hotel thumbnail
            AsyncImage(
                model = hotel.imageUrl.ifEmpty {
                    "https://images.unsplash.com/photo-1566073771259-6a8506099945?w=400&q=80"
                },
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(TripBlue.copy(alpha = 0.08f)),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    hotel.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (alreadyAdded) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(3.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Stars or type badge
                    if (hotel.stars != null) {
                        Surface(shape = RoundedCornerShape(5.dp), color = Color(0xFFFFC107).copy(alpha = 0.15f)) {
                            Text(
                                "${"★".repeat(hotel.stars.coerceAtMost(5))} ${hotel.stars}★",
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFF57C00),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Surface(shape = RoundedCornerShape(5.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)) {
                            Text(
                                hotel.type,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    // Price pill
                    Surface(shape = RoundedCornerShape(5.dp), color = Color(0xFF2E7D32).copy(alpha = 0.1f)) {
                        Text(
                            hotel.estimatedPriceRange,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF2E7D32),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Spacer(Modifier.height(3.dp))
                Text(
                    hotel.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.width(8.dp))
            if (alreadyAdded) {
                Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFF388E3C).copy(alpha = 0.15f)) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Check, null, tint = Color(0xFF388E3C), modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(3.dp))
                        Text("Added", style = MaterialTheme.typography.labelSmall, color = Color(0xFF388E3C), fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Surface(shape = RoundedCornerShape(8.dp), color = TripBlue) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(3.dp))
                        Text("Add", style = MaterialTheme.typography.labelSmall, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun AttractionPickerCard(
    attraction: Attraction,
    alreadyAdded: Boolean,
    onAdd: () -> Unit
) {
    val bgColor = if (alreadyAdded) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    else MaterialTheme.colorScheme.surface
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !alreadyAdded, onClick = onAdd),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(if (alreadyAdded) 0.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Category emoji circle
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        if (alreadyAdded) TextHint.copy(alpha = 0.1f) else TripBlue.copy(alpha = 0.1f),
                        RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(attraction.category.emoji, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    attraction.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (alreadyAdded) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    attraction.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Time chip
                    Surface(shape = RoundedCornerShape(6.dp), color = TripBlue.copy(alpha = 0.1f)) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Schedule, null, tint = TripBlue, modifier = Modifier.size(10.dp))
                            Spacer(Modifier.width(3.dp))
                            Text(attraction.suggestedTime, style = MaterialTheme.typography.labelSmall, color = TripBlue)
                        }
                    }
                    // Duration chip
                    Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFF57C00).copy(alpha = 0.1f)) {
                        Text(
                            "${if (attraction.durationHours % 1 == 0f) attraction.durationHours.toInt() else attraction.durationHours} hrs",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFF57C00)
                        )
                    }
                    // Category chip
                    Surface(shape = RoundedCornerShape(6.dp), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)) {
                        Text(
                            attraction.category.label,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Spacer(Modifier.width(8.dp))
            if (alreadyAdded) {
                Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFF388E3C).copy(alpha = 0.15f)) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Check, null, tint = Color(0xFF388E3C), modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(3.dp))
                        Text("Added", style = MaterialTheme.typography.labelSmall, color = Color(0xFF388E3C), fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                Surface(shape = RoundedCornerShape(8.dp), color = TripBlue) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.width(3.dp))
                        Text("Add", style = MaterialTheme.typography.labelSmall, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomActivityForm(
    day: Int,
    onAdd: (String, String, String) -> Unit
) {
    var time by remember { mutableStateOf("") }
    var activity by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Custom Activity – Day $day", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        OutlinedTextField(
            value = time,
            onValueChange = { time = it },
            label = { Text("Time (e.g. 09:00 AM)") },
            leadingIcon = { Icon(Icons.Default.Schedule, null, tint = TripBlue) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TripBlue, unfocusedBorderColor = MaterialTheme.colorScheme.outline)
        )
        OutlinedTextField(
            value = activity,
            onValueChange = { activity = it },
            label = { Text("Place or Activity") },
            leadingIcon = { Icon(Icons.Default.Place, null, tint = TripBlue) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TripBlue, unfocusedBorderColor = MaterialTheme.colorScheme.outline)
        )
        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Notes (optional)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TripBlue, unfocusedBorderColor = MaterialTheme.colorScheme.outline)
        )
        Button(
            onClick = {
                if (time.isNotBlank() && activity.isNotBlank()) onAdd(time, activity, notes)
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(25.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TripBlue)
        ) {
            Icon(Icons.Default.Add, null, tint = Color.White)
            Spacer(Modifier.width(8.dp))
            Text("Add to Itinerary", fontWeight = FontWeight.Bold)
        }
    }
}

private fun dayAccentColor(day: Int): Color = when (day % 5) {
    1    -> Color(0xFF1565C0)
    2    -> Color(0xFFE65100)
    3    -> Color(0xFF2E7D32)
    4    -> Color(0xFF6A1B9A)
    else -> Color(0xFF00695C)
}

@Composable
private fun DraggableItineraryItem(
    item: Itinerary,
    dayColor: Color,
    canMoveUp: Boolean,
    canMoveDown: Boolean,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit,
    onRemove: () -> Unit,
    onEdit: () -> Unit
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
            tint = if (isDragging) dayColor else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
            modifier = Modifier.size(20.dp).padding(end = 4.dp)
        )

        Surface(
            shape = RoundedCornerShape(6.dp),
            color = dayColor.copy(alpha = 0.12f),
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Text(
                item.time,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                color = dayColor,
                fontWeight = FontWeight.Bold
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(item.activity, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
            if (item.notes.isNotEmpty()) {
                Text(item.notes, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
        // Up/down arrows (visible when dragging) or edit+delete buttons
        if (isDragging) {
            Column {
                if (canMoveUp) {
                    Icon(Icons.Default.KeyboardArrowUp, null, tint = dayColor, modifier = Modifier.size(18.dp))
                }
                if (canMoveDown) {
                    Icon(Icons.Default.KeyboardArrowDown, null, tint = dayColor, modifier = Modifier.size(18.dp))
                }
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onEdit, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Edit, null, tint = TripBlue.copy(alpha = 0.7f), modifier = Modifier.size(15.dp))
                }
                IconButton(onClick = onRemove, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f), modifier = Modifier.size(15.dp))
                }
            }
        }
    }
}

// ── Edit Itinerary Dialog ──────────────────────────────────────────────────────

@Composable
private fun EditItineraryDialog(
    item: Itinerary,
    onDismiss: () -> Unit,
    onSave: (time: String, activity: String, notes: String) -> Unit
) {
    var time by remember { mutableStateOf(item.time) }
    var activity by remember { mutableStateOf(item.activity) }
    var notes by remember { mutableStateOf(item.notes) }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.EditNote, null, tint = TripBlue, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(8.dp))
                Text("Edit Activity", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Time (e.g. 09:00 AM)") },
                    leadingIcon = { Icon(Icons.Default.Schedule, null, tint = TripBlue) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TripBlue,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
                OutlinedTextField(
                    value = activity,
                    onValueChange = { activity = it },
                    label = { Text("Place or Activity") },
                    leadingIcon = { Icon(Icons.Default.Place, null, tint = TripBlue) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TripBlue,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TripBlue,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (time.isNotBlank() && activity.isNotBlank()) {
                        onSave(time.trim(), activity.trim(), notes.trim())
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = TripBlue),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Save", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpensePlanningTab(
    totalBudget: Double,
    allocations: Map<ExpenseCategory, Double>,
    onUpdateAllocation: (ExpenseCategory, Double) -> Unit
) {
    val planCategories = listOf(
        ExpenseCategory.STAY,
        ExpenseCategory.FOOD,
        ExpenseCategory.TRANSPORT,
        ExpenseCategory.ACTIVITIES
    )

    // Local text-field state so typing is smooth; syncs out via onUpdateAllocation
    var fieldValues by remember(allocations) {
        mutableStateOf(
            planCategories.associateWith { cat ->
                val v = allocations[cat] ?: 0.0
                if (v == 0.0) "" else "%.2f".format(v)
            }
        )
    }

    val allocatedTotal = planCategories.sumOf { allocations[it] ?: 0.0 }
    val remaining = totalBudget - allocatedTotal
    val usedFraction = if (totalBudget > 0) (allocatedTotal / totalBudget).coerceIn(0.0, 1.0).toFloat() else 0f
    val barColor = when {
        usedFraction > 1f -> Color(0xFFE53935)
        usedFraction > 0.85f -> Color(0xFFF57C00)
        else -> TripBlue
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ── Header ────────────────────────────────────────────────────────
        item {
            Text(
                "Budget Planner",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(2.dp))
            Text(
                "Your total budget has been split across categories. Adjust as needed — changes are saved with the trip.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // ── Overview card ────────────────────────────────────────────────
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = TripBlue.copy(alpha = 0.08f)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Budget", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                "$${"%.2f".format(totalBudget)}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = TripBlue
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Remaining", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(
                                "$${"%.2f".format(remaining)}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = if (remaining < 0) Color(0xFFE53935) else Color(0xFF2E7D32)
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { usedFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(50)),
                        color = barColor,
                        trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "$${"%.2f".format(allocatedTotal)} allocated of $${"%.2f".format(totalBudget)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // ── Per-category fields ──────────────────────────────────────────
        items(planCategories) { cat ->
            val catFraction = if (totalBudget > 0) ((allocations[cat] ?: 0.0) / totalBudget).toFloat() else 0f
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(cat.emoji, fontSize = 20.sp)
                            Spacer(Modifier.width(8.dp))
                            Text(cat.label, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Text(
                            "${"%.0f".format(catFraction * 100)}%",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = fieldValues[cat] ?: "",
                        onValueChange = { raw ->
                            fieldValues = fieldValues.toMutableMap().also { it[cat] = raw }
                            val parsed = raw.toDoubleOrNull() ?: 0.0
                            onUpdateAllocation(cat, parsed)
                        },
                        label = { Text("Amount (USD)") },
                        leadingIcon = { Icon(Icons.Default.AttachMoney, null, tint = TripBlue) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Decimal
                        ),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = TripBlue,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                    Spacer(Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { catFraction.coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(50)),
                        color = TripBlue,
                        trackColor = TripBlue.copy(alpha = 0.12f)
                    )
                }
            }
        }

        // ── Save reminder ────────────────────────────────────────────────
        item {
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Info, null, tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f), modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(6.dp))
                Text(
                    "Tap \"Save Trip\" to persist your budget breakdown.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
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
        Text(
            "Trip Notes",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = notes,
            onValueChange = onNotesChange,
            placeholder = {
                Text(
                    "Jot down anything — packing list, reminders, ideas...",
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TripBlue,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = TripBlue
            )
        )
    }
}

// Dark map style JSON for Google Maps
private val DARK_MAP_STYLE_JSON = """
[{"elementType":"geometry","stylers":[{"color":"#212121"}]},
{"elementType":"labels.icon","stylers":[{"visibility":"off"}]},
{"elementType":"labels.text.fill","stylers":[{"color":"#757575"}]},
{"elementType":"labels.text.stroke","stylers":[{"color":"#212121"}]},
{"featureType":"administrative","elementType":"geometry","stylers":[{"color":"#757575"}]},
{"featureType":"administrative.country","elementType":"labels.text.fill","stylers":[{"color":"#9e9e9e"}]},
{"featureType":"administrative.locality","elementType":"labels.text.fill","stylers":[{"color":"#bdbdbd"}]},
{"featureType":"poi","elementType":"labels.text.fill","stylers":[{"color":"#757575"}]},
{"featureType":"poi.park","elementType":"geometry","stylers":[{"color":"#181818"}]},
{"featureType":"poi.park","elementType":"labels.text.fill","stylers":[{"color":"#616161"}]},
{"featureType":"poi.park","elementType":"labels.text.stroke","stylers":[{"color":"#1b1b1b"}]},
{"featureType":"road","elementType":"geometry.fill","stylers":[{"color":"#2c2c2c"}]},
{"featureType":"road","elementType":"labels.text.fill","stylers":[{"color":"#8a8a8a"}]},
{"featureType":"road.arterial","elementType":"geometry","stylers":[{"color":"#373737"}]},
{"featureType":"road.highway","elementType":"geometry","stylers":[{"color":"#3c3c3c"}]},
{"featureType":"road.highway.controlled_access","elementType":"geometry","stylers":[{"color":"#4e4e4e"}]},
{"featureType":"road.local","elementType":"labels.text.fill","stylers":[{"color":"#616161"}]},
{"featureType":"transit","elementType":"labels.text.fill","stylers":[{"color":"#757575"}]},
{"featureType":"water","elementType":"geometry","stylers":[{"color":"#000000"}]},
{"featureType":"water","elementType":"labels.text.fill","stylers":[{"color":"#3d3d3d"}]}]
""".trimIndent()

// Day hue values for Google Maps colored markers
private val dayMarkerHues = listOf(
    com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_AZURE,   // Day 1 – Blue
    com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_ORANGE,  // Day 2 – Orange
    com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_GREEN,   // Day 3 – Green
    com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_VIOLET,  // Day 4 – Purple
    com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_YELLOW   // Day 5 – Yellow
)

private val MapDarkBg = Color(0xFF1A1A2E)
private val MapPanelBg = Color(0xFF16213E)
private val MapDivider  = Color(0xFF2A2A45)

@Composable
private fun MapPreviewTab(
    destination: String,
    itineraryByDay: Map<Int, List<Itinerary>>,
    placesViewModel: PlacesViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    // Resolve destination centre from DummyData
    val destLatLng = remember(destination) {
        val match = DummyData.destinations.find {
            destination.contains(it.name, ignoreCase = true) ||
                it.name.contains(destination.substringBefore(",").trim(), ignoreCase = true)
        }
        if (match != null && (match.latitude != 0.0 || match.longitude != 0.0))
            com.google.android.gms.maps.model.LatLng(match.latitude, match.longitude)
        else null
    }

    val locationHelper = remember {
        EntryPointAccessors.fromApplication(
            context.applicationContext,
            LocationHelperEntryPoint::class.java
        ).locationHelper()
    }

    val cameraPositionState = com.google.maps.android.compose.rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(
            destLatLng ?: com.google.android.gms.maps.model.LatLng(20.0, 0.0), 12f
        )
    }

    // Resolve position for every itinerary item:
    //   1. Attraction dataset (has precise lat/lon)
    //   2. Hotel dataset (strip 🏨 prefix)
    //   3. Fallback: destination centre
    data class MapPin(val day: Int, val item: Itinerary, val pos: com.google.android.gms.maps.model.LatLng, val isApproximate: Boolean)

    val pins: List<MapPin> = remember(destination, itineraryByDay, destLatLng) {
        itineraryByDay.flatMap { (day, items) ->
            items.mapNotNull { item ->
                val attraction = DestinationAttractionsDataset.findAttractionByName(destination, item.activity)
                val pos = when {
                    attraction != null ->
                        com.google.android.gms.maps.model.LatLng(attraction.lat, attraction.lon)
                    item.activity.startsWith("🏨 ") -> {
                        val hotelName = item.activity.removePrefix("🏨 ")
                        val hotel = HotelDataset.all.find { it.name.equals(hotelName, ignoreCase = true) }
                        hotel?.let { com.google.android.gms.maps.model.LatLng(it.latitude, it.longitude) }
                            ?: destLatLng
                    }
                    else -> destLatLng
                }
                pos?.let { MapPin(day, item, it, attraction == null) }
            }
        }
    }

    // Re-center the camera on the destination whenever this tab is composed
    LaunchedEffect(destLatLng) {
        if (destLatLng != null) {
            cameraPositionState.animate(
                com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(destLatLng, 12f)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // ── Google Map (light mode) ────────────────────────────────────────────
        Box(modifier = Modifier.fillMaxWidth().weight(0.58f)) {
            com.google.maps.android.compose.GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = com.google.maps.android.compose.MapProperties(
                    isMyLocationEnabled = locationHelper.hasPermission(),
                    mapStyleOptions = null   // light mode
                ),
                uiSettings = com.google.maps.android.compose.MapUiSettings(zoomControlsEnabled = true)
            ) {
                // Destination overview marker when no pins
                if (pins.isEmpty() && destLatLng != null) {
                    com.google.maps.android.compose.Marker(
                        state = com.google.maps.android.compose.MarkerState(position = destLatLng),
                        title = destination.substringBefore(","),
                        snippet = "Your trip destination"
                    )
                }

                // All itinerary items — colored by day
                pins.forEach { pin ->
                    val hue = dayMarkerHues[(pin.day - 1) % dayMarkerHues.size]
                    com.google.maps.android.compose.Marker(
                        state = com.google.maps.android.compose.MarkerState(position = pin.pos),
                        title = pin.item.activity,
                        snippet = "Day ${pin.day} · ${pin.item.time}",
                        icon = com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker(hue)
                    )
                }
            }

            // ── Day legend overlay (dark card) ─────────────────────────────────
            if (itineraryByDay.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MapPanelBg.copy(alpha = 0.95f)),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                        itineraryByDay.keys.sorted().forEach { day ->
                            val count = itineraryByDay[day]?.size ?: 0
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 3.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(dayAccentColor(day), androidx.compose.foundation.shape.CircleShape)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Day $day · $count place${if (count != 1) "s" else ""}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }

        // ── Bottom panel: dark themed itinerary summary ────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.42f)
                .background(MapPanelBg)
        ) {
            if (itineraryByDay.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Map,
                            null,
                            tint = Color.White.copy(alpha = 0.3f),
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(Modifier.height(10.dp))
                        Text(
                            "Add places in the Itinerary tab to see them on the map",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.5f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 14.dp, end = 16.dp, bottom = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Map,
                        null,
                        tint = TripBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Your Trip at a Glance",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                HorizontalDivider(color = MapDivider)
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    items(itineraryByDay.keys.sorted()) { day ->
                        val dayItems = itineraryByDay[day] ?: emptyList()
                        val dayColor = dayAccentColor(day)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Day badge
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(dayColor.copy(alpha = 0.25f), RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "$day",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = dayColor,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Day $day",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                                Text(
                                    dayItems.take(2).joinToString(" · ") {
                                        it.activity.removePrefix("🏨 ")
                                    } + if (dayItems.size > 2) " +${dayItems.size - 2} more" else "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.55f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            if (dayItems.isNotEmpty()) {
                                val routeUrl = buildMixedRouteUrl(dayItems, destination, destLatLng)
                                TextButton(
                                    onClick = {
                                        context.startActivity(
                                            Intent(Intent.ACTION_VIEW, Uri.parse(routeUrl))
                                        )
                                    },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Navigation,
                                        null,
                                        tint = TripBlue,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        "Route",
                                        color = TripBlue,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                        if (day != itineraryByDay.keys.max()) {
                            HorizontalDivider(color = MapDivider)
                        }
                    }
                }
            }
        }
    }
}

/**
 * Build a Google Maps Directions URL that works for every itinerary item.
 *
 * Items resolved from [DestinationAttractionsDataset] use precise lat/lon.
 * Custom activities fall back to a "Name, Destination" text search so that
 * Google Maps still finds a reasonable location.
 */
private fun buildMixedRouteUrl(
    items: List<Itinerary>,
    destination: String,
    fallbackLatLng: com.google.android.gms.maps.model.LatLng?
): String {
    if (items.isEmpty()) return "https://maps.google.com"

    fun pointFor(item: Itinerary): String {
        val attraction = DestinationAttractionsDataset.findAttractionByName(destination, item.activity)
        return if (attraction != null) {
            "${attraction.lat},${attraction.lon}"
        } else {
            Uri.encode("${item.activity}, $destination")
        }
    }

    // If only one item, open it directly in Google Maps search
    if (items.size == 1) {
        val attraction = DestinationAttractionsDataset.findAttractionByName(destination, items[0].activity)
        return if (attraction != null) {
            "https://www.google.com/maps/search/?api=1&query=${attraction.lat},${attraction.lon}"
        } else {
            "https://www.google.com/maps/search/?api=1&query=${Uri.encode("${items[0].activity}, $destination")}"
        }
    }

    val origin = pointFor(items.first())
    val dest = pointFor(items.last())
    val waypoints = if (items.size > 2) {
        items.subList(1, items.size - 1).joinToString("|") { pointFor(it) }
    } else ""

    return buildString {
        append("https://www.google.com/maps/dir/?api=1")
        append("&origin=$origin")
        append("&destination=$dest")
        if (waypoints.isNotEmpty()) append("&waypoints=$waypoints")
        append("&travelmode=walking")
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
            Text(place.category, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(
                "${place.distanceMeters}m away" + (if (place.address.isNotBlank()) " · ${place.address}" else ""),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
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
