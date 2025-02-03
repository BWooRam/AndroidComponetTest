package com.hyundaiht.androidcomponettest.broadcast

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
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
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LifecycleStartEffect
import com.hyundaiht.androidcomponettest.ui.theme.AndroidComponetTestTheme

class ImplicitBroadCastReceiverTestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidComponetTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ImplicitBroadcastReceiver()

                    Column(modifier = Modifier.padding(innerPadding)) {
                        TitleAndButton(
                            title = "Test3BroadcastReceiver SendIntent 테스트",
                            titleModifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(top = 10.dp),
                            buttonName = "실행",
                            buttonModifier = Modifier.wrapContentSize(),
                            clickEvent = {
                                val intent = Intent("${ACTION_UPDATE_DATA}3").apply {
                                    setPackage("com.hyundaiht.androidcomponettest")
                                }

                                sendBroadcast(intent)
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun ImplicitBroadcastReceiver() {
        val br = remember { Test3BroadcastReceiver() }
        val context = LocalContext.current
        LifecycleStartEffect(true) {
            ContextCompat.registerReceiver(
                context,
                br,
                IntentFilter("${ACTION_UPDATE_DATA}3"),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
            onStopOrDispose { context.unregisterReceiver(br) }
        }
    }
}

const val ACTION_UPDATE_DATA = "com.hyundaiht.androidcomponettest.ACTION_UPDATE_DATA"

@Composable
fun TitleAndButton(
    title: String,
    titleModifier: Modifier = Modifier,
    buttonName: String,
    buttonModifier: Modifier = Modifier,
    clickEvent: () -> Unit
) {
    Text(
        text = title,
        modifier = titleModifier
    )
    Button(
        onClick = clickEvent,
        modifier = buttonModifier,
        content = { Text(text = buttonName) }
    )
}