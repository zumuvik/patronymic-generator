package com.patronymic.generator.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patronymic.generator.data.Gender
import com.patronymic.generator.ui.theme.GlassWhite
import com.patronymic.generator.ui.theme.NeonBlue
import com.patronymic.generator.ui.theme.NeonPink
import com.patronymic.generator.ui.theme.NeonPurple
import com.patronymic.generator.ui.theme.glassmorphism
import com.patronymic.generator.ui.theme.neonGlow

/**
 * Переключатель пола в стиле глассморфизм.
 * Три варианта: Сын ♂ / Дочь ♀ / Оба ♂♀
 */
@Composable
fun GenderSelector(
    selectedGender: Gender,
    onGenderChange: (Gender) -> Unit,
    modifier: Modifier = Modifier,
) {
    val haptic = LocalHapticFeedback.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Gender.entries.forEach { gender ->
            val isSelected = gender == selectedGender
            val glowColor = when (gender) {
                Gender.SON -> NeonBlue
                Gender.DAUGHTER -> NeonPink
                Gender.BOTH -> NeonPurple
            }

            val bgAlpha by animateColorAsState(
                targetValue = if (isSelected) Color.White.copy(alpha = 0.2f)
                else Color.White.copy(alpha = 0.06f),
                animationSpec = spring(dampingRatio = 0.6f, stiffness = 300f),
                label = "genderBg",
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .glassmorphism(
                        alpha = if (isSelected) 0.2f else 0.08f,
                        cornerRadius = 16.dp,
                    )
                    .then(
                        if (isSelected) Modifier.neonGlow(glowColor, glowRadius = 6.dp)
                        else Modifier
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            onGenderChange(gender)
                        },
                    )
                    .padding(vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = when (gender) {
                        Gender.SON -> "♂"
                        Gender.DAUGHTER -> "♀"
                        Gender.BOTH -> "♂♀"
                    },
                    fontSize = 24.sp,
                    color = if (isSelected) glowColor else Color.White.copy(alpha = 0.5f),
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = gender.displayName,
                    fontSize = 13.sp,
                    color = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f),
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                )
            }
        }
    }
}
