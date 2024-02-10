package com.example.batteryalarm.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
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
import com.example.batteryalarm.viewmodel.MainViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private val mViewModel:MainViewModel by viewModels()
    private lateinit var receiver: BatteryLevelReceiver
    private lateinit var sharedPref:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        receiver= BatteryLevelReceiver(mViewModel)
        sharedPref = this.getSharedPreferences(getString(R.string.enable_state),Context.MODE_PRIVATE)
        initView()
        initAction()
        initObserver()
    }

    private fun initView() {
        supportActionBar?.title = "Battery Alarm"
        mBinding.enableButton.isEnabled = sharedPref.getBoolean(getString(R.string.enable_state),false)
        supportActionBar?.show()
    }

    private fun initAction(){
        if(mViewModel.detachReceiver){
            unregisterReceiver(receiver)
            finish()
        }
    }

    private fun initObserver() {
        mBinding.enableButton.setOnCheckedChangeListener { _, isChecked ->
            with (sharedPref.edit()) {
                putBoolean(getString(R.string.enable_state), isChecked)
                apply()
            }
            updateUI()
        }
    }

    @SuppressLint("InlinedApi")
    private fun updateUI() {
        if (sharedPref.getBoolean(getString(R.string.enable_state),false)) {
            val permissionState = ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            // If the permission is not granted, request it.
            if (permissionState == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_PERMISSION_REQUEST_CODE
                )
            }
            mBinding.notifyIcon.setImageResource(R.drawable.ic_notifications_on)
            mBinding.message.setText(R.string.alarm_enabled_msg)
            registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        }
        else {
            mBinding.notifyIcon.setImageResource(R.drawable.ic_notifications_off)
            mBinding.message.setText(R.string.alarm_disabled_msg)
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
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    companion object {
        const val CHANNEL_ID = "Battery Alarm"
        const val NOTIFICATION_PERMISSION_REQUEST_CODE=1005
    }
}