package com.tripsphere.presentation.screens.home

import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.tripsphere.presentation.components.*
import com.tripsphere.presentation.ui.theme.*
import com.tripsphere.presentation.viewmodel.HomeViewModel
import com.tripsphere.utils.DummyData
import kotlinx.coroutines.delay

// ─── Data models for static content ─────────────────────────────────────────

private data class HeroSlide(
    val imageUrl: String,
    val title: String,
    val tagline: String,
    val badge: String
)

private data class TravelTip(
    val icon: ImageVector,
    val title: String,
    val desc: String,
    val color: Color
)

private val heroSlides = listOf(
    HeroSlide(
        "https://images.unsplash.com/photo-1537996194471-e657df975ab4?w=800",
        "Bali, Indonesia",
        "Where paradise meets culture",
        "🔥 Trending"
    ),
    HeroSlide(
        "https://images.unsplash.com/photo-1492571350019-22de08371fd3?w=800",
        "Kyoto, Japan",
        "Walk through living history",
        "🌸 Must Visit"
    ),
    HeroSlide(
        "https://images.unsplash.com/photo-1514890547357-a9ee288728e0?w=800",
        "Santorini, Greece",
        "Sun, sea & white-washed dreams",
        "🌊 Top Rated"
    ),
    HeroSlide(
        "https://images.unsplash.com/photo-1474044159687-1ee9f3a51722?w=800",
        "Machu Picchu, Peru",
        "Rise to ancient wonders",
        "🏔️ Adventure"
    )
)

// IDs matching DummyData: Bali=2, Paris=3, New York=4, Kyoto=5, Santorini=1, Swiss Alps=7
private val popularDestinationIds = listOf(2, 3, 5, 1, 7)

private val travelTips = listOf(
    TravelTip(Icons.Default.WbSunny, "Best Weather", "Book 3 months ahead for ideal conditions", Color(0xFFF57C00)),
    TravelTip(Icons.Default.LocalOffer, "Smart Budget", "Plan expenses & avoid hidden fees", Color(0xFF388E3C)),
    TravelTip(Icons.Default.Security, "Stay Safe", "Always get travel insurance", Color(0xFF1565C0)),
    TravelTip(Icons.Default.Restaurant, "Local Food", "Eat where the locals eat", Color(0xFFD32F2F))
)

// ─── Testimonials Data ────────────────────────────────────────────────────────
private data class Testimonial(
    val name: String,
    val location: String,
    val avatarUrl: String,
    val review: String,
    val rating: Int
)

