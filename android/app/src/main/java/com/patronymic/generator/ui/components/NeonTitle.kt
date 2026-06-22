package com.patronymic.generator.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextShadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patronymic.generator.ui.theme.NeonBlue
import com.patronymic.generator.ui.theme.NeonPurple

/**
 * Неоновый заголовок с мерцающей тенью.
 * "Генератор" — одним стилем, "Отчеств" — другим.
 */
@Composable
fun NeonTitle(
    text: String = "Генератор Отчеств",
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "titleGlow")
    val glowRadius by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = 16f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "glowRadius",
    )
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "glowAlpha",
    )

    Column(
        modifier = modifier.fillMaxWidth().padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Генератор",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            style = androidx.compose.ui.text.TextStyle(
                textShadow = TextShadow(
                    color = NeonBlue.copy(alpha = glowAlpha),
                    blurRadius = glowRadius,
                ),
            ),
        )
        Text(
            text = "Отчеств",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = NeonPurple,
            textAlign = TextAlign.Center,
            style = androidx.compose.ui.text.TextStyle(
                textShadow = TextShadow(
                    color = NeonPurple.copy(alpha = glowAlpha * 0.7f),
                    blurRadius = glowRadius * 0.7f,
                ),
            ),
        )
    }
}
