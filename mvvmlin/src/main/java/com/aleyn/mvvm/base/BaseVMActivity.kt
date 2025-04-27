package com.aleyn.mvvm.base

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

/**
 * @author : Aleyn
 * @date : 2022/07/31 : 23:05
 */
abstract class BaseVMActivity<VM : BaseViewModel, VB : ViewBinding> : BaseActivity<VB>() {

    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        createViewModel()
        lifecycle.addObserver(viewModel)
        //注册 UI事件
        registerDefUIChange(viewModel)
        super.onCreate(savedInstanceState)
    }

    /**
     * 创建 ViewModel
     */
    @Suppress("UNCHECKED_CAST")
    private fun createViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val cls = type.actualTypeArguments[0] as Class<VM>
            viewModel = ViewModelProvider(viewModelStore, defaultViewModelProviderFactory)[cls]
        }
    }

}