package com.hyundaiht.androidcomponettest.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class Test1BroadcastReceiver : BroadcastReceiver() {
    private val tag = javaClass.simpleName

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(tag, "onReceive start")
        Toast.makeText(context, "$tag 이벤트 발생", Toast.LENGTH_SHORT).show()
    }
}