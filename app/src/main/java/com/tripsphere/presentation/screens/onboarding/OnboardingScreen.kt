package com.tripsphere.presentation.screens.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val prefs: OnboardingPreferences
) : ViewModel() {
    fun completeOnboarding() {
        viewModelScope.launch {
            prefs.setOnboardingCompleted()
        }
    }
}

data class OnboardingPage(
    val icon: ImageVector,
    val title: String,
    val subtitle: String,
    val gradientColors: List<Color>
)

val onboardingPages = listOf(
    OnboardingPage(
        icon = Icons.Default.TravelExplore,
        title = "Discover Amazing Places",
        subtitle = "Explore destinations curated for your travel style",
        gradientColors = listOf(Color(0xFF1565C0), Color(0xFF0288D1))
    ),
    OnboardingPage(
        icon = Icons.Default.RequestQuote,
        title = "Plan Trips Effortlessly",
        subtitle = "Create itineraries, manage expenses, and stay organized.",
        gradientColors = listOf(Color(0xFF0D47A1), Color(0xFF1565C0))
    ),
    OnboardingPage(
        icon = Icons.Default.OfflineBolt,
        title = "Travel Smarter",
        subtitle = "Everything you need, even offline. No more roaming anxiety — just pure exploration.",
        gradientColors = listOf(Color(0xFF1A237E), Color(0xFF283593))
    )
)

@Composable
fun OnboardingScreen(
    onGetStarted: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    var currentPage by remember { mutableIntStateOf(0) }
    val page = onboardingPages[currentPage]

    Box(modifier = Modifier.fillMaxSize()) {
        // Background gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(page.gradientColors))
        )

        // Skip button
        TextButton(
            onClick = {
                viewModel.completeOnboarding()
                onGetStarted()
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 48.dp, end = 16.dp)
        ) {
            Text("Skip", color = Color.White.copy(alpha = 0.8f), fontWeight = FontWeight.Medium)
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(100.dp))

            // Illustration area
            AnimatedContent(
                targetState = currentPage,
                transitionSpec = {
                    slideInHorizontally { it } + fadeIn() togetherWith
                            slideOutHorizontally { -it } + fadeOut()
                },
                label = "page_animation"
            ) { page ->
                val p = onboardingPages[page]
                Box(
                    modifier = Modifier
                        .size(220.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(Color.White.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = p.icon,
                        contentDescription = null,
                        modifier = Modifier.size(120.dp),
                        tint = Color.White
                    )
                }
            }

            Spacer(Modifier.height(40.dp))

            // Bottom card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Step indicator
                    Text(
                        text = "Step ${currentPage + 1} of ${onboardingPages.size}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextHint.copy(alpha = 0.7f)
                    )

                    Spacer(Modifier.height(16.dp))

                    AnimatedContent(
                        targetState = currentPage,
                        label = "text_animation"
                    ) { page ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = onboardingPages[page].title,
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = onboardingPages[page].subtitle,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                color = TextSecondary
                            )
                        }
                    }

                    Spacer(Modifier.height(32.dp))

                    // Dot indicators
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        onboardingPages.forEachIndexed { index, _ ->
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(if (index == currentPage) TripBlue else TextHint)
                                    .size(if (index == currentPage) 24.dp else 8.dp, 8.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(32.dp))

                    if (currentPage < onboardingPages.size - 1) {
                        Button(
                            onClick = { currentPage++ },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(27.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TripBlue)
                        ) {
                            Text("Next →", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        }
                    } else {
                        Button(
                            onClick = {
                                viewModel.completeOnboarding()
                                onGetStarted()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(27.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = TripAccent)
                        ) {
                            Text("Get Started", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
