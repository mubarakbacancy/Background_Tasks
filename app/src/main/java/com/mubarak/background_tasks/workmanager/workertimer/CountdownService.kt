package com.mubarak.background_tasks.workmanager.workertimer

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.WorkManager
import com.mubarak.background_tasks.R
import com.mubarak.background_tasks.workmanager.WorkManagerActivity

class CountdownService : Service() {

    private var countDownTimer: CountDownTimer? = null
    private val TIMER_DURATION: Long = 1 * 60 * 1000
    var remainingTime: Long = 0 // Store remaining time when paused


    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {

            remainingTime = intent.getLongExtra("countdownTimers", 0)

            createNotificationChannel()
            val notification = createNotificationBuilder(formatTime(remainingTime)).build()
            startForeground(1, notification)
            startCountdown()
        }


        return START_STICKY
    }

    private fun createNotificationBuilder(contentText: String): NotificationCompat.Builder {
        val notificationIntent = Intent(this, WorkManagerActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )


        val notificationBuilder = NotificationCompat.Builder(this, "MyChannelId")
            .setSmallIcon(R.drawable.ic_launcher_foreground).setContentTitle("Countdown Service")
            .setContentText("Time remaining: " + contentText).setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT).setOngoing(true)

        return notificationBuilder

    }

    private fun startCountdown() {

        countDownTimer = object : CountDownTimer(remainingTime, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                Log.e(TAG, "onTick: ${formatTime(millisUntilFinished)}")
                remainingTime = millisUntilFinished

                //update time on notification
                updateNotification(formatTime(millisUntilFinished))

                // Broadcast remaining time to MainActivity
                broadcastTimerData(formatTime(millisUntilFinished))
            }

            override fun onFinish() {
                Toast.makeText(applicationContext, "Countdown finished", Toast.LENGTH_SHORT).show()
                stopSelf()
                Log.e(TAG, "onFinish: Countdown finished")
                WorkManager.getInstance(applicationContext).cancelAllWork()
//                stopForeground(true)
                stopForeground(STOP_FOREGROUND_DETACH)
            }
        }

        countDownTimer?.start()
    }

    private fun createNotificationChannel() {
        //create a notification chanel for create a notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "MyChannelId", "Mubarak", NotificationManager.IMPORTANCE_LOW
            )

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun formatTime(millisTime: Long): String {
        val second = (millisTime / 1000) % 60
        val minutes = (millisTime / (1000 * 60)) % 60
        return String.format("%02d:%02d", minutes, second)
    }

    fun updateNotification(millisTime: String) {

        val notificationBuild = createNotificationBuilder(millisTime).build()

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        NotificationManagerCompat.from(this).notify(1, notificationBuild)
    }

    fun broadcastTimerData(time: String) {
        val intent = Intent("countdown-timer")
        intent.putExtra("remaining_time", time)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }

}