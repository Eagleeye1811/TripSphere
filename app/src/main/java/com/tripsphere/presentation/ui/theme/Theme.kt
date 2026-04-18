package com.tripsphere.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Primary palette
val TripBlue = Color(0xFF1565C0)
val TripBlueDark = Color(0xFF0D47A1)
val TripBlueLight = Color(0xFF1976D2)
val TripAccent = Color(0xFFF4511E)
val TripAccentLight = Color(0xFFFF7043)

// Surface palette
val Background = Color(0xFFF5F7FA)
val Surface = Color(0xFFFFFFFF)
val SurfaceVariant = Color(0xFFF0F4FF)
val DarkBackground = Color(0xFF0A0E1A)
val DarkSurface = Color(0xFF141828)
val DarkSurfaceVariant = Color(0xFF1E2438)

val CardBackground = Color(0xFFFFFFFF)
val CardBackgroundDark = Color(0xFF1A2035)

// Text
val TextPrimary = Color(0xFF0D1B3E)
val TextSecondary = Color(0xFF6B7A99)
val TextHint = Color(0xFFB0BAD3)

// Status
val Success = Color(0xFF2E7D32)
val Warning = Color(0xFFF57F17)
val Error = Color(0xFFC62828)

private val LightColorScheme = lightColorScheme(
    primary = TripBlue,
    onPrimary = Color.White,
    primaryContainer = SurfaceVariant,
    onPrimaryContainer = TripBlueDark,
    secondary = TripAccent,
    onSecondary = Color.White,
    background = Background,
    onBackground = TextPrimary,
    surface = Surface,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = TextSecondary,
    error = Error,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = TripBlueLight,
    onPrimary = Color.White,
    primaryContainer = DarkSurfaceVariant,
    onPrimaryContainer = Color(0xFFBBD4FF),
    secondary = TripAccentLight,
    onSecondary = Color.White,
    background = DarkBackground,
    onBackground = Color(0xFFE8EAF6),
    surface = DarkSurface,
    onSurface = Color(0xFFE8EAF6),
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = Color(0xFFADB5D0),
    error = Color(0xFFEF9A9A),
    onError = Color(0xFF601410)
)

@Composable
fun TripSphereTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = TripSphereTypography,
        content = content
    )
}
