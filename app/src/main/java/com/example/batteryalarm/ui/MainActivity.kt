package com.example.batteryalarm.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.batteryalarm.R
import com.example.batteryalarm.databinding.ActivityMainBinding
import com.example.batteryalarm.util.BatteryLevelReceiver
import com.example.batteryalarm.util.NotificationService
import com.example.batteryalarm.util.SharedPreferenceUtils
import com.example.batteryalarm.viewmodel.MainViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private val mViewModel:MainViewModel by viewModels()
    private lateinit var receiver: BatteryLevelReceiver
    private lateinit var sharedpref:SharedPreferenceUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        receiver= BatteryLevelReceiver(mViewModel)
        sharedpref= SharedPreferenceUtils()
        initView()
        updateAlarmButton()
        initAction()
        initObserver()
    }

    private fun initView() {
        supportActionBar?.title = "Battery Alarm"
        supportActionBar?.show()
    }

    private fun initAction(){
       startService(Intent(this, NotificationService::class.java))
    }

    private fun updateAlarmButton(){
        mViewModel.enabledStatus=sharedpref.getEnableState(this)
        if(mViewModel.enabledStatus){
            mBinding.enableButton.isChecked = true
            mBinding.notifyIcon.setImageResource(R.drawable.ic_notifications_on)
            mBinding.message.setText(R.string.alarm_enabled_msg)
            registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            mViewModel.isRegistered=true
        }
        else{
            mBinding.enableButton.isChecked = false
            mBinding.notifyIcon.setImageResource(R.drawable.ic_notifications_off)
            mBinding.message.setText(R.string.alarm_disabled_msg)
            if(mViewModel.isRegistered) {
                unregisterReceiver(receiver)
            }
        }
    }

    private fun initObserver() {
        mBinding.enableButton.setOnCheckedChangeListener { _, isChecked ->
            sharedpref.setEnableState(this,isChecked)
            updateAlarmButton()
            updateUI()
        }

        mViewModel.shouldStopLowBatteryAlarm.observeForever { shouldStop ->
            if (shouldStop) {
                receiver.stopRingtone()
            }
        }

        mViewModel.shouldStopFullChargeAlarm.observeForever { shouldStop ->
            if (shouldStop) {
                receiver.stopRingtone()
            }
        }
    }

    @SuppressLint("InlinedApi")
    private fun updateUI() {
        if (sharedpref.getEnableState(this)) {
            val permissionState = ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            // If the permission is not granted, request it.
            if (permissionState == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    private fun sendNotification() {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.battery_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting_icon -> {
                Toast.makeText(this, "Settings haven't implemented yet!!", Toast.LENGTH_SHORT)
                    .show()
            }

            R.id.share_icon -> {
                Toast.makeText(this, "Share haven't implemented yet!!", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    companion object {
        const val NOTIFICATION_PERMISSION_REQUEST_CODE=1005
    }
}