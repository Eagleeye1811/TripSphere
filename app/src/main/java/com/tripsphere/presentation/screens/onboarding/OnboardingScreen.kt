package com.tripsphere.presentation.screens.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tripsphere.presentation.ui.theme.*
import com.tripsphere.utils.OnboardingPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.sqrt

// ── ViewModel ────────────────────────────────────────────────────────────────

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val prefs: OnboardingPreferences
) : ViewModel() {
    fun completeOnboarding() {
        viewModelScope.launch { prefs.setOnboardingCompleted() }
    }
}

// ── Page data ────────────────────────────────────────────────────────────────

data class OnboardingPage(
    val title: String,
    val subtitle: String,
    val bgGradient: List<Color>,
    val accentColor: Color,
    val tag: String
)

val onboardingPages = listOf(
    OnboardingPage(
        title = "Discover Amazing Places",
        subtitle = "Explore thousands of handpicked destinations tailored to your unique travel style",
        bgGradient = listOf(Color(0xFF050C1A), Color(0xFF0B2545), Color(0xFF1565C0)),
        accentColor = Color(0xFF4FC3F7),
        tag = "500+ Destinations"
    ),
    OnboardingPage(
        title = "Plan Trips Effortlessly",
        subtitle = "Build day-by-day itineraries, track budgets and stay perfectly organised on every trip",
        bgGradient = listOf(Color(0xFF050C1A), Color(0xFF082035), Color(0xFF0D47A1)),
        accentColor = Color(0xFF69F0AE),
        tag = "Smart Planning"
    ),
    OnboardingPage(
        title = "Travel Smarter",
        subtitle = "Access maps, guides and bookings offline — adventure without limits or anxiety",
        bgGradient = listOf(Color(0xFF050C1A), Color(0xFF12153E), Color(0xFF1A237E)),
        accentColor = Color(0xFFFFB74D),
        tag = "Works Offline"
    )
)

// ── Main screen ───────────────────────────────────────────────────────────────

