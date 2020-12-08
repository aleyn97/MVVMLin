package com.aleyn.mvvm.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.aleyn.mvvm.R
import com.aleyn.mvvm.app.MVVMLin
import com.aleyn.mvvm.event.Message
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 *   @auther : Aleyn
 *   time   : 2019/11/01
 */
abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var viewModel: VM

    protected var mBinding: DB? = null

    private var dialog: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewDataBinding()
        lifecycle.addObserver(viewModel)
        //注册 UI事件
        registorDefUIChange()
        initView(savedInstanceState)
        initData()
    }

    abstract fun layoutId(): Int
    abstract fun initView(savedInstanceState: Bundle?)
    abstract fun initData()


    /**
     * DataBinding
     */
    private fun initViewDataBinding() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val cls = type.actualTypeArguments[1] as Class<*>
            if (ViewDataBinding::class.java != cls
                && ViewDataBinding::class.java.isAssignableFrom(cls)
            ) {
                mBinding = DataBindingUtil.setContentView(this, layoutId())
                mBinding?.lifecycleOwner = this
            } else setContentView(layoutId())

            createViewModel(type.actualTypeArguments[0])
        } else throw IllegalArgumentException("genericSuperclass not is ParameterizedType")
    }


    /**
     * 注册 UI 事件
     */
    private fun registorDefUIChange() {
        viewModel.defUI.showDialog.observe(this, {
            showLoading()
        })
        viewModel.defUI.dismissDialog.observe(this, {
            dismissLoading()
        })
        viewModel.defUI.toastEvent.observe(this, {
            ToastUtils.showShort(it)
        })
        viewModel.defUI.msgEvent.observe(this, {
            handleEvent(it)
        })
    }

    open fun handleEvent(msg: Message) {}

    /**
     * 打开等待框
     */
    private fun showLoading() {
        (dialog ?: MaterialDialog(this)
            .cancelable(false)
            .cornerRadius(8f)
            .customView(R.layout.custom_progress_dialog_view, noVerticalPadding = true)
            .lifecycleOwner(this)
            .maxWidth(R.dimen.dialog_width)
            .also { dialog = it })
            .show()
    }

    /**
     * 关闭等待框
     */
    private fun dismissLoading() {
        dialog?.run { if (isShowing) dismiss() }
    }


    /**
     * 创建 ViewModel
     */
    @Suppress("UNCHECKED_CAST")
    private fun createViewModel(tp: Type) {
        val tClass = tp as? Class<VM> ?: BaseViewModel::class.java
        val viewModelFactory =
            MVVMLin.getConfig().viewModelFactory() ?: defaultViewModelProviderFactory
        viewModel = ViewModelProvider(this, viewModelFactory).get(tClass) as VM
    }

}