package com.aleyn.mvvm.app

/**
 *   @auther : Aleyn
 *   time   : 2019/11/12
 */
object MVVMLin {

    private val DEFULT = object : GlobalConfig {}

    private var mConfig: GlobalConfig = DEFULT

    fun install(config: GlobalConfig) {
        mConfig = config
    }

    fun getConfig() = mConfig

}