@Composable
fun OnboardingScreen(
    onGetStarted: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    var currentPage by remember { mutableIntStateOf(0) }
    val page = onboardingPages[currentPage]

    Box(modifier = Modifier.fillMaxSize()) {

        // Animated full-screen background gradient
        AnimatedContent(
            targetState = currentPage,
            transitionSpec = { fadeIn(tween(700)) togetherWith fadeOut(tween(700)) },
            label = "bg_gradient"
        ) { idx ->
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(onboardingPages[idx].bgGradient))
            )
        }

        // Main layout column — illustration fills remaining space, card pins to bottom
        Column(modifier = Modifier.fillMaxSize()) {

            // Skip button row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = 8.dp, end = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = {
                        viewModel.completeOnboarding()
                        onGetStarted()
                    }
                ) {
                    Text(
                        text = "Skip",
                        color = Color.White.copy(alpha = 0.65f),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                }
            }

            // Illustration area — takes all remaining height above the card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = currentPage,
                    transitionSpec = {
                        (slideInHorizontally { it / 3 } + fadeIn(tween(450))) togetherWith
                                (slideOutHorizontally { -it / 3 } + fadeOut(tween(300)))
                    },
                    label = "illustration"
                ) { idx ->
                    when (idx) {
                        0 -> DiscoverIllustration(onboardingPages[0].accentColor)
                        1 -> PlanIllustration(onboardingPages[1].accentColor)
                        else -> TravelSmartIllustration(onboardingPages[2].accentColor)
                    }
                }
            }

            // ── Bottom dark card ─────────────────────────────────────────────
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp),
                color = Color(0xFF0B1628)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 28.dp)
                        .padding(top = 20.dp, bottom = 20.dp)
                        .navigationBarsPadding(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Drag handle
                    Box(
                        Modifier
                            .size(42.dp, 4.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.12f))
                    )

                    Spacer(Modifier.height(20.dp))

                    // Animated dot indicators
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        onboardingPages.forEachIndexed { index, _ ->
                            val isActive = index == currentPage
                            val dotWidth by animateDpAsState(
                                targetValue = if (isActive) 28.dp else 8.dp,
                                animationSpec = spring(stiffness = Spring.StiffnessMedium),
                                label = "dot_w_$index"
                            )
                            val dotColor by animateColorAsState(
                                targetValue = if (isActive) page.accentColor else Color.White.copy(alpha = 0.2f),
                                animationSpec = tween(300),
                                label = "dot_c_$index"
                            )
                            Box(
                                Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(dotColor)
                                    .size(width = dotWidth, height = 6.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // Page title + subtitle
                    AnimatedContent(
                        targetState = currentPage,
                        transitionSpec = {
                            (fadeIn(tween(350)) + slideInVertically { 20 }) togetherWith
                                    (fadeOut(tween(200)) + slideOutVertically { -20 })
                        },
                        label = "card_text"
                    ) { idx ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            // Tag pill
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = onboardingPages[idx].accentColor.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = onboardingPages[idx].tag,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = onboardingPages[idx].accentColor
                                )
                            }

                            Spacer(Modifier.height(14.dp))

                            Text(
                                text = onboardingPages[idx].title,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center,
                                color = Color.White,
                                lineHeight = 32.sp
                            )

                            Spacer(Modifier.height(10.dp))

                            Text(
                                text = onboardingPages[idx].subtitle,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                color = Color.White.copy(alpha = 0.55f),
                                lineHeight = 21.sp
                            )
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // Next / Get Started button
                    if (currentPage < onboardingPages.size - 1) {
                        Button(
                            onClick = { currentPage++ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = page.accentColor
                            )
                        ) {
                            Text(
                                text = "Next",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF050C1A)
                            )
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Color(0xFF050C1A)
                            )
                        }
                    } else {
                        Button(
                            onClick = {
                                viewModel.completeOnboarding()
                                onGetStarted()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TripAccent)
                        ) {
                            Icon(
                                imageVector = Icons.Default.RocketLaunch,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = Color.White
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = "Get Started",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

// ── Illustration helpers ──────────────────────────────────────────────────────

@Composable
private fun FloatTransition(speed: Int = 2400): Float {
    val t = rememberInfiniteTransition(label = "float_$speed")
    return t.animateFloat(
        initialValue = -8f, targetValue = 8f,
        animationSpec = infiniteRepeatable(
            tween(speed, easing = CubicBezierEasing(0.45f, 0f, 0.55f, 1f)),
            RepeatMode.Reverse
        ),
        label = "f$speed"
    ).value
}

// ── Page 1 — Discover Amazing Places ─────────────────────────────────────────

@Composable
fun DiscoverIllustration(accentColor: Color) {
    val f1 = FloatTransition(2400)
    val f2 = FloatTransition(3000)
    val f3 = FloatTransition(2700)

    val pulseScale by rememberInfiniteTransition(label = "pulse").animateFloat(
        0.92f, 1.08f,
        infiniteRepeatable(tween(1800, easing = CubicBezierEasing(0.45f, 0f, 0.55f, 1f)), RepeatMode.Reverse),
        label = "scale"
    )

    Box(Modifier.fillMaxSize()) {
        // Ambient glow behind everything
        Box(
            Modifier
                .size(240.dp)
                .align(Alignment.Center)
                .offset(y = f1.dp)
                .blur(64.dp)
                .background(TripBlue.copy(alpha = 0.45f), CircleShape)
        )

        // Back card — Tokyo
        TravelDestinationCard(
            city = "Tokyo",
            country = "Japan",
            icon = Icons.Default.Landscape,
            cardColor = Color(0xFF4A148C),
            accentColor = Color(0xFFCE93D8),
            rating = "4.7",
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = 36.dp, y = (28 + f3 * 0.5f).dp)
                .rotate(9f)
        )

        // Middle card — Bali
        TravelDestinationCard(
            city = "Bali",
            country = "Indonesia",
            icon = Icons.Default.BeachAccess,
            cardColor = Color(0xFF00695C),
            accentColor = Color(0xFF80CBC4),
            rating = "4.8",
            modifier = Modifier
                .align(Alignment.Center)
                .offset(x = (-28).dp, y = (14 + f2 * 0.7f).dp)
                .rotate(-7f)
        )

        // Front card — Paris (fully visible, no rotation)
        TravelDestinationCard(
            city = "Paris",
            country = "France",
            icon = Icons.Default.Apartment,
            cardColor = Color(0xFF0D47A1),
            accentColor = accentColor,
            rating = "4.9",
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = f1.dp)
                .scale(pulseScale)
        )

        // Floating badge — trip count
        Surface(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 28.dp, top = 12.dp)
                .offset(y = (-f2 * 0.4f).dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF0B1628).copy(alpha = 0.85f)
        ) {
            Row(
                modifier = Modifier.padding(10.dp, 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(Icons.Default.Explore, null, Modifier.size(14.dp), tint = accentColor)
                Text("500+ trips", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }

        // Floating pin badge — bottom left
        Surface(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 28.dp, bottom = 24.dp)
                .offset(y = f3.dp * 0.3f),
            shape = CircleShape,
            color = TripAccent.copy(alpha = 0.9f)
        ) {
            Icon(
                Icons.Default.LocationOn, null,
                Modifier
                    .size(38.dp)
                    .padding(9.dp),
                tint = Color.White
            )
        }
    }
}

@Composable
private fun TravelDestinationCard(
    city: String,
    country: String,
    icon: ImageVector,
    cardColor: Color,
    accentColor: Color,
    rating: String,
    modifier: Modifier
) {
    Surface(
        modifier = modifier.width(220.dp),
        shape = RoundedCornerShape(20.dp),
        color = cardColor,
        shadowElevation = 12.dp
    ) {
        Column(Modifier.padding(18.dp)) {
            // Icon + rating row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, null, Modifier.size(24.dp), tint = accentColor)
                }
                Spacer(Modifier.weight(1f))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Icon(Icons.Default.Star, null, Modifier.size(14.dp), tint = Color(0xFFFFD54F))
                    Text(rating, fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text = city,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(Icons.Default.LocationOn, null, Modifier.size(12.dp), tint = accentColor.copy(0.8f))
                Text(country, fontSize = 12.sp, color = Color.White.copy(alpha = 0.65f))
            }

            Spacer(Modifier.height(12.dp))

            // Fake scenic tag strip
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                SceneTag("Culture", accentColor)
                SceneTag("Food", accentColor)
                SceneTag("Nature", accentColor)
            }
        }
    }
}

