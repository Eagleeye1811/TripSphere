package com.tripsphere.presentation.screens.explore

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.tripsphere.domain.model.Destination
import com.tripsphere.domain.model.DestinationCategory
import com.tripsphere.presentation.ui.theme.*
import com.tripsphere.presentation.viewmodel.ExploreViewModel
import com.tripsphere.utils.ShakeDetector
import kotlinx.coroutines.delay

// ─── Category model ───────────────────────────────────────────────────────────

private data class CategoryTab(
    val category: DestinationCategory,
    val label: String
)

private val categoryTabs = listOf(
    CategoryTab(DestinationCategory.ALL,       "🌍 All"),
    CategoryTab(DestinationCategory.BEACH,     "🏖️ Beach"),
    CategoryTab(DestinationCategory.MOUNTAIN,  "⛰️ Mountain"),
    CategoryTab(DestinationCategory.CITY,      "🏙️ City"),
    CategoryTab(DestinationCategory.ADVENTURE, "🧗 Adventure")
)

// ─── Main Screen ─────────────────────────────────────────────────────────────

@Composable
fun ExploreScreen(
    onDestinationClick: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: ExploreViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    var showShakeHint by remember { mutableStateOf(false) }

    // Shake to shuffle destinations
    val shakeDetector = remember { ShakeDetector(context) }
    DisposableEffect(Unit) {
        shakeDetector.register()
        onDispose { shakeDetector.unregister() }
    }
    LaunchedEffect(Unit) {
        shakeDetector.shakeFlow.collect {
            viewModel.shuffleDestinations()
            showShakeHint = true
            delay(2000)
            showShakeHint = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            // ── Immersive Header ─────────────────────────────────────────
            ExploreHeader(
                searchQuery = uiState.searchQuery,
                onSearchChange = viewModel::onSearchQueryChange
            )

            // ── Category Tabs ────────────────────────────────────────────
            Spacer(Modifier.height(4.dp))
            CategoryTabsRow(
                selected = uiState.selectedCategory,
                onSelect = { viewModel.onCategorySelected(it) }
            )

            // ── Results count ────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = TripBlue
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Fetching live places…",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                } else {
                    Text(
                        text = "${uiState.filteredDestinations.size} tourist places",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(Modifier.weight(1f))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = TripBlue.copy(alpha = 0.10f),
                    modifier = Modifier.clickable { viewModel.refresh() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Refresh, null, tint = TripBlue, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text(
                            "Refresh",
                            style = MaterialTheme.typography.labelMedium,
                            color = TripBlue,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // ── Offline / error banner ───────────────────────────────────
            uiState.error?.let {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF3D2010)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.WifiOff, null, tint = Color(0xFFFF8A50), modifier = Modifier.size(16.dp))
                        Text(
                            "Showing offline data — tap Refresh to retry",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFFF8A50)
                        )
                    }
                }
            }

            // ── Staggered Destinations Grid ──────────────────────────────
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalItemSpacing = 12.dp,
                modifier = Modifier.fillMaxSize()
            ) {
                item(span = StaggeredGridItemSpan.FullLine) { Spacer(Modifier.height(0.dp)) }

                if (uiState.isLoading && uiState.filteredDestinations.isEmpty()) {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = TripBlue)
                                Spacer(Modifier.height(16.dp))
                                Text("Loading tourist places…", color = TextSecondary)
                            }
                        }
                    }
                } else {
                    val destinations = uiState.filteredDestinations
                    items(destinations, key = { it.id }) { destination ->
                        val isLarge = destinations.indexOf(destination) % 3 == 0
                        ExploreDestinationCard(
                            destination = destination,
                            isLarge = isLarge,
                            onClick = { onDestinationClick(destination.id) }
                        )
                    }
                }

                item(span = StaggeredGridItemSpan.FullLine) { Spacer(Modifier.height(20.dp)) }
            }
        }

        // ── Shake hint toast ─────────────────────────────────────────────
        AnimatedVisibility(
            visible = showShakeHint,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 72.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.Black.copy(alpha = 0.75f)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Shuffle, null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Text(
                        "Destinations shuffled!",
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// ─── Immersive Header with Search ─────────────────────────────────────────────

@Composable
private fun ExploreHeader(
    searchQuery: String,
    onSearchChange: (String) -> Unit
) {
    val bgColor = MaterialTheme.colorScheme.background
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
    ) {
        AsyncImage(
            model = "https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?w=800",
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.0f to Color.Black.copy(alpha = 0.55f),
                        0.5f to Color.Black.copy(alpha = 0.25f),
                        0.85f to bgColor.copy(alpha = 0.85f),
                        1.0f to bgColor
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 56.dp, start = 20.dp, end = 20.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Discover",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        lineHeight = 40.sp
                    )
                    Text(
                        text = "Your next destination awaits",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.25f))
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Map, null, tint = Color.White, modifier = Modifier.size(24.dp))
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                color = Color.White.copy(alpha = 0.25f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.4f))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
                    Spacer(Modifier.width(12.dp))
                    androidx.compose.foundation.text.BasicTextField(
                        value = searchQuery,
                        onValueChange = onSearchChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                        cursorBrush = androidx.compose.ui.graphics.SolidColor(Color.White),
                        decorationBox = { innerTextField ->
                            Box(contentAlignment = Alignment.CenterStart) {
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        "Search destinations...",
                                        color = Color.White.copy(alpha = 0.8f),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }, modifier = Modifier.size(24.dp)) {
                            Icon(Icons.Default.Close, null, tint = Color.White.copy(alpha = 0.8f))
                        }
                    }
                }
            }
        }
    }
}

