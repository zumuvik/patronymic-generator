package com.patronymic.generator.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patronymic.generator.ui.theme.GlassBorder
import com.patronymic.generator.ui.theme.NeonBlue
import com.patronymic.generator.ui.theme.NeonPink
import com.patronymic.generator.ui.theme.glassmorphism
import com.patronymic.generator.ui.theme.neonGlow

/**
 * Стеклянное поле ввода с Spring-анимациями на фокус,
 * неоновым курсором и HapticFeedback.
 */
@Composable
fun GlassTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
) {
    var isFocused by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    val borderGlow by animateFloatAsState(
        targetValue = if (isFocused) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 300f),
        label = "borderGlow",
    )

    val glowModifier = if (isFocused && !isError) {
        Modifier.neonGlow(NeonBlue, glowRadius = 4.dp)
    } else if (isError) {
        Modifier.neonGlow(NeonPink, glowRadius = 4.dp)
    } else {
        Modifier
    }

    val borderColor = when {
        isError -> NeonPink.copy(alpha = 0.8f)
        isFocused -> NeonBlue.copy(alpha = 0.5f + 0.3f * borderGlow)
        else -> GlassBorder
    }

    BasicTextField(
        value = value,
        onValueChange = { input ->
            val filtered = input.filter { c ->
                c in 'А'..'Я' || c in 'а'..'я' || c == 'ё' || c == 'Ё'
            }.take(20)
            onValueChange(filtered)
        },
        modifier = modifier
            .fillMaxWidth()
            .glowModifier
            .glassmorphism(
                alpha = if (isFocused) 0.18f else 0.12f,
                borderAlpha = 0f,
            )
            .border(
                width = 1.5.dp,
                shape = RoundedCornerShape(24.dp),
                brush = if (isFocused || isError) {
                    Brush.linearGradient(
                        colors = listOf(borderColor, borderColor.copy(alpha = 0.3f))
                    )
                } else {
                    Brush.linearGradient(
                        colors = listOf(GlassBorder, GlassBorder.copy(alpha = 0.1f))
                    )
                },
            )
            .padding(horizontal = 20.dp, vertical = 18.dp)
            .onFocusChanged { state ->
                if (state.isFocused != isFocused) {
                    isFocused = state.isFocused
                    if (isFocused) {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    }
                }
            },
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 18.sp,
        ),
        cursorBrush = SolidColor(NeonBlue),
        singleLine = true,
        decorationBox = { innerTextField ->
            if (value.isEmpty()) {
                Text(
                    text = label,
                    style = TextStyle(
                        color = Color.White.copy(alpha = if (isFocused) 0.3f else 0.5f),
                        fontSize = 16.sp,
                    ),
                )
            }
            innerTextField()
        },
    )
}
