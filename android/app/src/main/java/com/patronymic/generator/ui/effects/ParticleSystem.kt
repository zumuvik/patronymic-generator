package com.patronymic.generator.ui.effects

import androidx.compose.animation.core.withFrameNanos
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.random.Random

private val PARTICLE_COLORS = listOf(
    Color(0xFF00D4FF),
    Color(0xFFFF2D95),
    Color(0xFF7C3AED),
    Color.White,
    Color(0xFF00D4FF).copy(alpha = 0.6f),
    Color(0xFFFF2D95).copy(alpha = 0.5f),
)

// ============== FLOATING PARTICLES ==============

/**
 * Плавающие частицы на фоне. Лёгкие, синусоидальная траектория, мерцание.
 */
@Composable
fun FloatingParticles(
    modifier: Modifier = Modifier,
    particleCount: Int = 40,
) {
    val data = remember { ParticleData(particleCount) }
    var canvasSize by remember { mutableStateOf(Size.Zero) }

    LaunchedEffect(particleCount) {
        var lastFrameTime = 0L
        while (true) {
            withFrameNanos { nanos ->
                if (lastFrameTime > 0L) {
                    val dt = (nanos - lastFrameTime) / 1_000_000_000f
                    data.update(dt, canvasSize)
                }
                lastFrameTime = nanos
            }
        }
    }

    Canvas(modifier) {
        canvasSize = size
        data.draw(this)
    }
}

private class ParticleData(count: Int) {
    private val xs = FloatArray(count)
    private val ys = FloatArray(count)
    private val speeds = FloatArray(count)
    private val wobbleAmps = FloatArray(count)
    private val wobbleFreqs = FloatArray(count)
    private val wobblePhases = FloatArray(count)
    private val baseAlphas = FloatArray(count)
    private val alphaFreqs = FloatArray(count)
    private val alphaPhases = FloatArray(count)
    private val sizes = FloatArray(count)
    private val colorIdx = IntArray(count)

    private var elapsed = 0f

    init { for (i in 0 until count) reset(i, 1000f, 2000f) }

    private fun reset(i: Int, w: Float, h: Float) {
        xs[i] = Random.nextFloat() * w
        ys[i] = h + Random.nextFloat() * 300f
        speeds[i] = 18f + Random.nextFloat() * 55f
        wobbleAmps[i] = 20f + Random.nextFloat() * 70f
        wobbleFreqs[i] = 0.8f + Random.nextFloat() * 2.5f
        wobblePhases[i] = Random.nextFloat() * 6.2832f
        baseAlphas[i] = 0.15f + Random.nextFloat() * 0.6f
        alphaFreqs[i] = 0.6f + Random.nextFloat() * 2f
        alphaPhases[i] = Random.nextFloat() * 6.2832f
        sizes[i] = 1.2f + Random.nextFloat() * 3.5f
        colorIdx[i] = Random.nextInt(PARTICLE_COLORS.size)
    }

    fun update(dt: Float, size: Size) {
        elapsed += dt
        for (i in xs.indices) {
            ys[i] -= speeds[i] * dt
            val phase = wobblePhases[i] + elapsed * wobbleFreqs[i]
            xs[i] += kotlin.math.sin(phase.toDouble()).toFloat() * 0.6f
            if (ys[i] < -20f) reset(i, size.width, size.height)
            if (xs[i] < -60f) xs[i] = size.width + 60f
            if (xs[i] > size.width + 60f) xs[i] = -60f
        }
    }

    fun draw(ds: DrawScope) {
        for (i in xs.indices) {
            val shimmer = 0.5f + 0.5f * kotlin.math.sin(
                (alphaPhases[i] + elapsed * alphaFreqs[i]).toDouble()
            ).toFloat()
            val color = PARTICLE_COLORS[colorIdx[i]]
            ds.drawCircle(
                color = color.copy(alpha = baseAlphas[i] * shimmer),
                radius = sizes[i],
                center = Offset(xs[i], ys[i]),
            )
        }
    }
}

// ============== PARTICLE EXPLOSION ==============

/**
 * Взрыв частиц из точки (originX, originY).
 * Одноразовый эффект ~1.5 секунды после trigger=true.
 * Использует time-based физику: каждому кадру передаётся deltaTime.
 */
@Composable
fun ParticleExplosion(
    trigger: Boolean,
    originX: Float,
    originY: Float,
    modifier: Modifier = Modifier,
) {
    var particles by remember { mutableStateOf<ExplosionParticles?>(null) }

    LaunchedEffect(trigger) {
        if (!trigger) return@LaunchedEffect
        particles = ExplosionParticles(originX, originY)
        var lastFrameTime = 0L

        while (true) {
            withFrameNanos { nanos ->
                if (lastFrameTime == 0L) {
                    lastFrameTime = nanos
                    return@withFrameNanos
                }
                val dt = (nanos - lastFrameTime) / 1_000_000_000f
                lastFrameTime = nanos

                val exploded = particles ?: return@withFrameNanos
                exploded.update(dt)
                if (exploded.elapsed >= 1.8f) {
                    particles = null
                    return@withFrameNanos
                }
            }
            if (particles == null) break
        }
    }

    Canvas(modifier) {
        particles?.draw(this)
    }
}

/**
 * Состояние взрыва: 60 частиц с time-based физикой.
 * Частицы хранятся в массивах примитивов для производительности.
 */
private class ExplosionParticles(
    originX: Float,
    originY: Float,
    count: Int = 60,
) {
    var elapsed = 0f

    // Flat arrays: x, y, vx, vy, size, colorIndex
    private val xs = FloatArray(count) { originX }
    private val ys = FloatArray(count) { originY }
    private val vxs = FloatArray(count)
    private val vys = FloatArray(count)
    private val sizes = FloatArray(count)
    private val colors = IntArray(count)
    private val gravity = 400f   // px/s²

    init {
        for (i in 0 until count) {
            val angle = Random.nextFloat() * 6.2832f
            val speed = 200f + Random.nextFloat() * 600f
            vxs[i] = kotlin.math.cos(angle.toDouble()).toFloat() * speed
            vys[i] = kotlin.math.sin(angle.toDouble()).toFloat() * speed
            sizes[i] = 2f + Random.nextFloat() * 6f
            colors[i] = Random.nextInt(PARTICLE_COLORS.size)
        }
    }

    fun update(dt: Float) {
        elapsed += dt
        val damping = 0.98f.pow((dt * 60f).coerceAtLeast(1f))

        for (i in xs.indices) {
            // Euler integration
            xs[i] += vxs[i] * dt
            vys[i] += gravity * dt
            ys[i] += vys[i] * dt

            // Damping
            vxs[i] *= damping
            vys[i] *= damping
        }
    }

    fun draw(ds: DrawScope) {
        val progress = (elapsed / 1.8f).coerceIn(0f, 1f)
        val globalAlpha = (1f - progress).coerceIn(0f, 1f)

        for (i in xs.indices) {
            val particleAlpha = (1f - progress).coerceIn(0f, 1f)
            val color = PARTICLE_COLORS[colors[i]]
            ds.drawCircle(
                color = color.copy(alpha = particleAlpha * globalAlpha),
                radius = sizes[i] * (1f - progress * 0.7f).coerceAtLeast(0.3f),
                center = Offset(xs[i], ys[i]),
            )
        }
    }
}
