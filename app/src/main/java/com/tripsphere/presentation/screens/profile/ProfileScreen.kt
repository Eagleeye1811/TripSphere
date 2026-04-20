package com.tripsphere.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tripsphere.presentation.ui.theme.*
import com.tripsphere.presentation.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
    onNavigateToMyTrips: () -> Unit,
    onLogout: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Log Out", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.logout()
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Error)
                ) { Text("Log Out") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel") }
            }
        )
    }

    val navBarBottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 100.dp + navBarBottomPadding)
    ) {
        // Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(Brush.verticalGradient(listOf(TripBlueDark, TripBlue)))
            ) {
                // Notification bell
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 48.dp, end = 16.dp)
                ) {
                    Icon(Icons.Default.Notifications, null, tint = Color.White)
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            uiState.user?.name?.firstOrNull()?.toString() ?: "?",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        uiState.user?.name ?: "Traveler",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        uiState.user?.email ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Text(
                            "🌟 Pro Member",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Stats row
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-20).dp),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StatItem(value = "${uiState.totalTrips * 10 + 45}", label = "DAYS")
                    Divider(modifier = Modifier.height(40.dp).width(1.dp), color = TextHint.copy(alpha = 0.3f))
                    StatItem(value = "${uiState.placesVisited}", label = "PLACES")
                    Divider(modifier = Modifier.height(40.dp).width(1.dp), color = TextHint.copy(alpha = 0.3f))
                    StatItem(value = "${uiState.totalTrips}", label = "TRIPS")
                }
            }
        }

        // Menu items
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProfileMenuItem(
                    icon = Icons.Default.CardTravel,
                    title = "My Trips",
                    subtitle = null,
                    onClick = onNavigateToMyTrips
                )
                ProfileMenuItem(
                    icon = Icons.Default.BookmarkBorder,
                    title = "Saved Places",
                    subtitle = null,
                    onClick = { }
                )
                ProfileMenuItem(
                    icon = Icons.Default.WorkspacePremium,
                    title = "TripSphere Premium",
                    subtitle = "MANAGE SUBSCRIPTION",
                    tintColor = Color(0xFFFF8F00),
                    onClick = { }
                )
                ProfileMenuItem(
                    icon = Icons.Default.Settings,
                    title = "Settings",
                    subtitle = null,
                    onClick = { }
                )
                ProfileMenuItem(
                    icon = Icons.Default.HelpOutline,
                    title = "Help & Support",
                    subtitle = null,
                    onClick = { }
                )
                ProfileMenuItem(
                    icon = Icons.Default.Logout,
                    title = "Log Out",
                    subtitle = null,
                    tintColor = Error,
                    onClick = { showLogoutDialog = true }
                )
            }
        }

        item {
            Spacer(Modifier.height(16.dp))
            Text(
                "TripSphere v1.0 (Build 100)",
                style = MaterialTheme.typography.bodySmall,
                color = TextHint,
                modifier = Modifier.fillMaxWidth().wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.ExtraBold, color = TripBlue)
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextSecondary, letterSpacing = 1.sp)
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String?,
    tintColor: Color = TripBlue,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = tintColor.copy(alpha = 0.1f),
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    icon, null,
                    tint = tintColor,
                    modifier = Modifier.padding(8.dp).fillMaxSize()
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                if (subtitle != null) {
                    Text(subtitle, style = MaterialTheme.typography.labelSmall, color = Color(0xFFFF8F00))
                }
            }
            Icon(Icons.Default.ChevronRight, null, tint = TextHint)
        }
    }
}
