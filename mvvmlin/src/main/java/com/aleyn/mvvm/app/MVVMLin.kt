package com.aleyn.mvvm.app

import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 *   @auther : Aleyn
 *   time   : 2020/11/26
 */
object MVVMLin {

    private var globalConfig: GlobalConfig? = null

    private val DEFAULT = object : GlobalConfig {
        override fun provideRetrofit(build: Retrofit.Builder) {
        }

        override fun provideOkHttpClient(build: OkHttpClient.Builder) {
        }
    }

    fun install(config: GlobalConfig) {
        globalConfig = config
    }

    fun getConfig() = globalConfig ?: DEFAULT

}