package com.aleyn.mvvm.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.aleyn.mvvm.R
import com.aleyn.mvvm.event.Message
import java.lang.reflect.ParameterizedType

/**
 *   @author : Aleyn
 *   time   : 2019/11/01
 */
abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null

    protected val mBinding get() = _binding!!

    //是否第一次加载
    private var isFirst: Boolean = true

    private var dialog: MaterialDialog? = null

    /**
     * 使用 DataBinding时,要重写此方法返回相应的布局 id
     * 使用ViewBinding时，不用重写此方法
     */
    @LayoutRes
    open val layoutId = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return initBinding(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onVisible()
        initView(savedInstanceState)
        initObserve()
    }

    open fun initObserve() {}

    open fun initView(savedInstanceState: Bundle?) {}

    override fun onResume() {
        super.onResume()
        onVisible()
    }

    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            lazyLoadData()
            isFirst = false
        }
    }

    /**
     * 懒加载
     */
    open fun lazyLoadData() {}

    open fun handleEvent(msg: Message) {}

    /**
     * 打开等待框
     */
    protected fun showLoading() {
        (dialog ?: MaterialDialog(requireContext())
            .cancelable(false)
            .cornerRadius(8f)
            .customView(R.layout.custom_progress_dialog_view, noVerticalPadding = true)
            .lifecycleOwner(this)
            .maxWidth(R.dimen.dialog_width)
            .also {
                dialog = it
            }).show()
    }

    /**
     * 关闭等待框
     */
    protected fun dismissLoading() {
        dialog?.run { if (isShowing) dismiss() }
    }


    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val cls = type.actualTypeArguments[1] as Class<*>
            return when {
                ViewDataBinding::class.java.isAssignableFrom(cls) && cls != ViewDataBinding::class.java -> {
                    if (layoutId == 0) throw IllegalArgumentException("Using DataBinding requires overriding method layoutId")
                    _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
                    (mBinding as ViewDataBinding).lifecycleOwner = this
                    mBinding.root
                }
                ViewBinding::class.java.isAssignableFrom(cls) && cls != ViewBinding::class.java -> {
                    cls.getDeclaredMethod("inflate", LayoutInflater::class.java).let {
                        @Suppress("UNCHECKED_CAST")
                        _binding = it.invoke(null, inflater) as VB
                        mBinding.root
                    }
                }
                else -> {
                    if (layoutId == 0) throw IllegalArgumentException("If you don't use ViewBinding, you need to override method layoutId")
                    inflater.inflate(layoutId, container, false)
                }
            }
        } else throw IllegalArgumentException("Generic error")
    }

}