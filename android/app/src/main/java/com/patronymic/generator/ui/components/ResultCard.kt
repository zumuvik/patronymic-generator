package com.patronymic.generator.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextShadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patronymic.generator.ui.theme.GlassWhite
import com.patronymic.generator.ui.theme.NeonBlue
import com.patronymic.generator.ui.theme.NeonPurple
import com.patronymic.generator.ui.theme.glassmorphism
import com.patronymic.generator.ui.theme.neonTextShadow

/**
 * Карточка результата с эффектом letter-by-letter анимации
 * и неоновой подсветкой текста.
 */
@Composable
fun ResultCard(
    sonPatronymic: String,
    daughterPatronymic: String,
    fatherName: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var copied by remember { mutableStateOf(false) }

    // Glow animation
    val infiniteTransition = rememberInfiniteTransition(label = "resultGlow")
    val glowIntensity by infiniteTransition.animateFloat(
        initialValue = 4f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "glowIntensity",
    )

    val showBoth = sonPatronymic != daughterPatronymic

    Column(
        modifier = modifier
            .fillMaxWidth()
            .glassmorphism(alpha = 0.15f, cornerRadius = 24.dp)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (showBoth) {
            // Two cards
            PatronymicLine(
                label = "Отчество для сына",
                patronymic = sonPatronymic,
                glowIntensity = glowIntensity,
                delayMs = 0,
            )
            PatronymicLine(
                label = "Отчество для дочери",
                patronymic = daughterPatronymic,
                glowIntensity = glowIntensity,
                delayMs = 300,
            )
        } else {
            PatronymicLine(
                label = "Отчество",
                patronymic = sonPatronymic,
                glowIntensity = glowIntensity,
                delayMs = 0,
            )
        }

        Text(
            text = "от $fatherName",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 14.sp,
        )

        // Copy button
        Row(
            modifier = Modifier
                .clip(CircleShape)
                .glassmorphism(alpha = 0.1f, cornerRadius = 24.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = {
                        val text = if (showBoth) {
                            "$sonPatronymic / $daughterPatronymic"
                        } else sonPatronymic
                        copyToClipboard(context, text)
                        copied = true
                    },
                )
                .padding(horizontal = 20.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = if (copied) "✓ Скопировано!" else "📋 Копировать",
                color = if (copied) NeonBlue else Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
private fun PatronymicLine(
    label: String,
    patronymic: String,
    glowIntensity: Float,
    delayMs: Int,
) {
    var visibleChars by remember { mutableStateOf(0) }

    LaunchedEffect(patronymic) {
        visibleChars = 0
        patronymic.indices.forEach { i ->
            kotlinx.coroutines.delay(50L + delayMs)
            visibleChars = i + 1
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 13.sp,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = patronymic.take(visibleChars),
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            style = androidx.compose.ui.text.TextStyle(
                textShadow = TextShadow(
                    color = NeonBlue.copy(alpha = 0.6f),
                    blurRadius = glowIntensity,
                ),
            ),
        )
    }
}

private fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboard.setPrimaryClip(ClipData.newPlainText("patronymic", text))
}
