package com.mubarak.background_tasks.jobIntentservice

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService

class MyJobIntentService : JobIntentService() {

    companion object {
        const val JOB_ID = 1000

        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, MyJobIntentService::class.java, JOB_ID, work)
        }
    }

    override fun onHandleWork(intent: Intent) {
        // This method is called on a background thread
        Log.d("MyJobIntentService", "Background task started")

        // Perform your background task here
        performBackgroundTask()

        Log.d("MyJobIntentService", "Background task completed")
    }

    private fun performBackgroundTask() {
        // Perform your background task here
        // For example, you can make a network call, perform file operations, etc.

        for (i in 1..10) {
            Log.e("TAG", "performBackgroundTask: i $i")
        }
    }
}
