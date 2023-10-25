package com.aleyn.mvvm.base

import androidx.annotation.Keep

/**
 *   @author : Aleyn
 *   time   : 2020/01/13
 */
@Keep
interface IBaseResponse<T> {
    fun code(): Int
    fun msg(): String
    fun data(): T
    fun isSuccess(): Boolean
}