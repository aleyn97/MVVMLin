package com.aleyn.mvvm.base

/**
 * @author : Aleyn
 * @date : 2022/07/31 : 20:28
 */
interface IViewModel {

    fun showLoading(text: String = "")

    fun dismissLoading()

}