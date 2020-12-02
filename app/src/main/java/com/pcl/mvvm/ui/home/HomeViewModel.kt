package com.pcl.mvvm.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.aleyn.mvvm.base.BaseViewModel
import com.pcl.mvvm.data.HomeRepository
import com.pcl.mvvm.network.entity.BannerBean
import com.pcl.mvvm.network.entity.HomeListBean

class HomeViewModel @ViewModelInject constructor(
    private val homeRepository: HomeRepository
) : BaseViewModel() {

    private val mBanners = MutableLiveData<List<BannerBean>>()

    private val projectData = MutableLiveData<HomeListBean>()

    fun getBanner(refresh: Boolean = false): MutableLiveData<List<BannerBean>> {
        launchGo({
            mBanners.value = homeRepository.getBannerData(refresh)
        })
        return mBanners
    }

    /**
     * @param page 页码
     * @param refresh 是否刷新
     */
    fun getHomeList(page: Int, refresh: Boolean = false): MutableLiveData<HomeListBean> {
        launchGo({
            projectData.value = homeRepository.getHomeList(page, refresh)
        }, {
            projectData.value = null
        })
        return projectData
    }
}
