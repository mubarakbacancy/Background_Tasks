package com.mubarak.background_tasks.workmanager

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.mubarak.background_tasks.databinding.ActivityWorkManagerBinding
import com.mubarak.background_tasks.workmanager.worker.DoubleWorker
import com.mubarak.background_tasks.workmanager.worker.SingleWorker
import com.mubarak.background_tasks.workmanager.workertimer.CountdownWorker
import java.util.concurrent.TimeUnit

class WorkManagerActivity : AppCompatActivity() {
    companion object {
        const val KEY_VALUE = "key_value"
        const val KEY_WORK = "KEY_WORK"
    }

    // Declare the broadcast receiver as a property of your class so that it can be accessed later.
    private val timerBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            binding.tvTimer.text = intent?.getStringExtra("remaining_time")
        }
    }

    private lateinit var binding: ActivityWorkManagerBinding

    private val TIMER_DURATION: Long = 1 * 60 * 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWorkManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.apply {

            btnSingleWork.setOnClickListener {
                val constraints =
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED).build()

                val workRequest =
                    OneTimeWorkRequestBuilder<SingleWorker>()
                        .setInitialDelay(3, TimeUnit.SECONDS)
                        .setConstraints(constraints).build()
                WorkManager.getInstance(applicationContext).enqueue(workRequest)
            }

            //Chaining work
            btnParallelWorkers.setOnClickListener {

                val singleWorker = OneTimeWorkRequestBuilder<SingleWorker>().build()
                val doubleWorker = OneTimeWorkRequestBuilder<DoubleWorker>().build()

                val parallelWorker = mutableListOf<OneTimeWorkRequest>()
                parallelWorker.add(singleWorker)
                parallelWorker.add(doubleWorker)

                WorkManager.getInstance(applicationContext)
                    .beginWith(parallelWorker)
                    .enqueue()
                /*
                                WorkManager.getInstance(applicationContext)
                                    .beginWith(singleWorker)
                                    .then(doubleWorker)
                                    .then(3)

                                    .enqueue()*/

            }

            btnWorkLiveData.setOnClickListener {

                val inputData = Data.Builder()
                    .putInt(KEY_VALUE, 9000)
                    .build()

                val singleWorker = OneTimeWorkRequestBuilder<SingleWorker>()
                    .setInputData(inputData)
                    .setInitialDelay(5, TimeUnit.SECONDS)
                    .build()

                WorkManager.getInstance(applicationContext).beginWith(singleWorker).enqueue()

                WorkManager.getInstance(applicationContext).getWorkInfoByIdLiveData(singleWorker.id)
                    .observe(this@WorkManagerActivity) { workInfo ->
                        binding.tvStatus.text = workInfo.state.name

                        if (workInfo.state.isFinished) {
                            val data = workInfo.outputData.getString(KEY_WORK)
                            Toast.makeText(applicationContext, data.toString(), Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
            }


            btnPeriodicWork.setOnClickListener {

                val periodicWorker = PeriodicWorkRequestBuilder<DoubleWorker>(
                    1, // Minimum allowed interval is 15 minutes
                    TimeUnit.HOURS
                ).build()

                WorkManager.getInstance(applicationContext).enqueue(periodicWorker)
            }

            btnStartTimer.setOnClickListener {
                val data = Data.Builder()
                    .putLong("countdownTimer", TIMER_DURATION)
                    .build()

                val workRequest = OneTimeWorkRequest.Builder(CountdownWorker::class.java)
                    .setInputData(data)
                    .build()

                WorkManager.getInstance(applicationContext).enqueue(workRequest)
            }

            registerTimerBroadcastReceiver()
        }

    }

    // Register the broadcast receiver
    private fun registerTimerBroadcastReceiver() {
        LocalBroadcastManager.getInstance(applicationContext).registerReceiver(
            timerBroadcastReceiver,
            IntentFilter("countdown-timer")
        )
    }

    // Unregister the broadcast receiver when it's no longer needed
    private fun unregisterTimerBroadcastReceiver() {
        LocalBroadcastManager.getInstance(applicationContext).unregisterReceiver(timerBroadcastReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterTimerBroadcastReceiver()
    }
}