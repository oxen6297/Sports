package com.example.sportscommunity.sharedpreference

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager {

    companion object {
        private var sp: SharedPreferences? = null
        private var editor: SharedPreferences.Editor? = null

        private fun getInstance(context: Context): SharedPreferences {
            synchronized(this) {
                sp = context.getSharedPreferences("userId", Context.MODE_PRIVATE)
                return sp!!
            }
        }

        fun putString(context: Context, key: String, value: String) {
            editor = getInstance(context).edit()
            editor!!.putString(key, value)
            editor!!.commit()
        }

        fun getString(context: Context, key: String, default: String): String {
            return getInstance(context).getString(key, default)!!
        }

        fun putInt(context: Context, key: String, value: Int) {
            editor = getInstance(context).edit()
            editor!!.putInt(key, value)
            editor!!.commit()
        }

        fun getInt(context: Context, key: String, default: Int): Int {
            return getInstance(context).getInt(key, default)
        }
    }
}