package com.aleyn.mvvm.base

/**
 *   @auther : Aleyn
 *   time   : 2019/11/01
 */
data class BaseResult<out T>(val errorMsg: String, val errorCode: Int, val data: T) {
    fun isSuccess() = errorCode == 0
}