package com.aleyn.mvvm.utils

import com.aleyn.mvvm.base.IBaseResponse
import com.aleyn.mvvm.network.ResponseThrowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

@ExperimentalCoroutinesApi
fun <T> Flow<IBaseResponse<T>>.applyTransform(): Flow<T> {
    return this
        .flowOn(Dispatchers.IO)
        .map {
            if (it.isSuccess()) return@map it.data()
            else throw ResponseThrowable(it.code(), it.msg())
        }
}