package com.pcl.mvvm.ui.detail

import android.os.Build
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.databinding.ViewDataBinding
import com.aleyn.mvvm.base.BaseActivity
import com.aleyn.mvvm.base.NoViewModel
import com.pcl.mvvm.R
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : BaseActivity<NoViewModel, ViewDataBinding>() {


    override fun layoutId() = R.layout.activity_detail

    override fun initView(savedInstanceState: Bundle?) {
        initWebView()
    }

    override fun initData() {
        intent.getStringExtra("url")?.let {
            wv_detail.loadUrl(it)
        }
    }

    private fun initWebView() {
        wv_detail.setInitialScale(100)
        wv_detail.webViewClient = webViewClient
        val ws = wv_detail.settings
        with(ws) {
            loadWithOverviewMode = false
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            setAppCacheEnabled(true)
            cacheMode = WebSettings.LOAD_DEFAULT
            useWideViewPort = true
            blockNetworkImage = false
            domStorageEnabled = true
            layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
            textZoom = 100
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
    }

    private val webViewClient = object : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            view?.loadUrl(url)
            return true
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            view?.loadUrl(request?.url.toString())
            return true
        }
    }

}
