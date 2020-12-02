package com.pcl.mvvm.ui.me

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.aleyn.mvvm.base.BaseViewModel
import com.pcl.mvvm.data.HomeRepository
import com.pcl.mvvm.network.entity.UsedWeb

/**
 *   @auther : Aleyn
 *   time   : 2019/11/14
 */
class MeViewModel @ViewModelInject constructor(
    private val homeRepository: HomeRepository
) : BaseViewModel() {

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