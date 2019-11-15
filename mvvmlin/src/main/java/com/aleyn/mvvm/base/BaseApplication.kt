package com.aleyn.mvvm.base

import android.app.Application
import android.content.Context
import com.aleyn.mvvm.app.GlobalConfig
import com.aleyn.mvvm.app.MVVMLin

/**
 *   @auther : Aleyn
 *   time   : 2019/11/12
 */
open class BaseApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MVVMLin.install(GlobalConfig().apply {
            viewModelFactory = ViewModelFactory()
        })
    }
}