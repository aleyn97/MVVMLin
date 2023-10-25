package com.aleyn.mvvm.app

import com.aleyn.mvvm.network.ExceptionHandle
import com.blankj.utilcode.util.ToastUtils
import kotlinx.coroutines.CoroutineExceptionHandler

/**
 *   @author : Aleyn
 *   time   : 2019/11/12
 */
object MVVMLin {

    private val defNetException = CoroutineExceptionHandler { _, throwable ->
        val exception = ExceptionHandle.handleException(throwable)
        throwable.printStackTrace()
        ToastUtils.showShort(exception.errMsg)
    }

    val netException: CoroutineExceptionHandler
        get() = customNetException ?: defNetException


    private var customNetException: CoroutineExceptionHandler? = null


    fun setNetException(netException: CoroutineExceptionHandler) = apply {
        customNetException = netException
    }

}