package com.aleyn.mvvm.base

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aleyn.mvvm.app.MVVMLin
import com.aleyn.mvvm.event.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 *   @author : Aleyn
 *   time   : 2019/11/01
 */
open class BaseViewModel : ViewModel(), IViewModel, DefaultLifecycleObserver {

    private val _waitDialog = MutableStateFlow(WaitLoading(false))
    val waitDialog = _waitDialog.asStateFlow()

    private val _msgEvent = MutableSharedFlow<Message>()
    val msgEvent = _msgEvent.asSharedFlow()

    /**
     * 所有网络请求都在 viewModelScope 域中启动，当页面销毁时会自动
     * 调用ViewModel的  #onCleared 方法取消所有协程
     */
    fun launch(block: suspend CoroutineScope.() -> Unit) =
        viewModelScope.launch(MVVMLin.netException) { block() }

    override fun showLoading(text: String) {
        _waitDialog.update { it.copy(isShow = true, tipsText = text) }
    }

    override fun dismissLoading() {
        _waitDialog.update { it.copy(isShow = false, tipsText = "") }
    }
}

data class WaitLoading(
    val isShow: Boolean,
    val tipsText: String = "",
)