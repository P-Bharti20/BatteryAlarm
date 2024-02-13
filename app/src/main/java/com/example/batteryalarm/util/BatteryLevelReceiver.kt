package com.example.batteryalarm.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.BatteryManager
import androidx.appcompat.app.AlertDialog
import com.example.batteryalarm.R
import com.example.batteryalarm.viewmodel.MainViewModel


class BatteryLevelReceiver(private val mViewModel: MainViewModel) : BroadcastReceiver() {

    private lateinit var ringtone: Ringtone

    override fun onReceive(context: Context, intent: Intent) {
        ringtone = RingtoneManager.getRingtone(
            context,
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        )

        val integerBatteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
        val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val isCharging =
            status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL
        if (isCharging && integerBatteryLevel > 90) {
            // inform the user
            val mBuilder = AlertDialog.Builder(context)
            mBuilder.setMessage("Full battery!! \n\nUnplug Charger/stop the alarm")
                .setCancelable(false)
                .setPositiveButton(R.string.notification_exit) { dialog, _ ->
                    dialog.cancel()
                    mViewModel.shouldStopFullChargeAlarm.value = true
                }
            val mAlert = mBuilder.create()
            mAlert.show()
            ringtone.play()

        } else if (integerBatteryLevel < 10) {

            // inform the user
            val mBuilder = AlertDialog.Builder(
                context
            )
            mBuilder.setMessage("Low Battery!! \n\nPlug in Charger/stop the alarm")
                .setCancelable(false)
                .setPositiveButton(
                    R.string.notification_exit
                ) { dialog, _ ->
                    dialog.cancel()
                    mViewModel.shouldStopFullChargeAlarm.value = true
                }
            val mAlert = mBuilder.create()
            mAlert.show()
            ringtone.play()
        }

    }

    fun stopRingtone() {
        ringtone.stop()
    }
}