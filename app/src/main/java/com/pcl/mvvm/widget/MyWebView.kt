package com.pcl.mvvm.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.AttributeSet
import android.webkit.WebView


/**
 *   @auther : Aleyn
 *   time   : 2019/11/14
 */
@SuppressLint("ViewConstructor", "NewApi")
class MyWebView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : WebView(getFixedContext(context), attrs, defStyleAttr, defStyleRes) {

    companion object {
        fun getFixedContext(context: Context): Context {
            return if (Build.VERSION.SDK_INT in 21..22) context.createConfigurationContext(
                Configuration()
            ) else context
        }
    }
}