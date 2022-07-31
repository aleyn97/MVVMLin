package com.pcl.mvvm.network.api

import com.aleyn.cache.CacheStrategy
import com.pcl.mvvm.app.base.BaseResult
import com.pcl.mvvm.network.entity.BannerBean
import com.pcl.mvvm.network.entity.HomeListBean
import com.pcl.mvvm.network.entity.NavTypeBean
import com.pcl.mvvm.network.entity.UsedWeb
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

/**
 *   @author : Aleyn
 *   time   : 2019/10/29
 */
interface HomeService {

    /**
     * 玩安卓轮播图
     */
    @GET("banner/json")
    fun getBanner(
        @Header(CacheStrategy.CACHE_MODE) cacheModel: String
    ): Flow<BaseResult<List<BannerBean>>>


    /**
     * 导航数据
     */
    @GET("project/tree/json")
    suspend fun naviJson(): BaseResult<List<NavTypeBean>>


    /**
     * 项目列表
     * @param page 页码，从0开始
     */
    @GET("article/listproject/{page}/json")
    fun getHomeList(
        @Path("page") page: Int,
        @Header(CacheStrategy.CACHE_MODE) cacheModel: String
    ): Flow<BaseResult<HomeListBean>>


    /**
     * 项目列表
     * @param page 页码，从0开始
     */
    @GET("project/list/{page}/json")
    suspend fun getProjectList(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): BaseResult<HomeListBean>


    /**
     * 常用网站
     */
    @GET("friend/json")
    suspend fun getPopularWeb(): BaseResult<MutableList<UsedWeb>>
}