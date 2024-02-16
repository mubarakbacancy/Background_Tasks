package com.mubarak.background_tasks.jobIntentservice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mubarak.background_tasks.R

class JobIntentServiceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_job_intent_service)

        val intent = Intent(this, MyJobIntentService::class.java)
        MyJobIntentService.enqueueWork(this, intent)
    }
}