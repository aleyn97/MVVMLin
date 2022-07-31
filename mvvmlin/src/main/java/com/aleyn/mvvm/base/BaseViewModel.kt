package com.aleyn.mvvm.base

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aleyn.mvvm.app.MVVMLin
import com.aleyn.mvvm.event.Message
import com.aleyn.mvvm.event.SingleLiveEvent
import kotlinx.coroutines.*

/**
 *   @author : Aleyn
 *   time   : 2019/11/01
 */
open class BaseViewModel : ViewModel(), IViewModel, DefaultLifecycleObserver {

    val defUI: UIChange by lazy { UIChange() }

    /**
     * 所有网络请求都在 viewModelScope 域中启动，当页面销毁时会自动
     * 调用ViewModel的  #onCleared 方法取消所有协程
     */
    fun launch(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(MVVMLin.netException) { block() }

    /**
     * UI事件
     */
    inner class UIChange {
        val showDialog by lazy { SingleLiveEvent<String>() }
        val dismissDialog by lazy { SingleLiveEvent<Void>() }
        val msgEvent by lazy { SingleLiveEvent<Message>() }
    }

    override fun showLoading(text: String) {
        defUI.showDialog.postValue(text)
    }

    override fun dismissLoading() {
        defUI.dismissDialog.call()
    }
}