package com.patronymic.generator.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Перезапускает уведомления после перезагрузки устройства.
 */
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            NotificationScheduler.scheduleRepeating(context)
        }
    }
}
