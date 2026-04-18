package com.tripsphere.presentation.screens.hotel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tripsphere.presentation.ui.theme.*

private data class HotelCity(
    val name: String,
    val country: String,
    val flag: String,
    val tagline: String,
    val latitude: Double,
    val longitude: Double,
    val gradientStart: Color,
    val gradientEnd: Color
)

private val popularHotelCities = listOf(
    HotelCity("Paris",     "France",       "🇫🇷", "City of Light",        48.8566,   2.3522,  Color(0xFF6366F1), Color(0xFF8B5CF6)),
    HotelCity("Dubai",     "UAE",          "🇦🇪", "Luxury Redefined",     25.2048,  55.2708,  Color(0xFFF59E0B), Color(0xFFEF4444)),
    HotelCity("Tokyo",     "Japan",        "🇯🇵", "Neon & Serenity",      35.6762, 139.6503,  Color(0xFFEC4899), Color(0xFF8B5CF6)),
    HotelCity("New York",  "USA",          "🇺🇸", "The City Never Sleeps",40.7128, -74.0060,  Color(0xFF3B82F6), Color(0xFF06B6D4)),
    HotelCity("London",    "UK",           "🇬🇧", "Historic Grandeur",    51.5074,  -0.1278,  Color(0xFF1E3A5F), Color(0xFF3B82F6)),
    HotelCity("Singapore", "Singapore",    "🇸🇬", "Garden City",          1.3521,  103.8198,  Color(0xFF10B981), Color(0xFF059669)),
    HotelCity("Rome",      "Italy",        "🇮🇹", "Eternal City",         41.9028,  12.4964,  Color(0xFFDC2626), Color(0xFFF97316)),
    HotelCity("Bangkok",   "Thailand",     "🇹🇭", "Temple & Street Food", 13.7563, 100.5018,  Color(0xFF7C3AED), Color(0xFFDB2777)),
    HotelCity("Barcelona", "Spain",        "🇪🇸", "Gaudí's Playground",   41.3851,   2.1734,  Color(0xFFF97316), Color(0xFFFBBF24)),
    HotelCity("Bali",      "Indonesia",    "🇮🇩", "Island of the Gods",   -8.3405, 115.0920,  Color(0xFF059669), Color(0xFF34D399)),
    HotelCity("Istanbul",  "Turkey",       "🇹🇷", "East Meets West",      41.0082,  28.9784,  Color(0xFFB45309), Color(0xFFF59E0B)),
    HotelCity("Maldives",  "Maldives",     "🇲🇻", "Paradise on Earth",    1.9708,   73.5369,  Color(0xFF0EA5E9), Color(0xFF38BDF8))
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HotelsHubScreen(
    onCitySelected: (name: String, lat: Double, lon: Double) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filtered = remember(searchQuery) {
        if (searchQuery.isBlank()) popularHotelCities
        else popularHotelCities.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.country.contains(searchQuery, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // ── Hero header ───────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(listOf(TripBlue, Color(0xFF7C3AED))),
                    shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                )
                .padding(horizontal = 20.dp)
                .padding(top = 56.dp, bottom = 24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Hotel, null, tint = Color.White, modifier = Modifier.size(28.dp))
                    Text(
                        "Find Hotels",
                        style      = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color      = Color.White
                    )
                }
                Text(
                    "Discover stays across the world's best cities",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f)
                )

                Spacer(Modifier.height(12.dp))

                // Search bar
                TextField(
                    value           = searchQuery,
                    onValueChange   = { searchQuery = it },
                    modifier        = Modifier.fillMaxWidth(),
                    placeholder     = { Text("Search city or country…", color = Color.White.copy(alpha = 0.6f)) },
                    leadingIcon     = { Icon(Icons.Default.Search, null, tint = Color.White.copy(alpha = 0.8f)) },
                    trailingIcon    = if (searchQuery.isNotEmpty()) {
                        { IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, null, tint = Color.White.copy(alpha = 0.8f))
                        }}
                    } else null,
                    singleLine      = true,
                    shape           = RoundedCornerShape(16.dp),
                    colors          = TextFieldDefaults.colors(
                        focusedContainerColor    = Color.White.copy(alpha = 0.2f),
                        unfocusedContainerColor  = Color.White.copy(alpha = 0.15f),
                        focusedTextColor         = Color.White,
                        unfocusedTextColor       = Color.White,
                        focusedIndicatorColor    = Color.Transparent,
                        unfocusedIndicatorColor  = Color.Transparent,
                        cursorColor              = Color.White
                    )
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // ── Section title ─────────────────────────────────────────────────────
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Default.TravelExplore, null, tint = TripBlue, modifier = Modifier.size(18.dp))
            Text(
                if (searchQuery.isBlank()) "Popular Destinations" else "${filtered.size} result${if (filtered.size != 1) "s" else ""}",
                style      = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color      = TextPrimary
            )
        }

        Spacer(Modifier.height(10.dp))

        // ── City grid ─────────────────────────────────────────────────────────
        if (filtered.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.SearchOff, null, tint = TextHint, modifier = Modifier.size(48.dp))
                    Text("No cities match \"$searchQuery\"", color = TextSecondary)
                    TextButton(onClick = { searchQuery = "" }) { Text("Clear search") }
                }
            }
        } else {
            LazyVerticalGrid(
                columns             = GridCells.Fixed(2),
                contentPadding      = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement   = Arrangement.spacedBy(12.dp),
                modifier              = Modifier.fillMaxSize()
            ) {
                items(filtered) { city ->
                    HotelCityCard(city = city, onClick = { onCitySelected(city.name, city.latitude, city.longitude) })
                }
                item { Spacer(Modifier.height(80.dp)) }  // bottom nav space
                item { Spacer(Modifier.height(80.dp)) }
            }
        }
    }
}

@Composable
private fun HotelCityCard(city: HotelCity, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Brush.verticalGradient(listOf(city.gradientStart, city.gradientEnd)))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication        = null,
                onClick           = onClick
            )
            .padding(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(city.flag, fontSize = 28.sp)
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    city.name,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color      = Color.White,
                    maxLines   = 1,
                    overflow   = TextOverflow.Ellipsis
                )
                Text(
                    city.tagline,
                    style   = MaterialTheme.typography.labelSmall,
                    color   = Color.White.copy(alpha = 0.8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Arrow icon top-right
        Icon(
            Icons.Default.ArrowForward, null,
            tint     = Color.White.copy(alpha = 0.7f),
            modifier = Modifier.align(Alignment.TopEnd).size(18.dp)
        )
    }
}
