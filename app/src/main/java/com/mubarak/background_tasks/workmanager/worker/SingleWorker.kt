package com.mubarak.background_tasks.workmanager.worker

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mubarak.background_tasks.workmanager.WorkManagerActivity
import com.mubarak.background_tasks.workmanager.WorkManagerActivity.Companion.KEY_WORK
import java.text.SimpleDateFormat
import java.util.Date

class SingleWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    override fun doWork(): Result {

        return try {

            val counter = inputData.getInt(WorkManagerActivity.KEY_VALUE, 0)

            for (i in 0..counter) {
                Log.d("SingleWorker", "doWork: Single Work Running $i")
            }

            for (i in 0..10) {
                Log.d("SingleWorker", "doWork: Single Work Running2 $i")
            }

            //output Data
            val time = SimpleDateFormat("dd/MM/yyyy hh:mm:ss")
            val currentDate = time.format(Date())
            val outPutDate = Data.Builder()
                .putString(KEY_WORK, "Finished time $currentDate")
                .build()

            Result.success(outPutDate)
        } catch (e: Exception) {
            Log.e("SingleWorker", "doWork: error $e")
            Result.failure()
        }

    }
}