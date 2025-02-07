package com.hyundaiht.androidcomponettest.service.binding

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.hyundaiht.androidcomponettest.R

const val MSG_SAY_HELLO = 1
const val MSG_SAY_RESPONSE = 2

class MessengerTestService : Service() {
    private val tag = javaClass.simpleName

    private lateinit var mMessenger: Messenger

    /**
     * Handler of incoming messages from clients.
     */
    internal class IncomingHandler(
        context: Context,
        private val applicationContext: Context = context.applicationContext
    ) : Handler(Looper.myLooper()!!) {
        private var notificationManager: NotificationManager? = null

        // Notification Channel ID와 이름 설정
        private val channelId = "my_channel_id"
        private val channelName = "My Channel"
        private val channelDescription = "This is my notification channel"

        override fun handleMessage(msg: Message) {
            Log.d("MessengerTestService", "IncomingHandler hashCode = ${hashCode()}, handleMessage meg = ${msg.what}")
            createNotificationChannel()

            when (msg.what) {
                MSG_SAY_HELLO -> notificationManager?.notify(
                    0,
                    createNotification("MessengerTestService MSG_SAY_HELLO", "Hello!")
                )

                MSG_SAY_RESPONSE -> notificationManager?.notify(
                    0,
                    createNotification("MessengerTestService MSG_SAY_RESPONSE", "Me Too!")
                )

                else -> super.handleMessage(msg)
            }
        }

        private fun createNotification(title: String, content: String): Notification =
            NotificationCompat.Builder(applicationContext, channelId)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOngoing(true)
                .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
                .build()


        private fun createNotificationChannel() {
            // NotificationManager 객체를 가져옵니다
            notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // 중요도 설정 (예: 중요도 높음)
            val importance = NotificationManager.IMPORTANCE_HIGH

            // 채널을 생성합니다
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }

            // 채널을 시스템에 등록합니다
            notificationManager?.createNotificationChannel(channel)
        }
    }

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    override fun onBind(intent: Intent): IBinder? {
        Toast.makeText(applicationContext, "binding", Toast.LENGTH_SHORT).show()
        mMessenger = Messenger(IncomingHandler(this))
        return mMessenger.binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "서비스 종료됨 - onDestroy hashcode = ${hashCode()}")
    }
}