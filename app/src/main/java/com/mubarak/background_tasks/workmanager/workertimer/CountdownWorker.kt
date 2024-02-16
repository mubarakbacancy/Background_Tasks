package com.mubarak.background_tasks.workmanager.workertimer

import android.content.Context
import android.content.Intent
import androidx.work.Worker
import androidx.work.WorkerParameters

class CountdownWorker(context: Context, workerParams: WorkerParameters) : Worker(
    context,
    workerParams
) {
    override fun doWork(): Result {
        return try {
            val inputData = inputData.getLong("countdownTimer", 0)
            val intent = Intent(applicationContext, CountdownService::class.java)
            intent.putExtra("countdownTimers", inputData)
            applicationContext.startService(intent)

            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}