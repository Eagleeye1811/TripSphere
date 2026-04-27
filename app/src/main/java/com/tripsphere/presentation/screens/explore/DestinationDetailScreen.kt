package com.tripsphere.presentation.screens.explore

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.tripsphere.data.local.Attraction
import com.tripsphere.data.local.DestinationAttractionsDataset
import com.tripsphere.domain.model.Destination
import com.tripsphere.domain.model.Place
import com.tripsphere.presentation.ui.theme.*
import com.tripsphere.presentation.viewmodel.DestinationPhotoViewModel
import com.tripsphere.presentation.viewmodel.NearbyPlacesState
import com.tripsphere.presentation.viewmodel.PhotosState
import com.tripsphere.presentation.viewmodel.PlacesViewModel
import com.tripsphere.presentation.viewmodel.WeatherUiState
import com.tripsphere.presentation.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationDetailScreen(
    destination: Destination,
    onPlanTrip: () -> Unit,
    onNavigateBack: () -> Unit,
    onViewHotels: () -> Unit = {},
    weatherViewModel: WeatherViewModel = hiltViewModel(),
    placesViewModel: PlacesViewModel = hiltViewModel(),
    photoViewModel: DestinationPhotoViewModel = hiltViewModel()
) {
    val weatherState by weatherViewModel.weatherState.collectAsState()
    val nearbyState  by placesViewModel.nearbyState.collectAsState()
    val photosState  by placesViewModel.photosState.collectAsState()
    val myPhotos     by photoViewModel.photos.collectAsState()

    val context = LocalContext.current
    var showPhotoSourceSheet by remember { mutableStateOf(false) }
    // Track the actual File so we can build a stable file:// URI after capture
    var pendingCameraFile by remember { mutableStateOf<java.io.File?>(null) }

    LaunchedEffect(destination.id) {
        weatherViewModel.loadWeather(destination.latitude, destination.longitude)
        if (destination.latitude != 0.0 || destination.longitude != 0.0) {
            placesViewModel.loadNearbyPlaces(destination.latitude, destination.longitude)
        }
        placesViewModel.loadDestinationPhotos(destination.name)
        photoViewModel.loadPhotos(destination.id)
    }

    // ── Camera launcher ───────────────────────────────────────────────────────
    // Pass a FileProvider URI to the camera so it can write to our private file.
    // After capture, call addPhotoFile(file) — Coil loads File directly via FileFetcher.
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            pendingCameraFile?.let { photoViewModel.addPhotoFile(destination.id, it) }
        }
        pendingCameraFile = null
    }

    // ── Gallery picker ────────────────────────────────────────────────────────
    // Copy the selected image to internal storage (addPhotoFromUri does this on IO thread).
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { photoViewModel.addPhotoFromUri(destination.id, it) }
    }

    // ── Camera permission launcher ───────────────────────────────────────────
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val file = photoViewModel.createImageFile(destination.id)
            val providerUri = FileProvider.getUriForFile(context, "com.tripsphere.provider", file)
            pendingCameraFile = file          // remember the File for adding after capture
            cameraLauncher.launch(providerUri) // camera writes into this content URI
        }
    }

    // ── Source picker bottom sheet ────────────────────────────────────────────
    if (showPhotoSourceSheet) {
        ModalBottomSheet(
            onDismissRequest = { showPhotoSourceSheet = false },
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 40.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Add Photo",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // Take with camera
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showPhotoSourceSheet = false
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = TripBlue.copy(alpha = 0.08f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(TripBlue, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CameraAlt, null, tint = Color.White, modifier = Modifier.size(24.dp))
                        }
                        Column {
                            Text("Take a Photo", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                            Text("Open camera", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                    }
                }

                // Choose from gallery
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showPhotoSourceSheet = false
                            galleryLauncher.launch("image/*")
                        },
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = TripAccent.copy(alpha = 0.08f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(TripAccent, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.PhotoLibrary, null, tint = Color.White, modifier = Modifier.size(24.dp))
                        }
                        Column {
                            Text("Choose from Gallery", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                            Text("Pick an existing photo", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                    }
                }
            }
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
            // Extra bottom room: fixed CTA (≈120dp) + bottom nav bar (≈80dp)
            contentPadding = PaddingValues(bottom = 200.dp)
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

                        // Budget estimate — only shown when real data exists
                        if (destination.budgetEstimate.isNotBlank()) {
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
                                            "Estimated Budget",
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
                        }

                        // ── Top Attractions (rich cards from dataset) ─────────
                        val curatedAttractions = remember(destination.name) {
                            DestinationAttractionsDataset.getAttractionsForDestination(destination.name)
                        }

                        if (curatedAttractions.isNotEmpty()) {
                            Spacer(Modifier.height(20.dp))
                            Text(
                                "Top Attractions",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "${curatedAttractions.size} hand-picked places",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                            Spacer(Modifier.height(12.dp))
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                contentPadding = PaddingValues(end = 4.dp)
                            ) {
                                items(curatedAttractions.size) { idx ->
                                    AttractionCard(curatedAttractions[idx])
                                }
                            }
                        } else if (destination.topAttractions.isNotEmpty()) {
                            // Fallback to plain list when no curated data
                            Spacer(Modifier.height(16.dp))
                            Text("Top Attractions", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            destination.topAttractions.forEach { name ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Place, null, tint = TripAccent, modifier = Modifier.size(20.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text(name, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                                }
                                Divider(color = TextHint.copy(alpha = 0.2f))
                            }
                        }

                        // ── Nearby Places (Wikipedia GeoSearch) ───────────────
                        Spacer(Modifier.height(24.dp))
                        NearbyPlacesSection(nearbyState = nearbyState)

                        // ── Photos (API + user's own merged) ──────────────────
                        PhotoGallerySection(
                            photosState = photosState,
                            userPhotos = myPhotos,
                            onAddPhotoClick = { showPhotoSourceSheet = true }
                        )

                        // ── My Photos (manage / delete) ────────────────────────
                        if (myPhotos.isNotEmpty()) {
                            Spacer(Modifier.height(24.dp))
                            MyPhotosSection(
                                photos = myPhotos,
                                onAddClick = { showPhotoSourceSheet = true },
                                onDeletePhoto = { photoViewModel.deletePhoto(destination.id, it) }
                            )
                        }
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
                onClick = {
                    val shareText = buildString {
                        appendLine("🌍 ${destination.name}, ${destination.country}")
                        if (destination.rating > 0) appendLine("⭐ Rating: ${destination.rating}/5")
                        if (destination.bestTimeToVisit.isNotBlank()) appendLine("📅 Best time to visit: ${destination.bestTimeToVisit}")
                        if (destination.budgetEstimate.isNotBlank()) appendLine("💰 Estimated budget: ${destination.budgetEstimate}")
                        if (destination.description.isNotBlank()) {
                            appendLine()
                            appendLine(destination.description)
                        }
                        if (destination.topAttractions.isNotEmpty()) {
                            appendLine()
                            appendLine("🏛️ Top Attractions:")
                            destination.topAttractions.take(5).forEach { appendLine("  • $it") }
                        }
                        appendLine()
                        append("Discovered via TripSphere ✈️")
                    }
                    val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out ${destination.name}!")
                        putExtra(android.content.Intent.EXTRA_TEXT, shareText)
                    }
                    context.startActivity(android.content.Intent.createChooser(intent, "Share Destination"))
                },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(12.dp))
            ) {
                Icon(Icons.Default.Share, null, tint = Color.White)
            }
        }

        // Bottom CTA — sits above the system nav bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.background)
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Hotels quick-access button
            OutlinedButton(
                onClick = onViewHotels,
                modifier = Modifier.fillMaxWidth().height(44.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, TripBlue)
            ) {
                Icon(Icons.Default.Hotel, null, tint = TripBlue, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("View Hotels Nearby", color = TripBlue, fontWeight = FontWeight.SemiBold)
            }
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

// ── My Photos Section ─────────────────────────────────────────────────────────

@Composable
private fun MyPhotosSection(
    photos: List<String>,       // absolute file paths
    onAddClick: () -> Unit,
    onDeletePhoto: (String) -> Unit
) {
    var photoToDelete by remember { mutableStateOf<String?>(null) }

    // Delete confirmation dialog
    photoToDelete?.let { path ->
        AlertDialog(
            onDismissRequest = { photoToDelete = null },
            title = { Text("Delete Photo?", fontWeight = FontWeight.Bold) },
            text = { Text("Remove this photo from your collection for this destination?", color = TextSecondary) },
            confirmButton = {
                Button(
                    onClick = { onDeletePhoto(path); photoToDelete = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) { Text("Delete", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { photoToDelete = null }) { Text("Cancel") }
            }
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("My Photos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(
                "${photos.size} photo${if (photos.size == 1) "" else "s"} · tap ✕ to remove",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }

    Spacer(Modifier.height(10.dp))

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(end = 4.dp)
    ) {
        items(photos.size) { idx ->
            val path = photos[idx]
            Box(modifier = Modifier.size(width = 110.dp, height = 110.dp)) {
                // Use File directly — Coil's FileFetcher reads internal storage reliably
                AsyncImage(
                    model = java.io.File(path),
                    contentDescription = "My photo ${idx + 1}",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(14.dp)),
                    contentScale = ContentScale.Crop
                )
                // Delete badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD32F2F))
                        .clickable { photoToDelete = path },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Close, null, tint = Color.White, modifier = Modifier.size(13.dp))
                }
            }
        }
    }
    Spacer(Modifier.height(8.dp))
}

// ── Photo Gallery (API + user photos merged) ──────────────────────────────────

@Composable
private fun PhotoGallerySection(
    photosState: PhotosState,
    userPhotos: List<String> = emptyList(),  // absolute file paths
    onAddPhotoClick: () -> Unit = {}
) {
    val apiUrls: List<String> = if (photosState is PhotosState.Success) photosState.urls else emptyList()
    val isLoading = photosState is PhotosState.Loading
    val hasContent = isLoading || userPhotos.isNotEmpty() || apiUrls.isNotEmpty()

    if (!hasContent) return

    Spacer(Modifier.height(16.dp))

    // Header row: title + "Add" chip
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Photos", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = TripBlue.copy(alpha = 0.10f),
            modifier = Modifier.clickable { onAddPhotoClick() }
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(Icons.Default.AddAPhoto, null, tint = TripBlue, modifier = Modifier.size(14.dp))
                Text("Add", style = MaterialTheme.typography.labelMedium, color = TripBlue, fontWeight = FontWeight.SemiBold)
            }
        }
    }

    Spacer(Modifier.height(10.dp))

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(end = 4.dp)
    ) {
        // User photos first — use File() directly so Coil's FileFetcher loads reliably
        items(userPhotos.size) { idx ->
            Box(modifier = Modifier.size(width = 130.dp, height = 110.dp)) {
                AsyncImage(
                    model = java.io.File(userPhotos[idx]),
                    contentDescription = "My photo",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                // "You" label badge
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(5.dp),
                    shape = RoundedCornerShape(6.dp),
                    color = TripBlue.copy(alpha = 0.85f)
                ) {
                    Text(
                        "You",
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Loading skeletons
        if (isLoading) {
            items(4) {
                Box(
                    modifier = Modifier
                        .size(width = 160.dp, height = 110.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray.copy(alpha = 0.4f))
                )
            }
        }

        // API photos
        items(apiUrls.size) { idx ->
            AsyncImage(
                model = apiUrls[idx],
                contentDescription = null,
                modifier = Modifier
                    .size(width = 160.dp, height = 110.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
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
                            color = MaterialTheme.colorScheme.onSurface
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

// ── Attraction Card (used in Detail Screen) ───────────────────────────────────

@Composable
private fun AttractionCard(attraction: Attraction) {
    Card(
        modifier = Modifier.width(200.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            // Category emoji banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(TripBlue.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Text(attraction.category.emoji, style = MaterialTheme.typography.displaySmall)
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    attraction.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(3.dp))
                Text(
                    attraction.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    maxLines = 3,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    lineHeight = MaterialTheme.typography.bodySmall.lineHeight
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(shape = RoundedCornerShape(6.dp), color = TripBlue.copy(alpha = 0.1f)) {
                        Text(
                            attraction.suggestedTime,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = TripBlue
                        )
                    }
                    Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFF57C00).copy(alpha = 0.1f)) {
                        Text(
                            "${if (attraction.durationHours % 1 == 0f) attraction.durationHours.toInt() else attraction.durationHours} hrs",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFF57C00)
                        )
                    }
                }
            }
        }
    }
}

// ── Nearby Places Section (Wikipedia GeoSearch) ──────────────────────────────

@Composable
private fun NearbyPlacesSection(nearbyState: NearbyPlacesState) {
    // Title + content are shown together — never render a header with empty body
    when (nearbyState) {
        is NearbyPlacesState.Loading -> {
            Text("Nearby Places", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Box(Modifier.fillMaxWidth().height(140.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = TripBlue)
            }
        }

        is NearbyPlacesState.Error -> {
            Text("Nearby Places", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
                )
            ) {
                Text(
                    "Could not load nearby places",
                    modifier = Modifier.padding(14.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        is NearbyPlacesState.Success -> {
            if (nearbyState.places.isNotEmpty()) {
                Text("Nearby Places", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(12.dp))

                // Category filter chips
                val categories = listOf("All") + nearbyState.places
                    .map { it.category }.distinct().take(5)
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
            // If places is empty we show nothing (no orphan header)
        }

        else -> Unit // Idle — show nothing
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
