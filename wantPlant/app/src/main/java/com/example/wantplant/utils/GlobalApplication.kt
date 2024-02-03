package com.example.wantplant.utils

import android.app.Application
import com.example.wantplant.R
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Kakao SDK 초기화
        KakaoSdk.init(this, "38ef3a6efa9e6384d62bbc8aafadd751")
    }
}