package com.pcl.mvvm.ui.home

import com.aleyn.mvvm.base.BaseViewModel
import com.aleyn.mvvm.extend.asResponse
import com.aleyn.mvvm.extend.bindLoading
import com.pcl.mvvm.network.entity.BannerBean
import com.pcl.mvvm.network.entity.HomeListBean
import com.pcl.mvvm.utils.InjectorUtil
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.onCompletion

class HomeViewModel : BaseViewModel() {

    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }

    private val _banners = MutableSharedFlow<List<BannerBean>>()
    val mBanners: SharedFlow<List<BannerBean>> = _banners

    private val _projectData = MutableSharedFlow<HomeListBean>()
    val projectData: SharedFlow<HomeListBean> = _projectData

    private val _refreshState = MutableSharedFlow<Unit>()
    val refreshState: SharedFlow<Unit> = _refreshState

    /**
     * Banner
     */
    fun getBanner(refresh: Boolean = false) {
        launch {
            homeRepository.getBannerData(refresh)
                .asResponse()
                .collect(_banners)
        }
    }

    /**
     * @param page 页码
     * @param refresh 是否刷新
     */
    fun getHomeList(page: Int, refresh: Boolean = false) {
        launch {
            homeRepository.getHomeList(page, refresh)
                .asResponse()
                .onCompletion { if (refresh) _refreshState.emit(Unit) }
                .bindLoading(this@HomeViewModel)
                .collect(_projectData)
        }
    }
}
