package com.patronymic.generator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// === Цветовая палитра "Неон во тьме" ===
val DeepDark = Color(0xFF0D0B1E)
val MediumDark = Color(0xFF1A0A3E)
val SurfaceDark = Color(0xFF2A1A5E)
val NeonBlue = Color(0xFF00D4FF)
val NeonPink = Color(0xFFFF2D95)
val NeonPurple = Color(0xFF7C3AED)
val GlassWhite = Color.White.copy(alpha = 0.12f)
val GlassBorder = Color.White.copy(alpha = 0.20f)

private val DarkColorScheme = darkColorScheme(
    primary = NeonBlue,
    secondary = NeonPink,
    tertiary = NeonPurple,
    background = DeepDark,
    surface = MediumDark,
    surfaceVariant = SurfaceDark,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    outline = GlassBorder,
)

@Composable
fun PatronymicTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = androidx.compose.material3.Typography(
            displayLarge = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 42.sp,
                letterSpacing = (-1).sp,
            ),
            displayMedium = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
            ),
            headlineLarge = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
            ),
            bodyLarge = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
            ),
            bodyMedium = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
            ),
            labelLarge = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
            ),
        ),
        content = content,
    )
}
