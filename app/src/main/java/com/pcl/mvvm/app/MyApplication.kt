package com.pcl.mvvm.app

import com.aleyn.mvvm.base.BaseApplication
import com.blankj.utilcode.util.LogUtils
import com.pcl.mvvm.BuildConfig

/**
 *   @auther : Aleyn
 *   time   : 2019/11/04
 */
class MyApplication : BaseApplication() {


    override fun onCreate() {
        super.onCreate()

        // 在OnCreate 中可以传入 自定义的 GlobalConfig
        /*MVVMLin.install(object : GlobalConfig {
            override fun viewModelFactory() = ViewModelFactory.getInstance()
        })*/

        LogUtils.getConfig().run {
            isLogSwitch = BuildConfig.DEBUG
            setSingleTagSwitch(true)
        }

    }
}