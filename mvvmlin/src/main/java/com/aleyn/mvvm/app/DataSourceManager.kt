package com.aleyn.mvvm.app

import com.blankj.utilcode.util.CacheMemoryUtils
import com.blankj.utilcode.util.LogUtils
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

/**
 *   @auther : Aleyn
 *   time   : 2020/11/27
 */
@Singleton
class DataSourceManager @Inject constructor() {

    private val cacheMemory = CacheMemoryUtils.getInstance(javaClass.simpleName, 256)

    @Inject
    lateinit var retrofit: Retrofit

    fun <T> remoteService(serviceClass: Class<T>): T {
        return cacheMemory.get<T>(serviceClass.name) ?: retrofit.create(serviceClass).also {
            cacheMemory.put(serviceClass.name, it)
        }
    }

}