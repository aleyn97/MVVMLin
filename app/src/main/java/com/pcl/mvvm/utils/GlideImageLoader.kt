package com.pcl.mvvm.utils

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.pcl.mvvm.network.entity.BannerBean
import com.stx.xhb.androidx.XBanner

/**
 *   @auther : Aleyn
 *   time   : 2019/09/05
 */
class GlideImageLoader : XBanner.XBannerAdapter {

    override fun loadBanner(banner: XBanner?, model: Any?, view: View?, position: Int) {
        Glide.with(banner!!.context).load((model as BannerBean).xBannerUrl).into(view as ImageView)
    }

}