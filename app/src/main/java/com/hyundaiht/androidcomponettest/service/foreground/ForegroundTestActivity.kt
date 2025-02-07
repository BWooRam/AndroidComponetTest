package com.hyundaiht.androidcomponettest.service.foreground

import android.app.ActivityManager
import android.app.Notification
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hyundaiht.androidcomponettest.broadcast.TitleAndButton
import com.hyundaiht.androidcomponettest.ui.theme.AndroidComponetTestTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ForegroundTestActivity : ComponentActivity() {
    private val tag = javaClass.simpleName
    private val scope = CoroutineScope(Dispatchers.Default)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidComponetTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        TitleAndButton(
                            title = "ForegroundTestService startService 테스트",
                            titleModifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(top = 10.dp),
                            buttonName = "실행",
                            buttonModifier = Modifier.wrapContentSize(),
                            clickEvent = {
//                                startService("STICKY")
//                                startService("NOT_STICKY")
                                startService("REDELIVER")
                            }
                        )

                        TitleAndButton(
                            title = "ForegroundTestService startForegroundService 테스트",
                            titleModifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(top = 10.dp),
                            buttonName = "실행",
                            buttonModifier = Modifier.wrapContentSize(),
                            clickEvent = {
//                                startForegroundService("STICKY")
//                                startForegroundService("NOT_STICKY")
                                startForegroundService("REDELIVER")
                            }
                        )

                        TitleAndButton(
                            title = "ForegroundTestService 10초 뒤에 startForegroundService 테스트",
                            titleModifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(top = 10.dp),
                            buttonName = "실행",
                            buttonModifier = Modifier.wrapContentSize(),
                            clickEvent = {
                                scope.launch {
                                    delay(10000)
                                    startForegroundService("STICKY")
                                }
                            }
                        )

                        TitleAndButton(
                            title = "ForegroundTestService stopService 테스트",
                            titleModifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(top = 10.dp),
                            buttonName = "실행",
                            buttonModifier = Modifier.wrapContentSize(),
                            clickEvent = {
                                stopService()
                            }
                        )

                        TitleAndButton(
                            title = "ForegroundTestService stopServiceStopSelf 테스트",
                            titleModifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(top = 10.dp),
                            buttonName = "실행",
                            buttonModifier = Modifier.wrapContentSize(),
                            clickEvent = {
                                stopServiceStopSelf()
                            }
                        )

                        TitleAndButton(
                            title = "ForegroundTestService getHistoricalProcessExitReasons 테스트",
                            titleModifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(top = 10.dp),
                            buttonName = "실행",
                            buttonModifier = Modifier.wrapContentSize(),
                            clickEvent = {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                    getHistoricalProcessExitReasons()
                                }
                            }
                        )

                        TitleAndButton(
                            title = "ForegroundTestService isServiceRunning 테스트",
                            titleModifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(top = 10.dp),
                            buttonName = "실행",
                            buttonModifier = Modifier.wrapContentSize(),
                            clickEvent = {
                                isServiceRunning()
                            }
                        )

                        TitleAndButton(
                            title = "ForegroundTestService isForegroundServiceRunning 테스트",
                            titleModifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(top = 10.dp),
                            buttonName = "실행",
                            buttonModifier = Modifier.wrapContentSize(),
                            clickEvent = {
                                isForegroundServiceRunning()
                            }
                        )
                    }
                }
            }
        }
    }

    /**
     * 여러 가지 방면으로 적용했으나 해당 이벤트로 포그라운드 서비스를 확인할 수 없었습니다.
     *
     *  - https://developer.android.com/develop/background-work/services/fgs/handle-user-stopping?hl=ko
     *  - https://issuetracker.google.com/issues/294415021?pli=1
     *  - https://www.geeksforgeeks.org/how-to-handle-foreground-services-in-android/
     */
    @RequiresApi(Build.VERSION_CODES.R)
    private fun getHistoricalProcessExitReasons() {
        val activityManager = getSystemService(ActivityManager::class.java)
        val exitInfoList = activityManager.getHistoricalProcessExitReasons(packageName, 0, 0)

        Log.d(tag, "getHistoricalProcessExitReasons start")
        for ((index, exitInfo) in exitInfoList.withIndex()) {
            Log.d(tag, "------------------------ $index --------------------------")
            Log.d(tag, "getHistoricalProcessExitReasons Process: ${exitInfo.processName}")
            Log.d(tag, "getHistoricalProcessExitReasons pid: ${exitInfo.pid}")
            Log.d(tag, "getHistoricalProcessExitReasons Reason: ${exitInfo.reason}")
            Log.d(tag, "getHistoricalProcessExitReasons packageUid: ${exitInfo.packageUid}")
            Log.d(tag, "getHistoricalProcessExitReasons description: ${exitInfo.description}")
            Log.d(tag, "---------------------------------------------------")
        }
    }

    private fun isServiceRunning() {
        val activityManager = getSystemService(ActivityManager::class.java)
        val services = activityManager.getRunningServices(Integer.MAX_VALUE)

        Log.d(tag, "isServiceRunning start size = ${services.size}")
        for ((index, service) in services.withIndex()) {
            Log.d(tag, "------------------------ $index --------------------------")
            Log.d(tag, "isServiceRunning service: ${service.service}")
            Log.d(tag, "isServiceRunning foreground: ${service.foreground}")
            Log.d(tag, "isServiceRunning pid: ${service.pid}")
            Log.d(tag, "isServiceRunning uid: ${service.uid}")
            Log.d(tag, "isServiceRunning flags: ${service.flags}")
            Log.d(tag, "isServiceRunning process: ${service.process}")
            Log.d(tag, "---------------------------------------------------")
        }
    }

    private fun isForegroundServiceRunning() {
        val notificationManager = getSystemService(NotificationManager::class.java)
        val activeNotifications = notificationManager.activeNotifications

        Log.d(tag, "isForegroundServiceRunning start")
        for ((index, notification) in activeNotifications.withIndex()) {
            val target = notification.notification
            val extras = target.extras
            val title = extras.getString(Notification.EXTRA_TITLE, "null")
            val content = extras.getString(Notification.EXTRA_TEXT, "null")
            Log.d(tag, "------------------------ $index --------------------------")
            Log.d(tag, "isForegroundServiceRunning id: ${notification.id}")
            Log.d(tag, "isForegroundServiceRunning channelId: ${target.channelId}")
            Log.d(tag, "isForegroundServiceRunning smallIcon: ${target.smallIcon}")
            Log.d(tag, "isForegroundServiceRunning contentIntent: ${target.contentIntent}")
            Log.d(tag, "isForegroundServiceRunning title: $title")
            Log.d(tag, "isForegroundServiceRunning content: $content")

            Log.d(tag, "---------------------------------------------------")
        }
    }

    private fun startService(mode: String) {
        val intent = Intent(this, ForegroundTestService::class.java)
        intent.putExtra("MODE", mode)
        startService(intent)
    }

    private fun startForegroundService(mode: String) {
        val intent = Intent(this, ForegroundTestService::class.java)
        intent.putExtra("MODE", mode)
        startForegroundService(intent)
    }

    private fun stopServiceStopSelf() {
        val intent = Intent(this, ForegroundTestService::class.java)
        intent.putExtra("MODE", "STOP")
        startService(intent)
    }
    private fun stopService() {
        val intent = Intent(this, ForegroundTestService::class.java)
        stopService(intent)
    }

}