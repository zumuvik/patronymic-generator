package com.patronymic.generator.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.patronymic.generator.ui.components.GenderSelector
import com.patronymic.generator.ui.components.GenerateButton
import com.patronymic.generator.ui.components.GlassTextField
import com.patronymic.generator.ui.components.NeonTitle
import com.patronymic.generator.ui.components.ResultCard
import com.patronymic.generator.ui.effects.FloatingParticles
import com.patronymic.generator.ui.effects.ParticleExplosion
import com.patronymic.generator.ui.theme.NeonPink
import com.patronymic.generator.ui.theme.animatedGradientBackground
import com.patronymic.generator.ui.theme.glassmorphism
import com.patronymic.generator.ui.theme.neonGlow

/**
 * Главный экран приложения — вся композиция визуальных слоёв.
 */
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
) {
    val fatherName by viewModel.fatherName.collectAsState()
    val selectedGender by viewModel.selectedGender.collectAsState()
    val result by viewModel.result.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val explosionTrigger by viewModel.explosionTrigger.collectAsState()
    val explosionX by viewModel.explosionOriginX.collectAsState()
    val explosionY by viewModel.explosionOriginY.collectAsState()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .animatedGradientBackground(),
    ) {
        // Слой 1: плавающие частицы
        FloatingParticles(
            modifier = Modifier.fillMaxSize(),
            particleCount = 35,
        )

        // Слой 2: взрыв частиц (по триггеру)
        ParticleExplosion(
            trigger = explosionTrigger,
            originX = explosionX,
            originY = explosionY,
            modifier = Modifier.fillMaxSize(),
        )

        // Слой 3: контент
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(20.dp))

            // Заголовок
            NeonTitle()

            Spacer(Modifier.height(32.dp))

            // Поле ввода
            GlassTextField(
                value = fatherName,
                onValueChange = { viewModel.updateFatherName(it) },
                label = "Введите имя отца...",
                modifier = Modifier.fillMaxWidth(),
                isError = error != null,
            )

            Spacer(Modifier.height(20.dp))

            // Выбор пола
            GenderSelector(
                selectedGender = selectedGender,
                onGenderChange = { viewModel.updateGender(it) },
            )

            Spacer(Modifier.height(24.dp))

            // Кнопка генерации
            GenerateButton(
                onClick = {
                    viewModel.generate()
                },
                enabled = fatherName.isNotBlank() && !isLoading,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(24.dp))

            // Индикатор загрузки
            if (isLoading) {
                Text(
                    text = "Генерируем...",
                    color = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier.glassmorphism(alpha = 0.08f, cornerRadius = 12.dp)
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                )
            }

            // Результат
            AnimatedVisibility(
                visible = result != null,
                enter = fadeIn() + slideInVertically { it / 2 },
                exit = fadeOut(),
            ) {
                result?.let { r ->
                    ResultCard(
                        sonPatronymic = r.sonPatronymic,
                        daughterPatronymic = r.daughterPatronymic,
                        fatherName = r.fatherName,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            // Ошибка
            AnimatedVisibility(
                visible = error != null,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                error?.let { msg ->
                    Text(
                        text = msg,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .glassmorphism(alpha = 0.15f, cornerRadius = 16.dp)
                            .neonGlow(NeonPink, glowRadius = 6.dp)
                            .padding(16.dp),
                    )
                }
            }

            Spacer(Modifier.height(40.dp))
        }
    }
}
