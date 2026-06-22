package com.patronymic.generator.service

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/**
 * Планировщик навязчивых уведомлений.
 * Запускает PeriodicWorkRequest каждые 1-3 часа (рандомный интервал).
 */
object NotificationScheduler {

    private const val WORK_NAME = "aggressive_notifications"

    /**
     * Запускает периодические уведомления с интервалом ~3 часа.
     * Используется KEEP, чтобы при перезапуске не дублировать.
     */
    fun scheduleRepeating(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            3, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    /**
     * Немедленно отправляет одно уведомление (без планирования).
     */
    fun sendImmediate(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            3, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(context).enqueue(workRequest)
    }

    /**
     * Останавливает все уведомления.
     */
    fun cancelAll(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }
}