@Composable
private fun SceneTag(label: String, color: Color) {
    Box(
        Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(label, fontSize = 10.sp, color = Color.White.copy(0.75f), fontWeight = FontWeight.Medium)
    }
}

// ── Page 2 — Plan Trips Effortlessly ─────────────────────────────────────────

@Composable
fun PlanIllustration(accentColor: Color) {
    val float = FloatTransition(2600)

    Box(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        // Glow blob
        Box(
            Modifier
                .size(200.dp)
                .blur(60.dp)
                .background(accentColor.copy(alpha = 0.2f), CircleShape)
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = float.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF0C1E35),
            shadowElevation = 16.dp
        ) {
            Column(Modifier.padding(20.dp)) {
                // Trip header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(accentColor.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Flight, null, Modifier.size(24.dp), tint = accentColor)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            "Bali Adventure",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            "7 Days  ·  Jun 12 – 19",
                            fontSize = 12.sp,
                            color = Color.White.copy(0.5f)
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = accentColor.copy(0.15f)
                    ) {
                        Text(
                            "Active",
                            modifier = Modifier.padding(8.dp, 4.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = accentColor
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
                Spacer(Modifier.height(12.dp))

                // Itinerary items
                PlanItineraryItem("Day 1", "Arrival & Check-in", Icons.Default.Hotel, accentColor, done = true)
                PlanItineraryItem("Day 2", "Tanah Lot Temple", Icons.Default.AccountBalance, accentColor, done = true)
                PlanItineraryItem("Day 3", "Kuta Beach & Sunset", Icons.Default.WbSunny, accentColor, done = false)

                Spacer(Modifier.height(12.dp))
                HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
                Spacer(Modifier.height(12.dp))

                // Budget tracker
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccountBalanceWallet, null, Modifier.size(16.dp), tint = accentColor)
                    Spacer(Modifier.width(6.dp))
                    Text("Budget", fontSize = 12.sp, color = Color.White.copy(0.6f), fontWeight = FontWeight.Medium)
                    Spacer(Modifier.weight(1f))
                    Text(
                        "$450 / $800",
                        fontSize = 13.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.height(8.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(7.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(0.08f))
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth(0.5625f)
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(
                                Brush.horizontalGradient(listOf(accentColor.copy(0.7f), accentColor))
                            )
                    )
                }
                Spacer(Modifier.height(4.dp))
                Row {
                    Spacer(Modifier.weight(1f))
                    Text("56% used", fontSize = 10.sp, color = accentColor.copy(0.8f))
                }
            }
        }
    }
}

@Composable
private fun PlanItineraryItem(
    day: String,
    title: String,
    icon: ImageVector,
    accentColor: Color,
    done: Boolean
) {
    Row(
        modifier = Modifier.padding(vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(if (done) accentColor.copy(0.18f) else Color.White.copy(0.07f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (done) Icons.Default.CheckCircle else icon,
                contentDescription = null,
                modifier = Modifier.size(17.dp),
                tint = if (done) accentColor else Color.White.copy(0.4f)
            )
        }
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text(day, fontSize = 10.sp, color = Color.White.copy(0.4f), fontWeight = FontWeight.SemiBold)
            Text(
                title,
                fontSize = 13.sp,
                color = if (done) Color.White else Color.White.copy(0.75f),
                fontWeight = FontWeight.SemiBold
            )
        }
        if (done) {
            Text("Done", fontSize = 11.sp, color = accentColor, fontWeight = FontWeight.SemiBold)
        } else {
            Icon(Icons.Default.ChevronRight, null, Modifier.size(16.dp), tint = Color.White.copy(0.25f))
        }
    }
}

