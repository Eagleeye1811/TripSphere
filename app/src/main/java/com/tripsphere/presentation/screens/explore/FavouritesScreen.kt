package com.tripsphere.presentation.screens.explore

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import com.tripsphere.domain.model.Destination
import com.tripsphere.domain.model.DestinationCategory
import com.tripsphere.presentation.ui.theme.*
import com.tripsphere.utils.DummyData
import com.tripsphere.utils.FavouritesStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

// ── ViewModel ─────────────────────────────────────────────────────────────────

@HiltViewModel
class FavouritesViewModel @Inject constructor(
    private val favouritesStore: FavouritesStore
) : ViewModel() {

    val favouriteDestinations: StateFlow<List<Destination>> =
        favouritesStore.favouriteIds
            .map { ids -> DummyData.destinations.filter { it.id in ids } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val favouriteIds: StateFlow<Set<Int>> =
        favouritesStore.favouriteIds
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    fun toggleFavourite(id: Int) {
        viewModelScope.launch { favouritesStore.toggle(id) }
    }
}

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun FavouritesScreen(
    onDestinationClick: (Int) -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToExplore: () -> Unit,
    viewModel: FavouritesViewModel = hiltViewModel()
) {
    val favourites     by viewModel.favouriteDestinations.collectAsState()
    val favouriteIds   by viewModel.favouriteIds.collectAsState()
    val navBarPadding  = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // ── Header ──────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.verticalGradient(listOf(TripBlueDark, TripBlue)))
                .statusBarsPadding()
                .padding(bottom = 20.dp)
        ) {
            IconButton(
                onClick  = onNavigateBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 8.dp, start = 4.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }

            Column(
                modifier            = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Favorite, null,
                        tint     = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    "My Favourites",
                    style      = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color      = Color.White
                )
                Text(
                    if (favourites.isEmpty()) "No saved destinations yet"
                    else "${favourites.size} destination${if (favourites.size == 1) "" else "s"} saved",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        // ── Content ──────────────────────────────────────────────────────────
        if (favourites.isEmpty()) {
            FavouritesEmptyState(onNavigateToExplore = onNavigateToExplore)
        } else {
            LazyVerticalGrid(
                columns             = GridCells.Fixed(2),
                contentPadding      = PaddingValues(
                    start  = 16.dp,
                    end    = 16.dp,
                    top    = 16.dp,
                    bottom = 100.dp + navBarPadding
                ),
                verticalArrangement   = Arrangement.spacedBy(14.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier              = Modifier.fillMaxSize()
            ) {
                items(favourites, key = { it.id }) { dest ->
                    FavouriteCard(
                        destination      = dest,
                        isFavourite      = dest.id in favouriteIds,
                        onClick          = { onDestinationClick(dest.id) },
                        onToggleFavourite = { viewModel.toggleFavourite(dest.id) }
                    )
                }
            }
        }
    }
}

// ── Favourite card ─────────────────────────────────────────────────────────────

@Composable
private fun FavouriteCard(
    destination: Destination,
    isFavourite: Boolean,
    onClick: () -> Unit,
    onToggleFavourite: () -> Unit
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
            .fillMaxWidth()
            .height(220.dp)
            .shadow(6.dp, RoundedCornerShape(18.dp), clip = false)
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model              = destination.imageUrl,
            contentDescription = destination.name,
            modifier           = Modifier.fillMaxSize(),
            contentScale       = ContentScale.Crop
        )

        // Gradient scrim
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.0f to Color.Black.copy(0.06f),
                        0.4f to Color.Transparent,
                        0.65f to Color.Black.copy(0.25f),
                        1.0f to Color.Black.copy(0.90f)
                    )
                )
        )

        // Category chip — top left
        Surface(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp),
            shape = RoundedCornerShape(7.dp),
            color = categoryColor.copy(alpha = 0.92f)
        ) {
            Text(
                text       = destination.category.name.lowercase().replaceFirstChar { it.uppercase() },
                modifier   = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                fontSize   = 9.sp,
                color      = Color.White,
                fontWeight = FontWeight.ExtraBold
            )
        }

        // Heart button — top right
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    if (isFavourite) Color(0xFFEF5350).copy(0.90f)
                    else Color.Black.copy(0.35f)
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication        = null,
                    onClick           = onToggleFavourite
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector        = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint               = Color.White,
                modifier           = Modifier.size(16.dp)
            )
        }

        // Bottom info
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp)
        ) {
            Text(
                text       = destination.name,
                fontSize   = 14.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = Color.White,
                maxLines   = 1,
                overflow   = TextOverflow.Ellipsis
            )
            if (destination.highlights.isNotEmpty()) {
                Text(
                    text      = destination.highlights,
                    fontSize  = 9.sp,
                    color     = Color.White.copy(0.70f),
                    maxLines  = 1,
                    overflow  = TextOverflow.Ellipsis,
                    fontStyle = FontStyle.Italic
                )
            }
            Spacer(Modifier.height(5.dp))
            Row(
                modifier              = Modifier.fillMaxWidth(),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(Icons.Default.LocationOn, null, Modifier.size(10.dp), tint = Color.White.copy(0.75f))
                    Text(destination.country, fontSize = 10.sp, color = Color.White.copy(0.75f), maxLines = 1)
                }
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(Icons.Default.Star, null, Modifier.size(10.dp), tint = Color(0xFFFFD600))
                    Text(
                        destination.rating.toString(),
                        fontSize   = 10.sp,
                        color      = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ── Empty state ───────────────────────────────────────────────────────────────

@Composable
private fun FavouritesEmptyState(onNavigateToExplore: () -> Unit) {
    Column(
        modifier              = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment   = Alignment.CenterHorizontally,
        verticalArrangement   = Arrangement.Center
    ) {
        Box(
            modifier         = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(TripBlue.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.FavoriteBorder, null,
                tint     = TripBlue,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(Modifier.height(20.dp))

        Text(
            "No Favourites Yet",
            style      = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign  = TextAlign.Center,
            color      = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(8.dp))

        Text(
            "Tap the ❤ on any destination in Explore\nto save it here for quick access.",
            style     = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color     = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 22.sp
        )

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = onNavigateToExplore,
            shape   = RoundedCornerShape(14.dp),
            colors  = ButtonDefaults.buttonColors(containerColor = TripBlue),
            modifier = Modifier.height(50.dp).fillMaxWidth(0.65f)
        ) {
            Icon(Icons.Default.Explore, null, Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("Explore Destinations", fontWeight = FontWeight.Bold)
        }
    }
}
