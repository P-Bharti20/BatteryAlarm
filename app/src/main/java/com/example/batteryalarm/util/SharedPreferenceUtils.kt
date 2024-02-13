package com.example.batteryalarm.util

import android.content.Context
import com.example.batteryalarm.data.SharedPrefKeys.ENABLED_STATE
import com.example.batteryalarm.data.SharedPrefKeys.PREFERENCE_NAME

class SharedPreferenceUtils() {

    fun setEnableState(context: Context,state:Boolean){
        val sharedPreference = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.putBoolean(ENABLED_STATE,state).apply()
    }

    fun getEnableState(context: Context):Boolean{
        val sharedPreference = context.getSharedPreferences(PREFERENCE_NAME,Context.MODE_PRIVATE)
        return sharedPreference.getBoolean(ENABLED_STATE,true)
    }
}