package com.pcl.mvvm.ui.home

import androidx.lifecycle.MutableLiveData
import com.aleyn.mvvm.base.BaseViewModel
import com.pcl.mvvm.network.entity.BannerBean
import com.pcl.mvvm.network.entity.HomeListBean
import com.pcl.mvvm.utils.InjectorUtil

class HomeViewModel : BaseViewModel() {

    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }

    val mBanners = MutableLiveData<List<BannerBean>>()

    val projectData = MutableLiveData<HomeListBean>()

    fun getBanner() {
        launchOnlyresult({ homeRepository.getBannerData() }, {
            mBanners.value = it
        })
    }

    fun getHomeList(page: Int) {
        launchOnlyresult({ homeRepository.getHomeList(page) }, {
            projectData.value = it
        })
    }
}
