package com.pcl.mvvm.app.base

import androidx.annotation.Keep
import com.aleyn.mvvm.base.IBaseResponse

/**
 *   @author : Aleyn
 *   time   : 2019/11/01
 */
@Keep
data class BaseResult<T>(
    val errorMsg: String,
    val errorCode: Int,
    val data: T
) : IBaseResponse<T> {

    override fun code() = errorCode

    override fun msg() = errorMsg

    override fun data() = data

    override fun isSuccess() = errorCode == 0

}