package com.aleyn.mvvm.adapter

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.lang.reflect.Type
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * @author : Aleyn
 * @date : 2022/07/24 : 15:26
 */
class FlowCallAdapter<R : Type>(
    private val responseBodyType: R,
    private val isBody: Boolean,
    private val isAsync: Boolean
) : CallAdapter<R, Flow<*>> {

    override fun responseType() = responseBodyType

    override fun adapt(call: Call<R>): Flow<*> {
        val callResponse = if (isAsync) ASyncCall(call) else SyncCall(call)
        return if (isBody) bodyFlow(callResponse) else responseFlow(callResponse)
    }

    private fun responseFlow(call: CallResponse): Flow<Response<*>> = flow {
        val response = call.call()
        emit(response)
    }

    private fun bodyFlow(call: CallResponse): Flow<*> = flow {
        val response = call.call()
        if (response.isSuccessful) {
            emit(response.body()!!)
        } else {
            throw HttpException(response)
        }
    }


    internal interface CallResponse {
        suspend fun call(): Response<*>
    }

    internal inner class SyncCall<T>(private val originalCall: Call<T>) : CallResponse {

        override suspend fun call(): Response<T> {
            return suspendCancellableCoroutine {
                val call = originalCall.clone()
                it.invokeOnCancellation { call.cancel() }
                try {
                    it.resume(call.execute())
                } catch (e: Exception) {
                    it.resumeWithException(e)
                }
            }
        }
    }

    internal inner class ASyncCall<T>(private val originalCall: Call<T>) : CallResponse {

        override suspend fun call(): Response<T> = suspendCancellableCoroutine {
            val call = originalCall.clone()
            it.invokeOnCancellation { call.cancel() }
            call.enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    it.resume(response)
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    it.resumeWithException(t)
                }
            })

        }
    }
}