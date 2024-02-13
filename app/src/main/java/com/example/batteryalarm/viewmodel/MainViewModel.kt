package com.example.batteryalarm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor() :ViewModel() {
    var isRegistered = false
    var enabledStatus = false
    val shouldStopFullChargeAlarm = MutableLiveData(false)
    val shouldStopLowBatteryAlarm = MutableLiveData(false)
}


