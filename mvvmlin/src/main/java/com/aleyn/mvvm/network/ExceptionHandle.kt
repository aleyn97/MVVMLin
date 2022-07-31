package com.aleyn.mvvm.network

import android.net.ParseException
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException

/**
 *   @author : Aleyn
 *   time   : 2019/08/12
 */
object ExceptionHandle {

    fun handleException(e: Throwable): ResponseThrowable {
        val ex: ResponseThrowable
        when (e) {
            is ResponseThrowable -> {
                ex = e
            }
            is HttpException -> {
                ex = ResponseThrowable(ERROR.HTTP_ERROR, e)
            }
            is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> {
                ex = ResponseThrowable(ERROR.PARSE_ERROR, e)
            }
            is ConnectException -> {
                ex = ResponseThrowable(ERROR.NETWORK_ERROR, e)
            }
            is javax.net.ssl.SSLException -> {
                ex = ResponseThrowable(ERROR.SSL_ERROR, e)
            }
            is java.net.SocketTimeoutException -> {
                ex = ResponseThrowable(ERROR.TIMEOUT_ERROR, e)
            }
            is java.net.UnknownHostException -> {
                ex = ResponseThrowable(ERROR.TIMEOUT_ERROR, e)
            }
            else -> {
                ex = if (!e.message.isNullOrEmpty()) ResponseThrowable(1000, e.message!!, e)
                else ResponseThrowable(ERROR.UNKNOWN, e)
            }
        }
        return ex
    }
}