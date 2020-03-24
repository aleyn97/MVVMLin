package com.pcl.mvvm.utils

import com.pcl.mvvm.data.HomeRepository
import com.pcl.mvvm.data.db.LinDatabase
import com.pcl.mvvm.data.http.HomeNetWork

object InjectorUtil {

    fun getHomeRepository() = HomeRepository.getInstance(
        HomeNetWork.getInstance(),
        LinDatabase.getInstanse().homeLocaData()
    )

}