// ─── Category Tabs ────────────────────────────────────────────────────────────

@Composable
private fun CategoryTabsRow(
    selected: DestinationCategory,
    onSelect: (DestinationCategory) -> Unit
) {
    val unselectedBg   = MaterialTheme.colorScheme.surfaceVariant
    val unselectedText = MaterialTheme.colorScheme.onSurfaceVariant
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(categoryTabs) { tab ->
            val isSelected = tab.category == selected
            val bgColor by animateColorAsState(
                targetValue = if (isSelected) TripBlue else unselectedBg,
                animationSpec = tween(250), label = "tab_bg"
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) Color.White else unselectedText,
                animationSpec = tween(250), label = "tab_text"
            )
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(bgColor)
                    .clickable { onSelect(tab.category) }
                    .padding(horizontal = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = tab.label,
                    style = MaterialTheme.typography.labelLarge,
                    color = textColor,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}

// ─── Destination Card ─────────────────────────────────────────────────────────

@Composable
private fun ExploreDestinationCard(
    destination: Destination,
    isLarge: Boolean,
    onClick: () -> Unit
) {
    val imageHeight = if (isLarge) 220.dp else 160.dp
    var isLiked by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Box {
            AsyncImage(
                model = destination.imageUrl,
                contentDescription = destination.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight)
                    .background(
                        Brush.verticalGradient(
                            0.25f to Color.Transparent,
                            1.0f to Color.Black.copy(alpha = 0.80f)
                        )
                    )
            )
            // Rating badge
            Surface(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp),
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFFFD600)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Star, null, tint = Color(0xFF5D4037), modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(3.dp))
                    Text(
                        destination.rating.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF5D4037),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            // Like button
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.38f))
                    .clickable { isLiked = !isLiked },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    tint = if (isLiked) Color(0xFFEF5350) else Color.White,
                    modifier = Modifier.size(17.dp)
                )
            }
            // Info
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    destination.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = Color.White.copy(alpha = 0.80f), modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(3.dp))
                    Text(
                        destination.country,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.80f)
                    )
                }
                Spacer(Modifier.height(6.dp))
                Surface(shape = RoundedCornerShape(8.dp), color = Color.White.copy(alpha = 0.18f)) {
                    Text(
                        destination.description,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(0.88f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
