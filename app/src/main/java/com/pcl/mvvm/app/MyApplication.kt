package com.pcl.mvvm.app

import com.aleyn.mvvm.app.MVVMLin
import com.aleyn.mvvm.base.BaseApplication
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.pcl.mvvm.BuildConfig
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import kotlinx.coroutines.CoroutineExceptionHandler

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

        //可选
        MVVMLin.setNetException(CoroutineExceptionHandler { context, e ->
             ToastUtils.showShort(e.message)
        })

        LogUtils.getConfig().run {
            isLogSwitch = BuildConfig.DEBUG
            setSingleTagSwitch(true)
        }
    }
}