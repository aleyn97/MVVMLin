package com.pcl.mvvm.data.http

import com.pcl.mvvm.network.api.HomeService
import com.pcl.mvvm.utils.RetrofitClient

/**
 *   @auther : Aleyn
 *   time   : 2019/11/01
 */
class HomeNetWork {

    private val mService by lazy { RetrofitClient.getInstance().create(HomeService::class.java) }

    suspend fun getBannerData() = mService.getBanner()

    suspend fun getHomeList(page: Int) = mService.getHomeList(page)

    suspend fun getNaviJson() = mService.naviJson()

    suspend fun getProjectList(page: Int, cid: Int) = mService.getProjectList(page, cid)

    suspend fun getPopularWeb() = mService.getPopularWeb()


    companion object {
        @Volatile
        private var netWork: HomeNetWork? = null

        fun getInstance() = netWork ?: synchronized(this) {
            netWork ?: HomeNetWork().also { netWork = it }
        }
    }


}