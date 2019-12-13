package com.pcl.mvvm.utils

import com.aleyn.mvvm.base.BaseResult
import com.aleyn.mvvm.base.BaseViewModel
import com.aleyn.mvvm.network.ExceptionHandle
import com.aleyn.mvvm.network.ResponseThrowable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext


/**
 * 这个是对 BaseViewModel 的扩展，由于每个项目定义的  BaseResult 字段是不同的，
 * 如果字段正好和 BaseResult 中相同的话，忽略 这个文件就好，不同的话
 * 需要 如下 操作：
 * 1, 把这个 扩展类复制到 你的 项目中
 * 2，把 下边的  BaseResult 改成自己项目的 基类 ；
 * 3，把 下列几个函数中  BaseResult 涉及到的（data,errorCode，errorMsg）字段，修为自己项目定义的 ；
 * 4, launchOnlyresult 的函数名字可以改一个自定义的，为了和  BaseViewModel 中的好区分，不改也可以，不过
 * 用的时候要注意下
 */

/**
 * 只返回数据，其他全抛异常
 */
/**
 * 只返回数据，其他全抛异常
 */
fun <T> BaseViewModel.launchOnlyresult(
    block: suspend CoroutineScope.() -> BaseResult<T>,
    success: (T) -> Unit,
    error: (ResponseThrowable) -> Unit = {
        defUI.toastEvent.postValue("${it.code}:${it.errMsg}")
    },
    complete: () -> Unit = {},
    isShowDialog: Boolean = true
) {
    if (isShowDialog) defUI.showDialog.call()
    launchUI {
        handleException(
            { withContext(Dispatchers.IO) { block() } },
            { res ->
                executeResponse(res) { success(it) }
            },
            {
                error(it)
            },
            {
                defUI.dismissDialog.call()
                complete()
            }
        )
    }
}


/**
 * 请求结果过滤
 */
private suspend fun <T> executeResponse(
    response: BaseResult<T>,
    success: suspend CoroutineScope.(T) -> Unit
) {
    coroutineScope {
        if (response.isSuccess()) success(response.data)
        else throw ResponseThrowable(response.errorCode, response.errorMsg)
    }
}


/**
 * 异常统一处理
 */
private suspend fun <T> handleException(
    block: suspend CoroutineScope.() -> BaseResult<T>,
    success: suspend CoroutineScope.(BaseResult<T>) -> Unit,
    error: suspend CoroutineScope.(ResponseThrowable) -> Unit,
    complete: suspend CoroutineScope.() -> Unit
    ) {
    coroutineScope {
        try {
            success(block())
        } catch (e: Throwable) {
            error(ExceptionHandle.handleException(e))
        } finally {
            complete()
        }
    }
}