package com.pcl.mvvm.data

import com.aleyn.mvvm.base.BaseModel
import com.pcl.mvvm.app.base.BaseResult
import com.pcl.mvvm.data.db.dao.HomeDao
import com.pcl.mvvm.data.http.HomeNetWork
import com.pcl.mvvm.network.entity.BannerBean
import com.pcl.mvvm.network.entity.HomeListBean
import com.pcl.mvvm.network.entity.NavTypeBean
import com.pcl.mvvm.network.entity.UsedWeb

/**
 *   @auther : Aleyn
 *   time   : 2019/10/29
 */
class HomeRepository private constructor(
    private val netWork: HomeNetWork,
    private val localData: HomeDao
) : BaseModel() {

    suspend fun getBannerData(refresh: Boolean = false): List<BannerBean> {
        return cacheNetCall({
            netWork.getBannerData()
        }, {
            localData.getBannerList()
        }, {
            if (refresh) localData.deleteBannerAll()
            localData.insertBanner(it)
        }, {
            !refresh && !it.isNullOrEmpty()
        })
    }

    suspend fun getHomeList(page: Int, refresh: Boolean): HomeListBean {
        return cacheNetCall({
            netWork.getHomeList(page)
        }, {
            localData.getHomeList(page + 1)
        }, {
            if (refresh) localData.deleteHomeAll()
            localData.insertData(it)
        }, {
            !refresh
        })
    }

    suspend fun getNaviJson(): BaseResult<List<NavTypeBean>> {
        return netWork.getNaviJson()
    }

    suspend fun getProjectList(page: Int, cid: Int): BaseResult<HomeListBean> {
        return netWork.getProjectList(page, cid)
    }

    suspend fun getPopularWeb(): BaseResult<MutableList<UsedWeb>> {
        return netWork.getPopularWeb()
    }

    companion object {

        @Volatile
        private var INSTANCE: HomeRepository? = null

        fun getInstance(netWork: HomeNetWork, homeDao: HomeDao) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HomeRepository(netWork, homeDao).also { INSTANCE = it }
            }
    }
}