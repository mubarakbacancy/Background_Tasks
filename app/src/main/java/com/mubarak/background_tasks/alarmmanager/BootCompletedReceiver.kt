package com.mubarak.background_tasks.alarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.util.Log
import android.widget.Toast

class BootCompletedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Schedule the alarm 5 seconds after boot completed
            Log.e("BootCompletedReceiver", "onReceive: boot completed", )
            scheduleNotification(context)
        }
    }

    private fun scheduleNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(context, AlarmManagerActivity::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE)

        val triggerAtMillis = SystemClock.elapsedRealtime() + 7000 // 5 seconds

        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtMillis, pendingIntent)

        Toast.makeText(context, "Notification scheduled after 10 seconds", Toast.LENGTH_SHORT).show()

        val notificationHelper = NotificationHelper(context)
        notificationHelper.showNotification("Boot Device", "Device boot successfully")
    }
}
