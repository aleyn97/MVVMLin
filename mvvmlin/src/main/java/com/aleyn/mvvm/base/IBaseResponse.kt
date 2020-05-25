package com.aleyn.mvvm.base

/**
 *   @auther : Aleyn
 *   time   : 2020/01/13
 */
interface IBaseResponse<T> {
    fun code(): Int
    fun msg(): String
    fun data(): T
    fun isSuccess(): Boolean
}