package com.patronymic.generator.ui.components

import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patronymic.generator.ui.theme.NeonBlue
import com.patronymic.generator.ui.theme.NeonPink
import com.patronymic.generator.ui.theme.neonGlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Градиентная кнопка с breathing glow, spring animation и haptic feedback.
 */
@Composable
fun GenerateButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    // Breathing animation
    val infiniteTransition: InfiniteTransition = rememberInfiniteTransition(label = "breathing")
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "breathScale",
    )

    val glow by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "glow",
    )

    val shape = RoundedCornerShape(32.dp)
    val gradient = Brush.horizontalGradient(
        colors = listOf(NeonBlue, NeonPink)
    )

    val finalScale = if (enabled) breathScale else 1f
    val finalAlpha = if (enabled) 1f else 0.4f

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .scale(finalScale)
            .then(
                if (enabled && glow > 0.8f) {
                    Modifier.neonGlow(NeonBlue, glowRadius = (8 * glow).dp)
                } else Modifier
            )
            .shadow(16.dp, shape, ambientColor = NeonBlue.copy(alpha = 0.3f * glow))
            .clip(shape)
            .background(gradient)
            .then(
                if (enabled) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            scope.launch {
                                delay(50)
                                onClick()
                            }
                        },
                    )
                } else Modifier
            )
            .height(60.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Сгенерировать ✦",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
        )
    }
}
