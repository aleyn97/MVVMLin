package com.pcl.mvvm.utils

import com.aleyn.mvvm.network.interceptor.Level
import com.aleyn.mvvm.network.interceptor.LoggingInterceptor
import com.pcl.mvvm.BuildConfig
import com.pcl.mvvm.common.Constant.BASE_URL
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 *   @auther : Aleyn
 *   time   : 2019/09/04
 */
class RetrofitClient {

    companion object {
        fun getInstance() = SingletonHolder.INSTANCE
        private lateinit var retrofit: Retrofit
    }

    private object SingletonHolder {
        val INSTANCE = RetrofitClient()
    }

    init {
        retrofit = Retrofit.Builder()
            .client(getOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(20L, TimeUnit.SECONDS)
            .addNetworkInterceptor(LoggingInterceptor().apply {
                isDebug = BuildConfig.DEBUG
                level = Level.BASIC
                type = Platform.INFO
                requestTag = "Request"
                requestTag = "Response"
            })
            .writeTimeout(20L, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(8, 15, TimeUnit.SECONDS))
            .build()
    }

    fun <T> create(service: Class<T>?): T =
        retrofit.create(service!!) ?: throw RuntimeException("Api service is null!")
}