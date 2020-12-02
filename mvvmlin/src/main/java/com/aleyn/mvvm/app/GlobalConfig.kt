package com.aleyn.mvvm.app

import androidx.lifecycle.ViewModelProvider
import com.aleyn.mvvm.network.ResponseThrowable
import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 *   @auther : Aleyn
 *   time   : 2019/11/12
 */
interface GlobalConfig {

    fun provideRetrofit(build: Retrofit.Builder)

    fun provideOkHttpClient(build: OkHttpClient.Builder)

    fun viewModelFactory(): ViewModelProvider.NewInstanceFactory? = null

    fun globalHandleException(e: Throwable): ResponseThrowable? = null

}