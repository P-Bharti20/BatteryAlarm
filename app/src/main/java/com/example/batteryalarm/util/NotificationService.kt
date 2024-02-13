package com.example.batteryalarm.util

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import com.example.batteryalarm.viewmodel.MainViewModel

class NotificationService: Service() {

    private val mViewModel = MainViewModel()
    private val receiver= BatteryLevelReceiver(mViewModel)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        return START_STICKY
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onLowMemory() {
        stopSelf()
        super.onLowMemory()
    }
}