package com.tripsphere.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Brand colours (constant across modes) ────────────────────────────────────
val TripBlue       = Color(0xFF1565C0)
val TripBlueDark   = Color(0xFF0D47A1)
val TripBlueLight  = Color(0xFF2979FF)
val TripAccent     = Color(0xFFF4511E)
val TripAccentLight = Color(0xFFFF7043)

// ── Dark-mode surface palette ─────────────────────────────────────────────────
/** Deepest background — feels like a night sky over the ocean */
val DarkBackground     = Color(0xFF090D18)
/** Card / sheet surface */
val DarkSurface        = Color(0xFF111827)
/** Slightly elevated surface (chips, dialogs, containers) */
val DarkSurfaceVariant = Color(0xFF1C2538)
/** Deep card used for itinerary/trip cards */
val DarkCardSurface    = Color(0xFF172032)

// ── Light-mode surface palette ────────────────────────────────────────────────
val Background     = Color(0xFFF5F7FA)
val Surface        = Color(0xFFFFFFFF)
val SurfaceVariant = Color(0xFFF0F4FF)
val CardBackground = Color(0xFFFFFFFF)

// ── Text colours (light mode reference — prefer MaterialTheme.colorScheme.*) ──
val TextPrimary   = Color(0xFF0D1B3E)
val TextSecondary = Color(0xFF6B7A99)
val TextHint      = Color(0xFFB0BAD3)

// ── Semantic status colours ───────────────────────────────────────────────────
val Success = Color(0xFF2E7D32)
val Warning = Color(0xFFF57F17)
val Error   = Color(0xFFC62828)

// ── Colour schemes ────────────────────────────────────────────────────────────

private val LightColorScheme = lightColorScheme(
    primary             = TripBlue,
    onPrimary           = Color.White,
    primaryContainer    = SurfaceVariant,
    onPrimaryContainer  = TripBlueDark,
    secondary           = TripAccent,
    onSecondary         = Color.White,
    background          = Background,
    onBackground        = TextPrimary,
    surface             = Surface,
    onSurface           = TextPrimary,
    surfaceVariant      = SurfaceVariant,
    onSurfaceVariant    = TextSecondary,
    error               = Error,
    onError             = Color.White,
    outline             = TextHint.copy(alpha = 0.5f)
)

private val DarkColorScheme = darkColorScheme(
    primary             = TripBlueLight,
    onPrimary           = Color.White,
    primaryContainer    = DarkSurfaceVariant,
    onPrimaryContainer  = Color(0xFFBDD4FF),
    secondary           = TripAccentLight,
    onSecondary         = Color.White,
    background          = DarkBackground,
    onBackground        = Color(0xFFE4E8F5),
    surface             = DarkSurface,
    onSurface           = Color(0xFFE4E8F5),
    surfaceVariant      = DarkSurfaceVariant,
    onSurfaceVariant    = Color(0xFF9BAAC4),
    inverseSurface      = Color(0xFFE4E8F5),
    inverseOnSurface    = DarkBackground,
    error               = Color(0xFFEF9A9A),
    onError             = Color(0xFF601410),
    outline             = Color(0xFF3A4560),
    outlineVariant      = Color(0xFF2A3148),
    scrim               = Color(0xFF000000)
)

// ── Theme composable ──────────────────────────────────────────────────────────

@Composable
fun TripSphereTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = TripSphereTypography,
        content     = content
    )
}
