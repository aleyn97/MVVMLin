package com.aleyn.mvvm.network

import android.net.ParseException
import com.aleyn.mvvm.app.MVVMLin
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 *   @auther : Aleyn
 *   time   : 2019/08/12
 */
object ExceptionHandle {

    fun handleException(e: Throwable): ResponseThrowable {
        return MVVMLin.getConfig().globalHandleException(e) ?: when (e) {
            is ResponseThrowable -> e
            is HttpException -> ResponseThrowable(ERROR.HTTP_ERROR, e)
            is JsonParseException,
            is JSONException,
            is ParseException,
            is MalformedJsonException -> ResponseThrowable(ERROR.PARSE_ERROR, e)
            is ConnectException -> ResponseThrowable(ERROR.NETWORD_ERROR, e)
            is SSLException -> ResponseThrowable(ERROR.SSL_ERROR, e)
            is SocketTimeoutException -> ResponseThrowable(ERROR.TIMEOUT_ERROR, e)
            is UnknownHostException -> ResponseThrowable(ERROR.TIMEOUT_ERROR, e)
            else -> {
                if (!e.message.isNullOrEmpty()) ResponseThrowable(1000, e.message!!, e)
                else ResponseThrowable(ERROR.UNKNOWN, e)
            }
        }
    }
}