package com.pcl.mvvm.app

import com.aleyn.mvvm.app.GlobalConfig
import com.aleyn.mvvm.network.interceptor.LoggingInterceptor
import com.pcl.mvvm.common.Constant
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * @auther : Aleyn
 * time   : 2020/11/27
 */
class MyGlobalConfig : GlobalConfig {

    override fun provideRetrofit(build: Retrofit.Builder) {
        build.baseUrl(Constant.BASE_URL)
    }

    override fun provideOkHttpClient(build: OkHttpClient.Builder) {
        build.addInterceptor(LoggingInterceptor())
    }

}