package com.tripsphere.presentation.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tripsphere.presentation.navigation.Screen
import com.tripsphere.presentation.ui.theme.TripBlue
import com.tripsphere.presentation.ui.theme.TextHint

data class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home,      Icons.Default.Home,       "Home"),
    BottomNavItem(Screen.Explore,   Icons.Default.Explore,    "Explore"),
    BottomNavItem(Screen.MyTrips,   Icons.Default.CardTravel, "My Trips"),
    BottomNavItem(Screen.HotelsTab, Icons.Default.Hotel,      "Hotels")
)

@Composable
fun TripSphereBottomBar(
    currentRoute: String?,
    onNavigate: (Screen) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation    = 16.dp,
                    shape        = RoundedCornerShape(28.dp),
                    spotColor    = TripBlue.copy(alpha = 0.12f),
                    ambientColor = Color.Black.copy(alpha = 0.06f)
                )
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(28.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication        = null,
                    onClick           = {}
                )
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            bottomNavItems.forEach { item ->
                NavItem(
                    item       = item,
                    isSelected = currentRoute == item.screen.route,
                    onClick    = { onNavigate(item.screen) }
                )
            }
        }
    }
}

@Composable
private fun NavItem(
    item: BottomNavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val iconColor by animateColorAsState(
        targetValue   = if (isSelected) TripBlue else TextHint,
        animationSpec = tween(200),
        label         = "nav_color"
    )

    Column(
        modifier            = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication        = null,
                onClick           = onClick
            )
            .padding(horizontal = 14.dp, vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Icon(
            imageVector        = item.icon,
            contentDescription = item.label,
            tint               = iconColor,
            modifier           = Modifier.size(22.dp)
        )
        Text(
            text       = item.label,
            fontSize   = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color      = iconColor
        )
        // Tiny dot indicator
        Box(
            modifier = Modifier
                .size(4.dp)
                .clip(CircleShape)
                .background(if (isSelected) TripBlue else Color.Transparent)
        )
    }
}
