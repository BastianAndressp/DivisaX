package com.example.divisax.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = BrandPrimary,
    onPrimary = NightSky,
    secondary = BrandSecondary,
    onSecondary = TextPrimaryDark,
    tertiary = BrandTertiary,
    background = NightSky,
    onBackground = TextPrimaryDark,
    surface = SoftSurfaceDark,
    onSurface = TextPrimaryDark,
    surfaceVariant = DeepSpace,
    onSurfaceVariant = TextSecondaryDark,
    outline = TextSecondaryDark.copy(alpha = 0.4f)
)

private val LightColorScheme = lightColorScheme(
    primary = BrandSecondary,
    onPrimary = TextPrimaryDark,
    secondary = BrandPrimary,
    onSecondary = DayOnSurface,
    tertiary = BrandTertiary,
    background = DayBackground,
    onBackground = DayOnSurface,
    surface = DaySurface,
    onSurface = DayOnSurface,
    surfaceVariant = DaySurfaceVariant,
    onSurfaceVariant = DayOnSurface.copy(alpha = 0.7f),
    outline = DayOnSurface.copy(alpha = 0.35f)
)

@Composable
fun DivisaXTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}