package com.mubarak.background_tasks

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mubarak.background_tasks.alarmmanager.AlarmManagerActivity
import com.mubarak.background_tasks.databinding.ActivityMainBinding
import com.mubarak.background_tasks.jobIntentservice.JobIntentServiceActivity
import com.mubarak.background_tasks.workmanager.WorkManagerActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnWorkManager.setOnClickListener {
            startActivity(Intent(this, WorkManagerActivity::class.java))
        }

        binding.btnAlarmManagerActivity.setOnClickListener {
            startActivity(Intent(this, AlarmManagerActivity::class.java))

        }

        binding.btnJobIntentServiceActivity.setOnClickListener {
            startActivity(Intent(this, JobIntentServiceActivity::class.java))

        }


    }
}