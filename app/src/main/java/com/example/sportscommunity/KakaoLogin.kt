package com.example.sportscommunity

import android.app.Application
import android.content.Context
import com.kakao.sdk.common.KakaoSdk

class KakaoApplication : Application() {
    companion object {
        var appContext : Context? = null
    }
    override fun onCreate() {
        super.onCreate()
        appContext = this
        KakaoSdk.init(this,getString(R.string.kakao_app_key))
    }
}