package com.aleyn.mvvm.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

/**
 *   @auther : Aleyn
 *   time   : 2019/11/02
 */
class BaseInterceptor(private val headers: Map<String, String>?) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request().newBuilder().run {
            if (!headers.isNullOrEmpty()) {
                for (headMap in headers) {
                    addHeader(headMap.key, headMap.value).build()
                }
            }
            build()
        })
    }
}