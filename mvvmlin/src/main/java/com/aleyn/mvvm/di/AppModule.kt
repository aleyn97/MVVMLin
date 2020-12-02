package com.aleyn.mvvm.di

import com.aleyn.mvvm.app.MVVMLin
import com.aleyn.mvvm.network.interceptor.LoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 *   @auther : Aleyn
 *   time   : 2020/11/26
 */
@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun retrofitClient(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .also {
                MVVMLin.getConfig().provideRetrofit(it)
            }
            .build()
    }

    @Singleton
    @Provides
    fun httpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(20L, TimeUnit.SECONDS)
            .writeTimeout(20L, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(8, 15, TimeUnit.SECONDS))
            .also {
                MVVMLin.getConfig().provideOkHttpClient(it)
            }.build()
    }

}