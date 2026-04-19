package com.tripsphere.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tripsphere.presentation.navigation.Screen
import com.tripsphere.presentation.ui.theme.TripBlue
import com.tripsphere.presentation.ui.theme.TextSecondary

data class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home,      Icons.Default.Home,       "Home"),
    BottomNavItem(Screen.Explore,   Icons.Default.Explore,    "Explore"),
    BottomNavItem(Screen.MyTrips,   Icons.Default.CardTravel, "My Trips"),
    BottomNavItem(Screen.HotelsTab, Icons.Default.Hotel,      "Hotels"),
    BottomNavItem(Screen.Profile,   Icons.Default.Person,     "Profile")
)

@Composable
fun TripSphereBottomBar(
    currentRoute: String?,
    onNavigate: (Screen) -> Unit
) {
    // Floating Premium Bottom Bar
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 20.dp, 
                    shape = RoundedCornerShape(32.dp), 
                    spotColor = TripBlue.copy(alpha = 0.15f),
                    ambientColor = TripBlue.copy(alpha = 0.05f) 
                )
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(32.dp)
                )
                .padding(horizontal = 8.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            bottomNavItems.forEach { item ->
                val isSelected = currentRoute == item.screen.route
                BottomNavPill(
                    item = item,
                    isSelected = isSelected,
                    onClick = { onNavigate(item.screen) }
                )
            }
        }
    }
}

@Composable
private fun BottomNavPill(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    
    val iconTint by animateColorAsState(
        targetValue = if (isSelected) TripBlue else TextSecondary,
        animationSpec = tween(durationMillis = 300),
        label = "icon_tint_anim"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .background(
                if (isSelected) TripBlue.copy(alpha = 0.12f) else Color.Transparent
            )
            .padding(horizontal = if (isSelected) 20.dp else 12.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
            
            AnimatedVisibility(
                visible = isSelected,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = item.label,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = TripBlue
                )
            }
        }
    }
}
