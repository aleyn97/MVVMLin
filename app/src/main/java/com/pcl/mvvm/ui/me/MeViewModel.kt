package com.pcl.mvvm.ui.me

import androidx.lifecycle.MutableLiveData
import com.aleyn.mvvm.base.BaseViewModel
import com.pcl.mvvm.network.entity.UsedWeb
import com.pcl.mvvm.utils.InjectorUtil

/**
 *   @auther : Aleyn
 *   time   : 2019/11/14
 */
class MeViewModel : BaseViewModel() {

    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }

    var popularWeb = MutableLiveData<MutableList<UsedWeb>>()

    fun getPopularWeb() {
        launchGo({
            val result = homeRepository.getPopularWeb()
            if (result.isSuccess()) {
                popularWeb.value = result.data
            }
        })
    }
}