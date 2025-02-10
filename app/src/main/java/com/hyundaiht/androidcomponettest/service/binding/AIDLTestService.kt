package com.hyundaiht.androidcomponettest.service.binding

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.hyundaiht.androidcomponettest.IMyAidlInterface

class AIDLTestService : Service() {
    private val tag = javaClass.simpleName

    private val binder = object : IMyAidlInterface.Stub() {
        override fun basicTypes(
            anInt: Int,
            aLong: Long
        ) {
            Log.d(tag, "basicTypes() binder = ${this.hashCode()}, AIDLTestService = ${this@AIDLTestService.hashCode()}")
            Log.d(tag, "basicTypes() anInt = $aLong, aLong = $aLong")
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(tag, "onCreate hashcode = ${hashCode()}")
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d(tag, "onBind hashcode = ${hashCode()}")
        return binder  // 클라이언트에게 이 Binder 객체를 반환
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(tag, "onStartCommand hashcode = ${hashCode()}")
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "서비스 종료됨 - onDestroy hashcode = ${hashCode()}")
    }
}