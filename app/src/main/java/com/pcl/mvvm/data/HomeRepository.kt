package com.pcl.mvvm.data

import com.aleyn.mvvm.base.BaseModel
import com.aleyn.mvvm.base.BaseResult
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
    private val netWork: HomeNetWork
//    private val localData: LocalData   可用 Room 等 来做本地缓存，Demo 只展示了从网络获取
) : BaseModel() {

    suspend fun getBannerData(): BaseResult<List<BannerBean>> {
        return netWork.getBannerData()
    }

    suspend fun getHomeList(page: Int): BaseResult<HomeListBean> {
        return netWork.getHomeList(page)
    }

    suspend fun getNaviJson(): BaseResult<List<NavTypeBean>> {
        return netWork.getNaviJson()
    }

    suspend fun getProjectList(page: Int, cid: Int): BaseResult<HomeListBean> {
        return netWork.getProjectList(page, cid)
    }

    suspend fun getPopularWeb(): BaseResult<List<UsedWeb>> {
        return netWork.getPopularWeb()
    }

    companion object {

        @Volatile
        private var INSTANCE: HomeRepository? = null

        fun getInstance(netWork: HomeNetWork) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: HomeRepository(netWork)
            }
    }
}