private val testimonials = listOf(
    Testimonial("Emma W.", "New York, USA", "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=200", "TripSphere made my Bali trip magical! The recommendations were spot on and it felt extremely personalized.", 5),
    Testimonial("Liam P.", "London, UK", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=200", "An incredibly polished and premium app! Tracking my upcoming trips has never been easier or looked beautiful.", 5),
    Testimonial("Sophia T.", "Sydney, AU", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=200", "I found the best flights and hidden gems for my local holidays. Highly recommend it to all travelers!", 4)
)

// ─── Main Screen ─────────────────────────────────────────────────────────────

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onNavigateToExplore: () -> Unit,
    onNavigateToCreateTrip: () -> Unit,
    onNavigateToMyTrips: () -> Unit,
    onNavigateToHotels: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToDestinationDetail: (Int) -> Unit,
    onNavigateToActiveTrip: (Long) -> Unit,
    onNavigateToOverview: (Long) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        LoadingScreen()
        return
    }

    val navBarBottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 100.dp + navBarBottomPadding)
    ) {

        // ── Hero Header + Auto-scroll Carousel ──────────────────────────────
        item {
            HeroCarouselSection(
                userName = uiState.user?.name?.split(" ")?.firstOrNull(),
                onProfileClick = onNavigateToProfile,
                onSearchClick = onNavigateToExplore
            )
        }

        // ── Quick Actions ───────────────────────────────────────────────────
        item {
            Spacer(Modifier.height(24.dp))
            HomeSection(title = "Quick Actions") {
                QuickActionsRow(
                    onCreateTrip = onNavigateToCreateTrip,
                    onMyTrips = onNavigateToMyTrips,
                    onNavigateToHotels = onNavigateToHotels,
                    onNavigateToExplore = onNavigateToExplore
                )
            }
        }

        // ── Active Trip ─────────────────────────────────────────────────────
        if (uiState.activeTrip != null) {
            item {
                Spacer(Modifier.height(24.dp))
                HomeSectionWithAction(
                    title = "Active Trip",
                    actionLabel = "All Trips",
                    onAction = onNavigateToMyTrips
                ) {
                    ActiveTripBanner(
                        trip = uiState.activeTrip!!,
                        modifier = Modifier.padding(horizontal = 20.dp),
                        onClick = { onNavigateToActiveTrip(uiState.activeTrip!!.id) }
                    )
                }
            }
        }

        // ── Upcoming Trips ──────────────────────────────────────────────────
        if (uiState.upcomingTrips.isNotEmpty()) {
            item {
                Spacer(Modifier.height(24.dp))
                HomeSectionWithAction(
                    title = "Upcoming Trips",
                    actionLabel = "See All",
                    onAction = onNavigateToMyTrips
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) {
                        items(uiState.upcomingTrips) { trip ->
                            UpcomingTripCard(
                                trip = trip,
                                onClick = { onNavigateToOverview(trip.id) }
                            )
                        }
                    }
                }
            }
        }

        // ── Popular Destinations ────────────────────────────────────────────
        item {
            val popularDests = remember {
                DummyData.destinations
                    .filter { it.id in popularDestinationIds }
                    .sortedBy { popularDestinationIds.indexOf(it.id) }
            }
            Spacer(Modifier.height(24.dp))
            HomeSectionWithAction(
                title = "Popular Destinations",
                actionLabel = "Explore",
                onAction = onNavigateToExplore
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    items(popularDests) { dest ->
                        DestinationCard(
                            destination = dest,
                            onClick = { onNavigateToDestinationDetail(dest.id) }
                        )
                    }
                }
            }
        }


        // ── Traveler Testimonials ──────────────────────────────────────────────────
        item {
            Spacer(Modifier.height(24.dp))
            HomeSection(title = "Traveler Stories") {
                TestimonialsCarousel()
            }
        }

        // ── Travel Tips ─────────────────────────────────────────────────────
        item {
            Spacer(Modifier.height(24.dp))
            HomeSection(title = "Travel Tips") {
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    travelTips.forEach { tip ->
                        TravelTipCard(tip = tip)
                    }
                }
            }
        }

        // ── Empty State ─────────────────────────────────────────────────────
        if (uiState.activeTrip == null && uiState.upcomingTrips.isEmpty()) {
            item {
                Spacer(Modifier.height(32.dp))
                EmptyTripState(onCreateTrip = onNavigateToCreateTrip)
            }
        }
    }
}

