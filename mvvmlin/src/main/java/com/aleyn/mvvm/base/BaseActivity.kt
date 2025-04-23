package com.aleyn.mvvm.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewbinding.ViewBinding
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.aleyn.mvvm.R
import com.aleyn.mvvm.event.Message
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType

/**
 *   @author : Aleyn
 *   time   : 2019/11/01
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected lateinit var mBinding: VB

    private var dialog: MaterialDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(initBinding())
        initView(savedInstanceState)
        initObserve()
        initData()
    }

    abstract fun initView(savedInstanceState: Bundle?)

    open fun initObserve() {}

    abstract fun initData()


    /**
     * ViewBinding
     */
    @Suppress("UNCHECKED_CAST")
    open fun initBinding(): View {
        var type = javaClass.genericSuperclass
        var superclass = javaClass.superclass
        while (superclass != null) {
            if (type is ParameterizedType) {
                try {
                    type.actualTypeArguments.find {
                        ViewBinding::class.java.isAssignableFrom(it as Class<*>)
                    }?.let {
                        val cls = it as Class<VB>
                        val method = cls.getDeclaredMethod("inflate", LayoutInflater::class.java)
                        mBinding = method.invoke(null, layoutInflater) as VB
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

    open fun handleEvent(msg: Message) {}

    /**
     * 打开等待框
     */
    protected fun showLoading() {
        (dialog ?: MaterialDialog(this)
            .cancelable(false)
            .cornerRadius(8f)
            .customView(R.layout.custom_progress_dialog_view, noVerticalPadding = true)
            .lifecycleOwner(this)
            .maxWidth(R.dimen.dialog_width).also {
                dialog = it
            })
            .show()
    }

    /**
     * 关闭等待框
     */
    protected fun dismissLoading() {
        dialog?.run { if (isShowing) dismiss() }
    }

}