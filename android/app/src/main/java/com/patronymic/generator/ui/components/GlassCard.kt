package com.patronymic.generator.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.patronymic.generator.ui.theme.NeonBlue
import com.patronymic.generator.ui.theme.glassmorphism
import com.patronymic.generator.ui.theme.neonGlow

/**
 * Универсальная стеклянная карточка с анимацией появления.
 * Появляется с Spring-анимацией (scale 0.8→1.0, alpha 0→1).
 * При glowing=true — неоновая подсветка рамки.
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    title: String = "",
    subtitle: String = "",
    glowing: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        visible = true
    }

    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.8f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 300f),
        label = "cardScale",
    )

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 200f),
        label = "cardAlpha",
    )

    val finalModifier = modifier
        .fillMaxWidth()
        .scale(scale)
        .alpha(alpha)
        .glassmorphism()
        .then(
            if (glowing) Modifier.neonGlow(NeonBlue, glowRadius = 12.dp)
            else Modifier
        )
        .padding(20.dp)

    Column(
        modifier = finalModifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (title.isNotEmpty()) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
        }
        if (subtitle.isNotEmpty()) {
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.7f),
            )
        }
        content()
    }
}
