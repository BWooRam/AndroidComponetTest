package com.hyundaiht.androidcomponettest.service.startcommand

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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

class StartCommandTestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidComponetTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        //테스트1
                        TitleAndButton(
                            title = "StartCommandTestService STICKY 테스트",
                            titleModifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(top = 10.dp),
                            buttonName = "실행",
                            buttonModifier = Modifier.wrapContentSize(),
                            clickEvent = {
                                startService("STICKY")
                            }
                        )

                        //테스트2
                        TitleAndButton(
                            title = "StartCommandTestService NOT_STICKY 테스트",
                            titleModifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(top = 10.dp),
                            buttonName = "실행",
                            buttonModifier = Modifier.wrapContentSize(),
                            clickEvent = {
                                startService("NOT_STICKY")
                            }
                        )

                        //테스트3
                        TitleAndButton(
                            title = "StartCommandTestService REDELIVER 테스트",
                            titleModifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(top = 10.dp),
                            buttonName = "실행",
                            buttonModifier = Modifier.wrapContentSize(),
                            clickEvent = {
                                startService("REDELIVER")
                            }
                        )

                        //테스트4
                        TitleAndButton(
                            title = "StartCommandTestService stopService 테스트",
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

                        //테스트5
                        TitleAndButton(
                            title = "StartCommandTestService stopServiceStopSelf 테스트",
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
                    }
                }
            }
        }
    }

    private fun startService(mode: String) {
        val intent = Intent(this, StartCommandTestService::class.java)
        intent.putExtra("MODE", mode)
        startService(intent)
    }

    private fun stopServiceStopSelf() {
        val intent = Intent(this, StartCommandTestService::class.java)
        intent.putExtra("MODE", "STOP")
        startService(intent)
    }

    private fun stopService() {
        val intent = Intent(this, StartCommandTestService::class.java)
        stopService(intent)
    }
}