package com.pcl.mvvm.data

import com.aleyn.mvvm.base.BaseModel
import com.pcl.mvvm.app.base.BaseResult
import com.pcl.mvvm.data.db.dao.HomeDao
import com.pcl.mvvm.network.api.HomeService
import com.pcl.mvvm.network.entity.BannerBean
import com.pcl.mvvm.network.entity.HomeListBean
import com.pcl.mvvm.network.entity.NavTypeBean
import com.pcl.mvvm.network.entity.UsedWeb
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

/**
 *   @auther : Aleyn
 *   time   : 2019/10/29
 */
@ActivityScoped
class HomeRepository @Inject constructor(
    private val localData: HomeDao
) : BaseModel() {

    suspend fun getBannerData(refresh: Boolean = false): List<BannerBean> {
        return cacheNetCall({
            netWork<HomeService>().getBanner()
        }, {
            localData.getBannerList()
        }, {
            if (refresh) localData.deleteBannerAll()
            localData.insertBanner(it)
        }, {
            !refresh && it != null && it.isNotEmpty()
        })
    }

    suspend fun getHomeList(page: Int, refresh: Boolean): HomeListBean {
        return cacheNetCall({
            netWork<HomeService>().getHomeList(page)
        }, {
            localData.getHomeList(page + 1)
        }, {
            if (refresh) localData.deleteHomeAll()
            localData.insertData(it)
        }, {
            !refresh && it != null
        })
    }

    suspend fun getNaviJson(): BaseResult<List<NavTypeBean>> {
        return netWork<HomeService>().naviJson()
    }

    suspend fun getProjectList(page: Int, cid: Int): BaseResult<HomeListBean> {
        return netWork<HomeService>().getProjectList(page, cid)
    }

    suspend fun getPopularWeb(): BaseResult<MutableList<UsedWeb>> {
        return netWork<HomeService>().getPopularWeb()
    }
}