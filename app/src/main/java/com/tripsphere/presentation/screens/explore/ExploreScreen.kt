package com.tripsphere.presentation.screens.explore

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
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

// ─── Models ───────────────────────────────────────────────────────────────────

private data class CategoryTab(
    val category: DestinationCategory,
    val label: String,
    val emoji: String,
    val accentColor: Color
)

private val categoryTabs = listOf(
    CategoryTab(DestinationCategory.ALL,       "All",       "🌍", TripBlue),
    CategoryTab(DestinationCategory.BEACH,     "Beaches",   "🏖️", Color(0xFF00BCD4)),
    CategoryTab(DestinationCategory.MOUNTAIN,  "Mountains", "⛰️", Color(0xFF4CAF50)),
    CategoryTab(DestinationCategory.CITY,      "Cities",    "🏙️", Color(0xFF9C27B0)),
    CategoryTab(DestinationCategory.ADVENTURE, "Adventure", "🧗", Color(0xFFFF7043))
)

// ─── Helpers ──────────────────────────────────────────────────────────────────

private fun formatReviews(count: Int): String = when {
    count >= 10_000 -> "${"%.0f".format(count / 1_000f)}k"
    count >= 1_000  -> "${"%.1f".format(count / 1_000f)}k"
    else            -> count.toString()
}

private fun priceLevelColor(level: String): Color = when (level) {
    "Luxury"   -> Color(0xFFFF8F00)
    "Budget"   -> Color(0xFF388E3C)
    else       -> Color(0xFF1565C0)
}

// ─── Main Screen ──────────────────────────────────────────────────────────────

