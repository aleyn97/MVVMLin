package com.aleyn.mvvm.base

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aleyn.mvvm.app.MVVMLin
import com.aleyn.mvvm.event.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

/**
 *   @author : Aleyn
 *   time   : 2019/11/01
 */
open class BaseViewModel : ViewModel(), IViewModel, DefaultLifecycleObserver {

    val defUI: UIChange by lazy(LazyThreadSafetyMode.NONE) { UIChange() }

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
        val showDialog = MutableSharedFlow<String>()
        val dismissDialog = MutableSharedFlow<Unit>()
        val msgEvent = MutableSharedFlow<Message>()
    }

    override fun showLoading(text: String) {
        launch { defUI.showDialog.emit(text) }
    }

    override fun dismissLoading() {
        launch { defUI.dismissDialog.emit(Unit) }
    }
}