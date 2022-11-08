package com.example.sportscommunity.Adapter

import android.content.Context
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentActivity

fun backPressed(context: Context, activity: FragmentActivity) {

    var backPressedTime: Long = 0
    val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            Toast.makeText(
                context,
                "종료하시려면 뒤로가기 버튼을 한번 더 눌러주세요",
                Toast.LENGTH_SHORT
            ).show()

            if (System.currentTimeMillis() > backPressedTime + 1500) {
                backPressedTime = System.currentTimeMillis()
                return
            }

            if (System.currentTimeMillis() <= backPressedTime + 1500) {
                activity.finish()
            }
        }
    }
    activity.onBackPressedDispatcher.addCallback(activity,callback)
}