@Composable
fun ExploreScreen(
    onDestinationClick: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: ExploreViewModel = hiltViewModel()
) {
    val uiState       by viewModel.uiState.collectAsState()
    val context        = LocalContext.current
    var showShakeHint  by remember { mutableStateOf(false) }

    // Shake to shuffle
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

    val allDests       = uiState.filteredDestinations
    val favouriteIds   by viewModel.favouriteIds.collectAsState()

    // ── ALL mode rows (computed once per list change) ──────────────────────────
    val featuredDests     = remember(allDests) { allDests.sortedByDescending { it.rating }.take(6) }
    val trendingDests     = remember(allDests) { allDests.sortedByDescending { it.reviewCount }.take(8) }
    val beachDests        = remember(allDests) { allDests.filter { it.category == DestinationCategory.BEACH } }
    val mountainDests     = remember(allDests) { allDests.filter { it.category == DestinationCategory.MOUNTAIN } }
    val cityDests         = remember(allDests) { allDests.filter { it.category == DestinationCategory.CITY } }
    val adventureDests    = remember(allDests) { allDests.filter { it.category == DestinationCategory.ADVENTURE } }
    val luxuryDests       = remember(allDests) { allDests.filter { it.priceLevel == "Luxury" }.sortedByDescending { it.rating } }
    val budgetDests       = remember(allDests) { allDests.filter { it.priceLevel == "Budget" }.sortedByDescending { it.rating } }
    val hiddenGemsDests   = remember(allDests) { allDests.filter { it.reviewCount < 22_000 }.sortedByDescending { it.rating }.take(8) }

    // ── Single-category rows ───────────────────────────────────────────────────
    val topRatedDests     = remember(allDests) { allDests.sortedByDescending { it.rating } }
    val luxuryCatDests    = remember(allDests) { allDests.filter { it.priceLevel == "Luxury" } }
    val budgetCatDests    = remember(allDests) { allDests.filter { it.priceLevel == "Budget" } }
    val midRangeCatDests  = remember(allDests) { allDests.filter { it.priceLevel == "Mid-Range" }.sortedByDescending { it.rating } }
    val othersDests       = remember(uiState.allDestinations, uiState.selectedCategory) {
        uiState.allDestinations
            .filter { it.category != uiState.selectedCategory }
            .sortedByDescending { it.rating }
            .take(10)
    }

    val navBarBottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 100.dp + navBarBottomPadding)
        ) {
            // ── Header ───────────────────────────────────────────────────────
            item {
                ExploreHeader(
                    searchQuery       = uiState.searchQuery,
                    onSearchChange    = viewModel::onSearchQueryChange,
                    totalDestinations = uiState.filteredDestinations.size
                )
            }

            // ── Category filter tabs ──────────────────────────────────────────
            item {
                CategoryFilterRow(
                    selectedCategory = uiState.selectedCategory,
                    onCategorySelect = { viewModel.onCategorySelected(it) }
                )
            }

            // ── Empty state ───────────────────────────────────────────────────
            if (allDests.isEmpty()) {
                item { EmptySearchState(query = uiState.searchQuery) }
                return@LazyColumn
            }

            // ── ALL mode: multiple themed rows ────────────────────────────────
            if (uiState.selectedCategory == DestinationCategory.ALL) {

                if (featuredDests.isNotEmpty()) {
                    item(key = "featured") {
                        SectionRow(
                            title              = "✨ Featured",
                            subtitle           = "The world's most breathtaking destinations",
                            accentColor        = TripBlue,
                            destinations       = featuredDests,
                            onItemClick        = onDestinationClick,
                            cardWidth          = 200.dp,
                            cardHeight         = 275.dp,
                            favouriteIds       = favouriteIds,
                            onToggleFavourite  = viewModel::toggleFavourite
                        )
                    }
                }

                if (trendingDests.isNotEmpty()) {
                    item(key = "trending") {
                        SectionRow(
                            title              = "🔥 Trending Now",
                            subtitle           = "Most visited destinations globally",
                            accentColor        = Color(0xFFFF5722),
                            destinations       = trendingDests,
                            onItemClick        = onDestinationClick,
                            favouriteIds       = favouriteIds,
                            onToggleFavourite  = viewModel::toggleFavourite
                        )
                    }
                }

                if (beachDests.isNotEmpty()) {
                    item(key = "beaches") {
                        SectionRow(
                            title              = "🏖️ Beach Escapes",
                            subtitle           = "Sun, sea & sand at their finest",
                            accentColor        = Color(0xFF00BCD4),
                            destinations       = beachDests,
                            onItemClick        = onDestinationClick,
                            onSeeAll           = { viewModel.onCategorySelected(DestinationCategory.BEACH) },
                            favouriteIds       = favouriteIds,
                            onToggleFavourite  = viewModel::toggleFavourite
                        )
                    }
                }

                if (mountainDests.isNotEmpty()) {
                    item(key = "mountains") {
                        SectionRow(
                            title              = "⛰️ Mountain Retreats",
                            subtitle           = "Peaks, glaciers & high-altitude wonder",
                            accentColor        = Color(0xFF4CAF50),
                            destinations       = mountainDests,
                            onItemClick        = onDestinationClick,
                            onSeeAll           = { viewModel.onCategorySelected(DestinationCategory.MOUNTAIN) },
                            favouriteIds       = favouriteIds,
                            onToggleFavourite  = viewModel::toggleFavourite
                        )
                    }
                }

                if (cityDests.isNotEmpty()) {
                    item(key = "cities") {
                        SectionRow(
                            title              = "🏙️ City Breaks",
                            subtitle           = "Culture, cuisine & iconic skylines",
                            accentColor        = Color(0xFF9C27B0),
                            destinations       = cityDests,
                            onItemClick        = onDestinationClick,
                            onSeeAll           = { viewModel.onCategorySelected(DestinationCategory.CITY) },
                            favouriteIds       = favouriteIds,
                            onToggleFavourite  = viewModel::toggleFavourite
                        )
                    }
                }

                if (adventureDests.isNotEmpty()) {
                    item(key = "adventure") {
                        SectionRow(
                            title              = "🧗 Adventure Awaits",
                            subtitle           = "Expeditions that push every boundary",
                            accentColor        = Color(0xFFFF7043),
                            destinations       = adventureDests,
                            onItemClick        = onDestinationClick,
                            onSeeAll           = { viewModel.onCategorySelected(DestinationCategory.ADVENTURE) },
                            favouriteIds       = favouriteIds,
                            onToggleFavourite  = viewModel::toggleFavourite
                        )
                    }
                }

                if (luxuryDests.isNotEmpty()) {
                    item(key = "luxury") {
                        SectionRow(
                            title              = "💎 Luxury Escapes",
                            subtitle           = "Premium experiences without compromise",
                            accentColor        = Color(0xFFFF8F00),
                            destinations       = luxuryDests,
                            onItemClick        = onDestinationClick,
                            favouriteIds       = favouriteIds,
                            onToggleFavourite  = viewModel::toggleFavourite
                        )
                    }
                }

                if (budgetDests.isNotEmpty()) {
                    item(key = "budget") {
                        SectionRow(
                            title              = "💰 Budget Discoveries",
                            subtitle           = "Incredible destinations, wallet-friendly prices",
                            accentColor        = Color(0xFF388E3C),
                            destinations       = budgetDests,
                            onItemClick        = onDestinationClick,
                            favouriteIds       = favouriteIds,
                            onToggleFavourite  = viewModel::toggleFavourite
                        )
                    }
                }

                if (hiddenGemsDests.isNotEmpty()) {
                    item(key = "hidden") {
                        SectionRow(
                            title              = "🔮 Hidden Gems",
                            subtitle           = "Off-the-beaten-path wonders worth seeking",
                            accentColor        = Color(0xFF7C4DFF),
                            destinations       = hiddenGemsDests,
                            onItemClick        = onDestinationClick,
                            favouriteIds       = favouriteIds,
                            onToggleFavourite  = viewModel::toggleFavourite
                        )
                    }
                }

            } else {
                // ── Single-category mode: curated rows ────────────────────────
                val tab = categoryTabs.find { it.category == uiState.selectedCategory }
                val tabColor = tab?.accentColor ?: TripBlue

                if (topRatedDests.isNotEmpty()) {
                    item(key = "cat_toprated") {
                        SectionRow(
                            title              = "⭐ Top Rated ${tab?.label ?: ""}",
                            subtitle           = "Highest-rated by verified travellers",
                            accentColor        = tabColor,
                            destinations       = topRatedDests,
                            onItemClick        = onDestinationClick,
                            cardWidth          = 200.dp,
                            cardHeight         = 275.dp,
                            favouriteIds       = favouriteIds,
                            onToggleFavourite  = viewModel::toggleFavourite
                        )
                    }
                }

                if (luxuryCatDests.size >= 2) {
                    item(key = "cat_luxury") {
                        SectionRow(
                            title              = "💎 Premium ${tab?.label ?: ""}",
                            subtitle           = "Elevated experiences, exceptional quality",
                            accentColor        = Color(0xFFFF8F00),
                            destinations       = luxuryCatDests,
                            onItemClick        = onDestinationClick,
                            favouriteIds       = favouriteIds,
                            onToggleFavourite  = viewModel::toggleFavourite
                        )
                    }
                }

                if (midRangeCatDests.size >= 2) {
                    item(key = "cat_midrange") {
                        SectionRow(
                            title              = "🌟 Best Value ${tab?.label ?: ""}",
                            subtitle           = "Outstanding quality at a fair price",
                            accentColor        = Color(0xFF1565C0),
                            destinations       = midRangeCatDests,
                            onItemClick        = onDestinationClick,
                            favouriteIds       = favouriteIds,
                            onToggleFavourite  = viewModel::toggleFavourite
                        )
                    }
                }

                if (budgetCatDests.size >= 2) {
                    item(key = "cat_budget") {
                        SectionRow(
                            title              = "💰 Budget-Friendly ${tab?.label ?: ""}",
                            subtitle           = "Great adventures without breaking the bank",
                            accentColor        = Color(0xFF388E3C),
                            destinations       = budgetCatDests,
                            onItemClick        = onDestinationClick,
                            favouriteIds       = favouriteIds,
                            onToggleFavourite  = viewModel::toggleFavourite
                        )
                    }
                }

                if (othersDests.isNotEmpty()) {
                    item(key = "cat_others") {
                        SectionRow(
                            title              = "🌍 You May Also Like",
                            subtitle           = "Explore beyond your current category",
                            accentColor        = TripBlue,
                            destinations       = othersDests,
                            onItemClick        = onDestinationClick,
                            favouriteIds       = favouriteIds,
                            onToggleFavourite  = viewModel::toggleFavourite
                        )
                    }
                }
            }
        }

        // ── Shake hint toast ─────────────────────────────────────────────────
        AnimatedVisibility(
            visible  = showShakeHint,
            enter    = fadeIn() + scaleIn(),
            exit     = fadeOut() + scaleOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 80.dp)
        ) {
            Surface(
                shape           = RoundedCornerShape(24.dp),
                color           = MaterialTheme.colorScheme.inverseSurface,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier              = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Shuffle, null,
                        tint     = MaterialTheme.colorScheme.inverseOnSurface,
                        modifier = Modifier.size(16.dp))
                    Text(
                        "Destinations shuffled!",
                        color      = MaterialTheme.colorScheme.inverseOnSurface,
                        style      = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// ─── Section Row (horizontal OTT-style scroll row) ────────────────────────────

@Composable
private fun SectionRow(
    title: String,
    subtitle: String = "",
    accentColor: Color,
    destinations: List<Destination>,
    onItemClick: (Int) -> Unit,
    onSeeAll: (() -> Unit)? = null,
    cardWidth: Dp  = 175.dp,
    cardHeight: Dp = 248.dp,
    favouriteIds: Set<Int> = emptySet(),
    onToggleFavourite: (Int) -> Unit = {}
) {
    Column(modifier = Modifier.padding(top = 22.dp)) {
        // Section header
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 2.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier              = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(22.dp)
                        .clip(CircleShape)
                        .background(accentColor)
                )
                Column {
                    Text(
                        text       = title,
                        style      = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color      = MaterialTheme.colorScheme.onBackground
                    )
                    if (subtitle.isNotEmpty()) {
                        Text(
                            text      = subtitle,
                            style     = MaterialTheme.typography.labelSmall,
                            color     = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines  = 1,
                            overflow  = TextOverflow.Ellipsis,
                            fontSize  = 11.sp
                        )
                    }
                }
            }
            if (onSeeAll != null) {
                Row(
                    modifier          = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication        = null,
                            onClick           = onSeeAll
                        )
                        .padding(start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "See All",
                        style      = MaterialTheme.typography.labelMedium,
                        color      = accentColor,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(Icons.Default.ChevronRight, null,
                        tint     = accentColor,
                        modifier = Modifier.size(16.dp))
                }
            } else {
                Text(
                    "${destinations.size} places",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        LazyRow(
            contentPadding        = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(destinations, key = { it.id }) { dest ->
                DestinationCard(
                    destination      = dest,
                    width            = cardWidth,
                    height           = cardHeight,
                    onClick          = { onItemClick(dest.id) },
                    isFavourite      = dest.id in favouriteIds,
                    onToggleFavourite = { onToggleFavourite(dest.id) }
                )
            }
        }
    }
}

// ─── Rich destination card ────────────────────────────────────────────────────

@Composable
private fun DestinationCard(
    destination: Destination,
    width: Dp,
    height: Dp,
    onClick: () -> Unit,
    isFavourite: Boolean = false,
    onToggleFavourite: () -> Unit = {}
) {

    val categoryColor = when (destination.category) {
        DestinationCategory.BEACH     -> Color(0xFF00BCD4)
        DestinationCategory.MOUNTAIN  -> Color(0xFF4CAF50)
        DestinationCategory.CITY      -> Color(0xFF9C27B0)
        DestinationCategory.ADVENTURE -> Color(0xFFFF7043)
        else                          -> TripBlue
    }

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .shadow(8.dp, RoundedCornerShape(20.dp), clip = false)
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
    ) {
        // Photo
        AsyncImage(
            model              = destination.imageUrl,
            contentDescription = destination.name,
            modifier           = Modifier.fillMaxSize(),
            contentScale       = ContentScale.Crop
        )

        // Gradient scrim — heavy at the bottom for legibility
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.00f to Color.Black.copy(alpha = 0.08f),
                        0.35f to Color.Transparent,
                        0.60f to Color.Black.copy(alpha = 0.20f),
                        1.00f to Color.Black.copy(alpha = 0.92f)
                    )
                )
        )

        // Category chip — top left
        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp),
            shape = RoundedCornerShape(8.dp),
            color = categoryColor.copy(alpha = 0.92f)
        ) {
            Text(
                text       = destination.category.name.lowercase().replaceFirstChar { it.uppercase() },
                modifier   = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                style      = MaterialTheme.typography.labelSmall,
                color      = Color.White,
                fontWeight = FontWeight.ExtraBold,
                fontSize   = 10.sp
            )
        }

        // Like button — top right
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .size(34.dp)
                .clip(CircleShape)
                .background(
                    if (isFavourite) Color(0xFFEF5350).copy(alpha = 0.90f)
                    else Color.Black.copy(alpha = 0.38f)
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication        = null
                ) { onToggleFavourite() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = if (isFavourite) "Remove from favourites" else "Add to favourites",
                tint               = Color.White,
                modifier           = Modifier.size(17.dp)
            )
        }

        // Bottom info block
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(horizontal = 11.dp, vertical = 11.dp)
        ) {
            // Name
            Text(
                text       = destination.name,
                style      = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.ExtraBold,
                color      = Color.White,
                maxLines   = 1,
                overflow   = TextOverflow.Ellipsis
            )

            // Highlights tagline
            if (destination.highlights.isNotEmpty()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text       = destination.highlights,
                    style      = MaterialTheme.typography.labelSmall,
                    color      = Color.White.copy(alpha = 0.72f),
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis,
                    fontStyle  = FontStyle.Italic,
                    fontSize   = 9.5.sp
                )
            }

            Spacer(Modifier.height(6.dp))

            // Country + Rating row
            Row(
                modifier              = Modifier.fillMaxWidth(),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn, null,
                        tint     = Color.White.copy(alpha = 0.78f),
                        modifier = Modifier.size(10.dp)
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text     = destination.country,
                        style    = MaterialTheme.typography.labelSmall,
                        color    = Color.White.copy(alpha = 0.78f),
                        maxLines = 1,
                        fontSize = 10.sp
                    )
                }
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(Icons.Default.Star, null,
                        tint     = Color(0xFFFFD600),
                        modifier = Modifier.size(10.dp))
                    Text(
                        text       = destination.rating.toString(),
                        style      = MaterialTheme.typography.labelSmall,
                        color      = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 10.sp
                    )
                    if (destination.reviewCount > 0) {
                        Text(
                            text     = " (${formatReviews(destination.reviewCount)})",
                            style    = MaterialTheme.typography.labelSmall,
                            color    = Color.White.copy(alpha = 0.60f),
                            fontSize = 9.sp
                        )
                    }
                }
            }

            // Price level pill
            if (destination.priceLevel.isNotEmpty()) {
                Spacer(Modifier.height(5.dp))
                val plColor = priceLevelColor(destination.priceLevel)
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = plColor.copy(alpha = 0.88f)
                ) {
                    Text(
                        text       = destination.priceLevel,
                        modifier   = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                        style      = MaterialTheme.typography.labelSmall,
                        color      = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 9.sp
                    )
                }
            }
        }
    }
}

