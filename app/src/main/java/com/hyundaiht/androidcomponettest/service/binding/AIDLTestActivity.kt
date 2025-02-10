package com.hyundaiht.androidcomponettest.service.binding

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Message
import android.os.RemoteException
import android.util.Log
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
import com.hyundaiht.androidcomponettest.IMyAidlInterface
import com.hyundaiht.androidcomponettest.broadcast.TitleAndButton
import com.hyundaiht.androidcomponettest.service.foreground.ForegroundTestService
import com.hyundaiht.androidcomponettest.ui.theme.AndroidComponetTestTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class AIDLTestActivity : ComponentActivity() {
    private val tag = javaClass.simpleName
    private val scope = CoroutineScope(Dispatchers.Default)

    /** Messenger for communicating with the service.  */
    private var mService: IMyAidlInterface? = null

    /** Flag indicating whether we have called bind on the service.  */
    private var bound: Boolean = false

    /**
     * Class for interacting with the main interface of the service.
     */
    private val mConnection = object : ServiceConnection {
        override fun onBindingDied(name: ComponentName?) {
            super.onBindingDied(name)
            Log.d(tag, "ServiceConnection onBindingDied")
        }

        override fun onNullBinding(name: ComponentName?) {
            super.onNullBinding(name)
            Log.d(tag, "ServiceConnection onNullBinding")
        }

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            Log.d(tag, "ServiceConnection onServiceConnected")
            mService = IMyAidlInterface.Stub.asInterface(service)
            bound = true
        }

        override fun onServiceDisconnected(className: ComponentName) {
            Log.d(tag, "ServiceConnection onServiceDisconnected")
            mService = null
            bound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidComponetTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        TitleAndButton(
                            title = "$tag bindService 테스트",
                            titleModifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(top = 10.dp),
                            buttonName = "실행",
                            buttonModifier = Modifier.wrapContentSize(),
                            clickEvent = {
//                                startForegroundService("STICKY")
//                                startForegroundService("NOT_STICKY")
                                bindService()
                            }
                        )

                        TitleAndButton(
                            title = "$tag sendMessage 테스트",
                            titleModifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(top = 10.dp),
                            buttonName = "실행",
                            buttonModifier = Modifier.wrapContentSize(),
                            clickEvent = {
                                sendMessage(MSG_SAY_HELLO)
                            }
                        )

                        TitleAndButton(
                            title = "$tag sendMessage 테스트",
                            titleModifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(top = 10.dp),
                            buttonName = "실행",
                            buttonModifier = Modifier.wrapContentSize(),
                            clickEvent = {
                                sendMessage(MSG_SAY_RESPONSE)
                            }
                        )

                        TitleAndButton(
                            title = "$tag stopService 테스트",
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
                            title = "$tag stopServiceStopSelf 테스트",
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
        val intent = Intent(this, ForegroundTestService::class.java)
        intent.putExtra("MODE", mode)
        startService(intent)
    }

    private fun bindService() {
        val intent = Intent().apply {
            val intentPackage = packageName
            val intentClass = "com.hyundaiht.androidcomponettest.service.binding.AIDLTestService"
            Log.d(tag, "bindService intentPackage = $intentPackage, intentClass = $intentClass")
            setClassName(intentPackage, intentClass)
        }
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    private fun sendMessage(msgType: Int) {
        if (!bound) {
            Log.d(tag, "sendMessage bound = false")
            return
        }

        // Create and send a message to the service, using a supported 'what' value.
        val msg: Message = Message.obtain(null, msgType, 0, 0)
        try {
            mService?.basicTypes(0, 1)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    private fun stopServiceStopSelf() {
        val intent = Intent(this, BindingTestService::class.java)
        intent.putExtra("MODE", "STOP")
        startService(intent)
    }

    private fun stopService() {
        val intent = Intent(this, BindingTestService::class.java)
        stopService(intent)
    }

}