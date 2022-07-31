package com.pcl.mvvm.app

import com.aleyn.mvvm.base.BaseApplication
import com.blankj.utilcode.util.LogUtils
import com.pcl.mvvm.BuildConfig
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 *   @author : Aleyn
 *   time   : 2019/11/04
 */
class MyApplication : BaseApplication() {

    companion object {
        init {
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
                ClassicsHeader(context)
            }
        }
    }


    override fun onCreate() {
        super.onCreate()

        /*MVVMLin.setNetException(CoroutineExceptionHandler { context, e ->

        })*/

        LogUtils.getConfig().run {
            isLogSwitch = BuildConfig.DEBUG
            setSingleTagSwitch(true)
        }
    }
}