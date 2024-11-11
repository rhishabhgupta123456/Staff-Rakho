package com.staffrakho

import android.app.Application
import com.google.android.gms.ads.MobileAds

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {}
    }
}