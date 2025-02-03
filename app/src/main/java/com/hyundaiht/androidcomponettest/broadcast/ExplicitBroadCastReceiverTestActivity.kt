package com.hyundaiht.androidcomponettest.broadcast

import android.Manifest
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
import com.hyundaiht.androidcomponettest.ui.theme.AndroidComponetTestTheme

class ExplicitCastReceiverTestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidComponetTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        TitleAndButton(
                            title = "Test1BroadcastReceiver SendIntent 테스트",
                            titleModifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(top = 10.dp),
                            buttonName = "실행",
                            buttonModifier = Modifier.wrapContentSize(),
                            clickEvent = {
                                val intent = Intent("${ACTION_UPDATE_DATA}1").apply {
                                    setPackage("com.hyundaiht.androidcomponettest")
                                }

                                sendBroadcast(intent)
                            }
                        )

                        TitleAndButton(
                            title = "Test2BroadcastReceiver SendIntent 테스트",
                            titleModifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(top = 10.dp),
                            buttonName = "실행",
                            buttonModifier = Modifier.wrapContentSize(),
                            clickEvent = {
                                val intent = Intent("${ACTION_UPDATE_DATA}2").apply {
                                    setPackage("com.hyundaiht.androidcomponettest")
                                }
                                sendOrderedBroadcast(intent, Manifest.permission.POST_NOTIFICATIONS)
                            }
                        )
                    }
                }
            }
        }
    }
}