// ─── Immersive Header ─────────────────────────────────────────────────────────

@Composable
private fun ExploreHeader(
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    totalDestinations: Int
) {
    val bgColor = MaterialTheme.colorScheme.background
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(290.dp)
    ) {
        AsyncImage(
            model              = "https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?w=900",
            contentDescription = null,
            modifier           = Modifier.fillMaxSize(),
            contentScale       = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.00f to Color.Black.copy(alpha = 0.55f),
                        0.40f to Color.Black.copy(alpha = 0.15f),
                        0.75f to bgColor.copy(alpha = 0.75f),
                        1.00f to bgColor
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(top = 14.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                verticalAlignment     = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Discover",
                        style      = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.ExtraBold,
                        color      = Color.White,
                        lineHeight = 36.sp
                    )
                    Text(
                        "Your next destination awaits ✈️",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.88f)
                    )
                }
                Surface(
                    shape  = RoundedCornerShape(20.dp),
                    color  = Color.White.copy(alpha = 0.18f),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp, Color.White.copy(alpha = 0.35f)
                    )
                ) {
                    Column(
                        modifier            = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "$totalDestinations+",
                            style      = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color      = Color.White
                        )
                        Text(
                            "Places",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.80f)
                        )
                    }
                }
            }

            // Search bar
            Surface(
                modifier       = Modifier
                    .fillMaxWidth()
                    .shadow(12.dp, RoundedCornerShape(18.dp)),
                shape          = RoundedCornerShape(18.dp),
                color          = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier          = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Icon(Icons.Default.Search, null, tint = TripBlue, modifier = Modifier.size(22.dp))
                    Spacer(Modifier.width(12.dp))
                    androidx.compose.foundation.text.BasicTextField(
                        value         = searchQuery,
                        onValueChange = onSearchChange,
                        modifier      = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        singleLine    = true,
                        textStyle     = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        cursorBrush   = androidx.compose.ui.graphics.SolidColor(TripBlue),
                        decorationBox = { innerTextField ->
                            Box(contentAlignment = Alignment.CenterStart) {
                                if (searchQuery.isEmpty()) {
                                    Text(
                                        "Search destinations, countries…",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick  = { onSearchChange("") },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.Close, null,
                                tint     = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp))
                        }
                    } else {
                        Icon(Icons.Default.Mic, null,
                            tint     = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}

// ─── Category filter row (tabs with underline indicator only) ─────────────────

@Composable
private fun CategoryFilterRow(
    selectedCategory: DestinationCategory,
    onCategorySelect: (DestinationCategory) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyRow(
            contentPadding        = PaddingValues(horizontal = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items(categoryTabs) { tab ->
                val isSelected = tab.category == selectedCategory
                val textColor by animateColorAsState(
                    targetValue   = if (isSelected) tab.accentColor
                                    else MaterialTheme.colorScheme.onSurfaceVariant,
                    animationSpec = tween(200),
                    label         = "cat_text"
                )
                Column(
                    modifier            = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication        = null
                        ) { onCategorySelect(tab.category) }
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(tab.emoji, fontSize = 13.sp)
                        Text(
                            tab.label.uppercase(),
                            style         = MaterialTheme.typography.labelMedium,
                            fontWeight    = if (isSelected) FontWeight.ExtraBold else FontWeight.Normal,
                            color         = textColor,
                            letterSpacing = 0.8.sp
                        )
                    }
                    Spacer(Modifier.height(6.dp))
                    AnimatedVisibility(
                        visible = isSelected,
                        enter   = fadeIn(tween(180)),
                        exit    = fadeOut(tween(180))
                    ) {
                        Box(
                            modifier = Modifier
                                .width(32.dp)
                                .height(3.dp)
                                .clip(CircleShape)
                                .background(tab.accentColor)
                        )
                    }
                    if (!isSelected) Spacer(Modifier.height(3.dp))
                }
            }
        }

        HorizontalDivider(
            color     = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
            thickness = 0.8.dp
        )
    }
}

// ─── Empty state ──────────────────────────────────────────────────────────────

@Composable
private fun EmptySearchState(query: String) {
    Column(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(vertical = 56.dp),
        horizontalAlignment   = Alignment.CenterHorizontally,
        verticalArrangement   = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier         = Modifier
                .size(84.dp)
                .clip(CircleShape)
                .background(TripBlue.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.SearchOff, null, tint = TripBlue, modifier = Modifier.size(42.dp))
        }
        Text(
            text       = if (query.isNotEmpty()) "No results for \"$query\"" else "Nothing here yet",
            style      = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color      = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text  = "Try a different search term or browse a category",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
