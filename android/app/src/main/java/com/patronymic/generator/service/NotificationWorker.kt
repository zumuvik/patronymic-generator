package com.patronymic.generator.service

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.patronymic.generator.MainActivity
import com.patronymic.generator.PatronymicApp

/**
 * WorkManager Worker, который отправляет "агрессивное" уведомление.
 * Использует кликбейтные хуки для привлечения внимания.
 */
class NotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return Result.retry()
            }
        }

        val message = CLICKBAIT_MESSAGES.random()

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(
            applicationContext,
            PatronymicApp.NOTIFICATION_CHANNEL_ID
        )
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle("⚠️ Генератор Отчеств")
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("$message\n\nЖми — сгенерируем отчество за секунду!")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 200, 100, 300))
            .setContentIntent(pendingIntent)
            .setDefaults(NotificationCompat.DEFAULT_LIGHTS or NotificationCompat.DEFAULT_SOUND)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(
            System.currentTimeMillis().toInt(),
            notification
        )

        return Result.success()
    }

    companion object {
        private val CLICKBAIT_MESSAGES = listOf(
            "Эй! Ты чекал, чем там занимается Александровна?",
            "Твой будущий сын заждался отчества!",
            "Быстро зайди в приложение и посмотри новые отчества!",
            "Никитична уже заскучала без тебя...",
            "Пора бы уже узнать, какое отчество у твоих будущих детей!",
            "Ильич требует твоего внимания! Немедленно!",
            "Ты уже 3 часа не генерировал отчества. Это нормально?",
            "Петровна прислала открытку: «Заходи, отчество новое есть!»",
            "Срочно! Владимирович устал ждать!",
            "Без твоего отчества мир не полон. Заходи!",
            "Чё, забыл про отчества? А мы помним!",
            "Генератор скучает... Все имена превратились в тыкву.",
            "Оповещение с небес: «Сгенерируй отчество, смертный!»",
            "Докладная записка: Павлович волнуется.",
            "Экстренное предупреждение: отчество не выбрано!",
            "Твоя родословная плачет. Утешь её отчеством!",
            "Александрович одобряет это уведомление.",
            "Не заставляй Ильиничну нервничать — открой приложение!",
            "Опять сидишь без отчества? Есть разговор...",
            "Фомич прислал телеграмму: «Жду генерации!»",
        )
    }
}