// ─── Hero Carousel ────────────────────────────────────────────────────────────

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HeroCarouselSection(
    userName: String?,
    onProfileClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { heroSlides.size })

    // Auto-scroll every 4 seconds
    LaunchedEffect(Unit) {
        while (true) {
            delay(4000)
            val next = (pagerState.currentPage + 1) % heroSlides.size
            pagerState.animateScrollToPage(
                page = next,
                animationSpec = tween(durationMillis = 800, easing = EaseInOutCubic)
            )
        }
    }

    Box(modifier = Modifier.fillMaxWidth().height(400.dp)) {
        // Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val slide = heroSlides[page]
            Box(modifier = Modifier.fillMaxSize()) {
                AsyncImage(
                    model = slide.imageUrl,
                    contentDescription = slide.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Multi-stop dark gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                0.0f to Color.Black.copy(alpha = 0.45f),
                                0.45f to Color.Transparent,
                                1.0f to Color.Black.copy(alpha = 0.80f)
                            )
                        )
                )
                // Slide text at bottom
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 20.dp, end = 20.dp, bottom = 44.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = TripAccent.copy(alpha = 0.92f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = slide.badge,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = slide.title,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        lineHeight = 38.sp
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = slide.tagline,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }

        // Top bar (name + notification bell)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Welcome back ✈️",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.82f)
                )
                Text(
                    text = userName?.let { "Hello, $it 👋" } ?: "Hello, Traveler 👋",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                GlassIconButton(icon = Icons.Default.Search, onClick = onSearchClick)
                GlassIconButton(icon = Icons.Default.Notifications, onClick = onProfileClick)
            }
        }

        // Dot indicators at the bottom
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            repeat(heroSlides.size) { index ->
                val isSelected = index == pagerState.currentPage
                val width by animateDpAsState(
                    targetValue = if (isSelected) 24.dp else 7.dp,
                    animationSpec = tween(300),
                    label = "dot_width"
                )
                Box(
                    modifier = Modifier
                        .height(7.dp)
                        .width(width)
                        .clip(CircleShape)
                        .background(if (isSelected) Color.White else Color.White.copy(alpha = 0.45f))
                )
            }
        }
    }
}

@Composable
private fun GlassIconButton(icon: ImageVector, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.22f))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
    }
}

// ─── Quick Actions Row ────────────────────────────────────────────────────────

private data class QuickAction(
    val icon: ImageVector,
    val label: String,
    val gradientStart: Color,
    val gradientEnd: Color
)

@Composable
private fun QuickActionsRow(
    onCreateTrip: () -> Unit,
    onMyTrips: () -> Unit,
    onNavigateToHotels: () -> Unit,
    onNavigateToExplore: () -> Unit
) {
    val actions = listOf(
        QuickAction(Icons.Default.AddCircle, "New Trip", Color(0xFF1565C0), Color(0xFF1976D2)) to onCreateTrip,
        QuickAction(Icons.Default.Map, "My Trips", Color(0xFF00695C), Color(0xFF00897B)) to onMyTrips,
        QuickAction(Icons.Default.Hotel, "Hotels", Color(0xFFBF360C), Color(0xFFE64A19)) to onNavigateToHotels,
        QuickAction(Icons.Default.Explore, "Explore", Color(0xFF4A148C), Color(0xFF6A1B9A)) to onNavigateToExplore
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        actions.forEach { (action, onClick) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable(onClick = onClick)
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(
                            Brush.linearGradient(listOf(action.gradientStart, action.gradientEnd))
                        )
                        .shadow(6.dp, RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = action.icon,
                        contentDescription = action.label,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(Modifier.height(6.dp))
                Text(
                    text = action.label,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

// ─── Destination Card ─────────────────────────────────────────────────────────

@Composable
private fun DestinationCard(destination: com.tripsphere.domain.model.Destination, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box {
            AsyncImage(
                model = destination.imageUrl,
                contentDescription = destination.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .background(
                        Brush.verticalGradient(
                            0.35f to Color.Transparent,
                            1.0f to Color.Black.copy(alpha = 0.78f)
                        )
                    )
            )
            // Category chip
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.White.copy(alpha = 0.22f),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
            ) {
                Text(
                    text = destination.category.name.lowercase()
                        .replaceFirstChar { it.uppercase() },
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            // Rating chip
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFFFD600),
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFF5D4037),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = "${"%.1f".format(destination.rating)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF5D4037),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            // Name & country at bottom
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(14.dp)
            ) {
                Text(
                    text = destination.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.White.copy(0.85f),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(Modifier.width(3.dp))
                    Text(
                        text = destination.country,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(0.85f)
                    )
                }
            }
        }
    }
}

// ─── Upcoming Trip Card ───────────────────────────────────────────────────────

@Composable
private fun UpcomingTripCard(trip: com.tripsphere.domain.model.Trip, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box {
            AsyncImage(
                model = trip.imageUrl.ifEmpty { "https://images.unsplash.com/photo-1488646953014-85cb44e25828?w=400" },
                contentDescription = trip.destination,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        Brush.verticalGradient(
                            0.3f to Color.Transparent,
                            1.0f to Color.Black.copy(alpha = 0.80f)
                        )
                    )
            )
            // Upcoming badge
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = TripBlue.copy(alpha = 0.88f),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(10.dp)
            ) {
                Text(
                    "Upcoming",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(14.dp)
            ) {
                Text(
                    text = trip.destination.substringBefore(","),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = Color.White.copy(0.75f),
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = trip.startDate.toString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(0.75f)
                    )
                }
            }
        }
    }
}


