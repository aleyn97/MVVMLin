package com.pcl.mvvm.ui.detail

import android.os.Build
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import com.aleyn.mvvm.base.BaseActivity
import com.aleyn.mvvm.base.NoViewModel
import com.pcl.mvvm.databinding.ActivityDetailBinding

class DetailActivity : BaseActivity<NoViewModel, ActivityDetailBinding>() {


    override fun initView(savedInstanceState: Bundle?) {
        initWebView()
    }

    override fun initData() {
        intent.getStringExtra("url")?.let {
            mBinding.wvDetail.loadUrl(it)
        }
    }

    private fun initWebView() {
        mBinding.wvDetail.setInitialScale(100)
        mBinding.wvDetail.webViewClient = webViewClient
        val ws = mBinding.wvDetail.settings
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
            textZoom = 100
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
    }

    private val webViewClient = object : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            url?.let { view?.loadUrl(it) }
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
