package com.example.batteryalarm.viewmodel

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor() :ViewModel() {
    var enabledStatus = false
    var detachReceiver = false
}


