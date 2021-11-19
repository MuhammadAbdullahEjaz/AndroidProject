package com.example.project.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPref {
    companion object {
        private var sharedPref: SharedPreferences? = null

        fun setupSharedPref(cont: Context){
            sharedPref = cont.getSharedPreferences("Main", Context.MODE_PRIVATE)
        }

        fun setPrefNotFirst(value: Boolean) {
            sharedPref!!.edit().apply {
                putBoolean("notfirst", value)
            }.apply()
        }

        fun getPrefNotFirst(): Boolean {
            return sharedPref?.getBoolean("notfirst", false)!!
        }
    }

}