package com.patronymic.generator

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.work.Configuration
import androidx.work.WorkManager
import com.patronymic.generator.service.NotificationScheduler

/**
 * Application класс. Инициализирует WorkManager и NotificationChannels.
 */
class PatronymicApp : Application(), Configuration.Provider {

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        scheduleAggressiveNotifications()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Уведомления генератора",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Агрессивные напоминания: «Эй, сгенерируй отчество!»"
                enableVibration(true)
                enableLights(true)
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun scheduleAggressiveNotifications() {
        NotificationScheduler.scheduleRepeating(this)
    }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "patronymic_notifications"
    }
}