// ── Page 3 — Travel Smarter ───────────────────────────────────────────────────

@Composable
fun TravelSmartIllustration(accentColor: Color) {
    val float = FloatTransition(2800)

    val pulseAnim by rememberInfiniteTransition(label = "pin_pulse").animateFloat(
        0.5f, 1f,
        infiniteRepeatable(tween(1000, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "pin_alpha"
    )

    Box(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        // Glow blob
        Box(
            Modifier
                .size(200.dp)
                .blur(60.dp)
                .background(accentColor.copy(alpha = 0.18f), CircleShape)
        )

        // Map card
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .offset(y = float.dp),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFF0A1628),
            shadowElevation = 16.dp
        ) {
            Box(Modifier.fillMaxSize()) {
                // Map grid + route
                Canvas(Modifier.fillMaxSize()) {
                    val gridSpacing = 32.dp.toPx()
                    val gridColor = Color.White.copy(alpha = 0.055f)

                    var x = 0f
                    while (x < size.width) {
                        drawLine(gridColor, Offset(x, 0f), Offset(x, size.height), 1.5f)
                        x += gridSpacing
                    }
                    var y = 0f
                    while (y < size.height) {
                        drawLine(gridColor, Offset(0f, y), Offset(size.width, y), 1.5f)
                        y += gridSpacing
                    }

                    val startX = size.width * 0.18f
                    val startY = size.height * 0.72f
                    val endX = size.width * 0.80f
                    val endY = size.height * 0.22f

                    // Route shadow
                    val routePath = Path().apply {
                        moveTo(startX, startY)
                        cubicTo(
                            size.width * 0.38f, size.height * 0.25f,
                            size.width * 0.62f, size.height * 0.75f,
                            endX, endY
                        )
                    }
                    drawPath(routePath, accentColor.copy(0.18f), style = Stroke(10.dp.toPx()))
                    drawPath(
                        routePath, accentColor.copy(0.85f),
                        style = Stroke(
                            width = 3.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f))
                        )
                    )

                    // Start dot — green
                    val startColor = Color(0xFF4CAF50)
                    drawCircle(startColor.copy(0.25f), 16.dp.toPx(), Offset(startX, startY))
                    drawCircle(startColor, 7.dp.toPx(), Offset(startX, startY))

                    // End dot — accentColor
                    drawCircle(accentColor.copy(0.25f), 16.dp.toPx(), Offset(endX, endY))
                    drawCircle(accentColor, 7.dp.toPx(), Offset(endX, endY))
                }

                // Offline badge — top right
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp),
                    shape = RoundedCornerShape(10.dp),
                    color = accentColor.copy(0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Icon(Icons.Default.OfflineBolt, null, Modifier.size(14.dp), tint = accentColor)
                        Text("Offline Ready", fontSize = 11.sp, color = accentColor, fontWeight = FontWeight.SemiBold)
                    }
                }

                // Bottom route info strip
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(10.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF0C1E35).copy(0.95f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Route, null, Modifier.size(20.dp), tint = accentColor)
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text(
                                "Ubud → Seminyak",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                "24 km  ·  ~45 min",
                                fontSize = 11.sp,
                                color = Color.White.copy(0.5f)
                            )
                        }
                        Box(
                            Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2E7D32).copy(0.22f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Download, null, Modifier.size(17.dp), tint = Color(0xFF66BB6A))
                        }
                    }
                }
            }
        }

        // Floating stat cards above the map
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-float * 0.3f).dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SmallStatCard(
                icon = Icons.Default.WifiOff,
                label = "No Signal",
                value = "Works Fine",
                accentColor = accentColor
            )
            SmallStatCard(
                icon = Icons.Default.Map,
                label = "Maps Saved",
                value = "3 regions",
                accentColor = accentColor
            )
        }
    }
}

@Composable
private fun SmallStatCard(
    icon: ImageVector,
    label: String,
    value: String,
    accentColor: Color
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color(0xFF0C1E35)
    ) {
        Row(
            modifier = Modifier.padding(10.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, Modifier.size(15.dp), tint = accentColor)
            }
            Column {
                Text(value, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(label, fontSize = 10.sp, color = Color.White.copy(0.45f))
            }
        }
    }
}