// ─── Travel Tip Card ──────────────────────────────────────────────────────────

@Composable
private fun TravelTipCard(tip: TravelTip) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(tip.color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(tip.icon, contentDescription = null, tint = tip.color, modifier = Modifier.size(28.dp))
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tip.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = tip.desc,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ─── Testimonials Carousel ────────────────────────────────────────────────────

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TestimonialsCarousel() {
    val pagerState = rememberPagerState(pageCount = { testimonials.size })
    
    LaunchedEffect(Unit) {
        while(true) {
            delay(5000)
            val next = (pagerState.currentPage + 1) % testimonials.size
            pagerState.animateScrollToPage(
                page = next,
                animationSpec = tween(durationMillis = 800, easing = EaseInOutCubic)
            )
        }
    }

    Column {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 20.dp),
            pageSpacing = 16.dp
        ) { page ->
            val testimonial = testimonials[page]
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = TripBlue)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Icon(
                        Icons.Default.FormatQuote,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 10.dp, end = 10.dp)
                            .size(80.dp)
                    )
                    
                    Column(modifier = Modifier.padding(24.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = testimonial.avatarUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.White, CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = testimonial.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = testimonial.location,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White.copy(alpha = 0.85f)
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "\"${testimonial.review}\"",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.95f),
                            lineHeight = 24.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                        Spacer(Modifier.height(20.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            repeat(5) { index ->
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = null,
                                    tint = if (index < testimonial.rating) Color(0xFFFFD600) else Color.White.copy(alpha = 0.3f),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
        
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(testimonials.size) { index ->
                val isSelected = index == pagerState.currentPage
                val width by animateDpAsState(
                    targetValue = if (isSelected) 24.dp else 8.dp,
                    label = "indicator"
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .height(8.dp)
                        .width(width)
                        .clip(CircleShape)
                        .background(if (isSelected) TripBlue else TripBlue.copy(alpha = 0.2f))
                )
            }
        }
    }
}

// ─── Empty State ──────────────────────────────────────────────────────────────

@Composable
private fun EmptyTripState(onCreateTrip: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(listOf(Color(0xFF1976D2).copy(0.18f), Color(0xFF42A5F5).copy(0.06f)))
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.TravelExplore,
                contentDescription = null,
                tint = TripBlue,
                modifier = Modifier.size(58.dp)
            )
        }
        Spacer(Modifier.height(20.dp))
        Text(
            text = "No trips yet",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Start planning your first adventure and explore the world!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onCreateTrip,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(27.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = TripBlue
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AddCircle, null, tint = Color.White, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    "Plan My First Trip",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

// ─── Section Layout Helpers ───────────────────────────────────────────────────

@Composable
private fun HomeSection(title: String, content: @Composable () -> Unit) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 0.dp)
        )
        Spacer(Modifier.height(14.dp))
        content()
    }
}

@Composable
private fun HomeSectionWithAction(
    title: String,
    actionLabel: String,
    onAction: () -> Unit,
    content: @Composable () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            TextButton(onClick = onAction) {
                Text(
                    text = actionLabel,
                    color = TripBlue,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.width(2.dp))
                Icon(Icons.Default.ChevronRight, null, tint = TripBlue, modifier = Modifier.size(16.dp))
            }
        }
        Spacer(Modifier.height(14.dp))
        content()
    }
}
