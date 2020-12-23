package com.aleyn.mvvm.app

import androidx.lifecycle.ViewModelProvider
import com.aleyn.mvvm.base.ViewModelFactory
import com.aleyn.mvvm.network.ExceptionHandle

/**
 *   @auther : Aleyn
 *   time   : 2019/11/12
 */
interface GlobalConfig {

    fun viewModelFactory(): ViewModelProvider.Factory? = ViewModelFactory.getInstance()

    fun globalExceptionHandle(e: Throwable) = ExceptionHandle.handleException(e)

}