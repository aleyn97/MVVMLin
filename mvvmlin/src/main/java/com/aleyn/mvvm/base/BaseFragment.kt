package com.aleyn.mvvm.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewbinding.ViewBinding
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.aleyn.mvvm.R
import com.aleyn.mvvm.event.Message
import com.aleyn.mvvm.extend.flowLaunch
import java.lang.reflect.InvocationTargetException
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

    /**
     * 注册 UI 事件
     */
    fun registerDefUIChange(viewModel: BaseViewModel) {
        flowLaunch {
            viewModel.waitDialog.collect {
                if (it.isShow) showLoading(it.tipsText) else dismissLoading()
            }
        }
        flowLaunch {
            viewModel.msgEvent.collect(::handleEvent)
        }
    }

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
    @SuppressLint("CheckResult")
    protected fun showLoading(tips: String = "") {
        (dialog ?: MaterialDialog(requireContext())
            .cancelable(false)
            .cornerRadius(8f)
            .customView(R.layout.custom_progress_dialog_view, noVerticalPadding = true)
            .lifecycleOwner(this)
            .maxWidth(R.dimen.dialog_width)
            .also {
                dialog = it
            }).apply {
            if (tips.isNotBlank()) {
                getCustomView().findViewById<TextView>(R.id.tvTip).text = tips
            }
            if (!isShowing) show()
        }
    }

    /**
     * 关闭等待框
     */
    protected fun dismissLoading() {
        dialog?.run { if (isShowing) dismiss() }
    }


    @Suppress("UNCHECKED_CAST")
    open fun initBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        var type = javaClass.genericSuperclass
        var superclass = javaClass.superclass
        while (superclass != null) {
            if (type is ParameterizedType) {
                try {
                    type.actualTypeArguments.find {
                        ViewBinding::class.java.isAssignableFrom(it as Class<*>)
                    }?.let {
                        val cls = it as Class<VB>
                        val method = cls.getDeclaredMethod(
                            "inflate",
                            LayoutInflater::class.java,
                            ViewGroup::class.java,
                            Boolean::class.java
                        )
                        _binding = method.invoke(null, inflater, container, false) as VB
                        return mBinding.root
                    }
                } catch (_: NoSuchMethodException) {
                } catch (_: ClassCastException) {
                } catch (e: InvocationTargetException) {
                    throw e.targetException
                }
            }
            type = superclass.genericSuperclass
            superclass = superclass.superclass
        }
        throw IllegalArgumentException("ViewBinding class not found")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}