package com.hyundaiht.androidcomponettest.service.startcommand

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log

class StartCommandTestService : Service() {
    private val tag = javaClass.simpleName

    override fun onCreate() {
        super.onCreate()
        Log.d(tag, "onCreate hashcode = ${hashCode()}")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val mode = intent?.getStringExtra("MODE") ?: "UNKNOWN"
        Log.d(tag, "서비스 시작됨 - Mode: $mode, hashcode = ${hashCode()}")

        // 3초 후 서비스 종료
        if(mode == "STOP") {
            Handler(Looper.getMainLooper()).postDelayed({
                stopSelf()
            }, 3000)
        }

        return when (mode) {
            "STICKY" -> START_STICKY
            "NOT_STICKY" -> START_NOT_STICKY
            "REDELIVER" -> START_REDELIVER_INTENT
            else -> START_NOT_STICKY
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "서비스 종료됨 - onDestroy, hashcode = ${hashCode()}")
    }

    override fun onBind(intent: Intent?): IBinder? = null
}