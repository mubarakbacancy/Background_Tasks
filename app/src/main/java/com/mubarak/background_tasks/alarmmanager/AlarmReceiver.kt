package com.mubarak.background_tasks.alarmmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.e("AlarmReceiver", "onReceive: successfully received", )
        val notificationHelper = NotificationHelper(context)
        notificationHelper.showNotification("Alarm", "This is your notification")
    }
}
