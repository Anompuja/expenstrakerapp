package com.example.expensetracker.presentation.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Define your light color scheme using the colors from Color.kt
private val LightColorScheme = lightColorScheme(
    primary = PrimaryGreen,
    secondary = AccentRed,
    tertiary = Pink40, // Example
    background = BackgroundLight, // Or Color.White if your background image doesn't cover all
    surface = Color(0xFFFDFDFD), // Slightly off-white for cards, dialogs
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    primaryContainer = PurpleGrey80, // Example, adjust
    onPrimaryContainer = PurpleGrey40 // Example, adjust
    // You can define more colors here
)

// Define your dark color scheme (optional, but good practice)
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryDarkGreen,
    secondary = AccentRed, // Keep accent consistent or choose a dark theme variant
    tertiary = Pink80, // Example
    background = Color(0xFF1C1B1F), // A common dark background color
    surface = Color(0xFF2C2B2F), // Darker surface for cards
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color(0xFFE6E1E5), // Light text on dark background
    onSurface = Color(0xFFE6E1E5),
    primaryContainer = PurpleGrey40, // Example, adjust
    onPrimaryContainer = PurpleGrey80 // Example, adjust
)

@Composable
fun ExpenseTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Set to true if you want to use Monet colors on A12+
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

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb() // Or Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme // Adjust based on status bar color
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography, // From Type.kt
        content = content
    )
}