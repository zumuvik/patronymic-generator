package com.patronymic.generator.ui.theme

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextShadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Glassmorphism эффект — полупрозрачная поверхность с размытием заднего плана.
 */
fun Modifier.glassmorphism(
    alpha: Float = 0.12f,
    blurRadius: Int = 20,
    cornerRadius: Dp = 24.dp,
    borderAlpha: Float = 0.20f,
): Modifier {
    val shape = RoundedCornerShape(cornerRadius)
    return this
        .clip(shape)
        .background(Color.White.copy(alpha = alpha), shape)
        .border(1.dp, Color.White.copy(alpha = borderAlpha), shape)
        .let { modifier ->
            if (Build.VERSION.SDK_INT >= 31) {
                modifier.graphicsLayer {
                    renderEffect = RenderEffect.createBlurEffect(
                        blurRadius.toFloat(),
                        blurRadius.toFloat(),
                        Shader.TileMode.CLAMP
                    ).asRenderEffect()
                }
            } else modifier
        }
}

/**
 * Неоновое свечение через многослойные тени.
 */
fun Modifier.neonGlow(
    color: Color = NeonBlue,
    glowRadius: Dp = 8.dp,
): Modifier = this
    .shadow(glowRadius, RoundedCornerShape(24.dp), spotColor = color, ambientColor = color)
    .shadow(glowRadius * 0.5f, RoundedCornerShape(24.dp), spotColor = color, ambientColor = color)
    .shadow(glowRadius * 0.25f, RoundedCornerShape(24.dp), spotColor = color, ambientColor = color)

/**
 * Анимированный градиентный фон с бесконечно движущимися цветами.
 * Использует LinearGradient с анимированным offset'ом.
 */
@Composable
fun Modifier.animatedGradientBackground(
    colors: List<Color> = listOf(
        NeonBlue.copy(alpha = 0.06f),
        NeonPurple.copy(alpha = 0.04f),
        DeepDark,
    ),
    animationDuration: Int = 8000,
): Modifier {
    val infiniteTransition = rememberInfiniteTransition(label = "bgGradient")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(animationDuration, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "gradientProgress",
    )

    return this.background(
        Brush.linearGradient(
            colors = colors,
            start = Offset(progress * 0.5f, 0f),
            end = Offset(1f - progress * 0.5f, 1f),
        )
    )
}

/**
 * Тень текста для неонового эффекта.
 */
fun neonTextShadow(color: Color = NeonBlue, blurRadius: Float = 8f) = TextShadow(
    color = color.copy(alpha = 0.8f),
    blurRadius = blurRadius,
)
