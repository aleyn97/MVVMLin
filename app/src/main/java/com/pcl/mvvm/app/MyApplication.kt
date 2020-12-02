package com.pcl.mvvm.app

import android.app.Application
import android.content.Context
import com.aleyn.mvvm.app.MVVMLin
import com.blankj.utilcode.util.LogUtils
import com.pcl.mvvm.BuildConfig
import dagger.hilt.android.HiltAndroidApp

/**
 *   @auther : Aleyn
 *   time   : 2019/11/04
 */
@HiltAndroidApp
class MyApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MVVMLin.install(MyGlobalConfig())
    }

    override fun onCreate() {
        super.onCreate()
        LogUtils.getConfig().run {
            isLogSwitch = BuildConfig.DEBUG
            setSingleTagSwitch(true)
        }

    }
}