package com.hyundaiht.androidcomponettest.service.binding

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.hyundaiht.androidcomponettest.R

class BindingTestService : Service() {
    private val tag = javaClass.simpleName

    // Notification Channel ID와 이름 설정
    private val channelId = "my_channel_id"
    private val channelName = "My Channel"
    private val channelDescription = "This is my notification channel"
    private var receiver: BluetoothScanReceiver? = null

    override fun onCreate() {
        super.onCreate()
        Log.d(tag, "onCreate hashcode = ${hashCode()}")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val mode = intent?.getStringExtra("MODE") ?: "UNKNOWN"
        Log.d(tag, "서비스 시작됨 - Mode: $mode, hashcode = ${hashCode()}")
        startBluetoothScan(this)
        startForeground()
        // 3초 후 서비스 종료
        if (mode == "STOP") {
            Handler(Looper.getMainLooper()).postDelayed({
                stopSelf()
            }, 5000)
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
        Log.d(tag, "서비스 종료됨 - onDestroy hashcode = ${hashCode()}")
        kotlin.runCatching {
            unregisterReceiver(receiver)
        }.onSuccess {
            Log.d(tag, "unregisterReceiver onSuccess")
        }.onFailure {
            Log.d(tag, "unregisterReceiver onFailure e = $it")
        }
    }

    class BluetoothScanReceiver(private val tag: String) : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(tag, "BluetoothScanReceiver onReceive intent = $intent")
            when (intent?.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (ActivityCompat.checkSelfPermission(
                            context!!,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        Log.d(tag, "BluetoothScanReceiver PERMISSION_DENIED")
                        return
                    }
                    Log.d(
                        tag,
                        "BluetoothScanReceiver Found device: ${device?.name} - ${device?.address}"
                    )
                }

                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Log.d(tag, "BluetoothScanReceiverBluetooth scan finished.")
                    context?.unregisterReceiver(this)
                }
            }
        }
    }

    private fun startBluetoothScan(context: Context) {
        val bluetoothAdapter: BluetoothAdapter? = getBluetoothAdapter(context)
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            Log.e(tag, "startBluetoothScan() Bluetooth is not available or not enabled.")
            return
        }

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        receiver = BluetoothScanReceiver(tag)
        context.registerReceiver(receiver, filter)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_SCAN
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // 블루투스 스캔 시작
            bluetoothAdapter.startDiscovery()
        }
    }

    private fun getBluetoothAdapter(context: Context): BluetoothAdapter? {
        val bluetoothManager = context.getSystemService(BluetoothManager::class.java)
        return bluetoothManager?.adapter
    }

    private fun startForeground() {
        createNotificationChannel()
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("알림 제목")
            .setContentText("알림 내용")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()

        ServiceCompat.startForeground(
            /* service = */ this,
            /* id = */ 100, // Cannot be 0
            /* notification = */ notification,
            /* foregroundServiceType = */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE
            } else {
                0
            },
        )
    }

    private fun createNotificationChannel() {
        // NotificationManager 객체를 가져옵니다
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 중요도 설정 (예: 중요도 높음)
        val importance = NotificationManager.IMPORTANCE_HIGH

        // 채널을 생성합니다
        val channel = NotificationChannel(channelId, channelName, importance).apply {
            description = channelDescription
        }

        // 채널을 시스템에 등록합니다
        notificationManager.createNotificationChannel(channel)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}