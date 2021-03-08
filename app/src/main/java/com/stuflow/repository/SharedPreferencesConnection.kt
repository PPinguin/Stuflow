package com.stuflow.repository

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesConnection {
    private lateinit var sharedPrefs: SharedPreferences

    fun init(context: Context){
        sharedPrefs = context.getSharedPreferences("_data", Context.MODE_PRIVATE)
    }

    fun put(key:String, data:String){}

    fun get(key:String){}
}