package com.pcl.mvvm.ui.project

import com.aleyn.mvvm.base.BaseViewModel
import com.aleyn.mvvm.extend.getOrThrow
import com.pcl.mvvm.network.entity.ArticlesBean
import com.pcl.mvvm.network.entity.NavTypeBean
import com.pcl.mvvm.utils.InjectorUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 *   @author : Aleyn
 *   time   : 2019/11/12
 */
class ProjectViewModel : BaseViewModel() {

    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }

    private val _navData = MutableStateFlow<List<NavTypeBean>>(emptyList())
    val navData = _navData.asStateFlow()

    private val _items = MutableStateFlow<List<ArticlesBean>>(emptyList())
    val items = _items.asStateFlow()

    private var page: Int = 0

    var currentIndex = -1
        private set

    /**
     * 顺序请求
     */
    fun getFirstData() {
        launch {
            //tab 数据
            val navResult = homeRepository.getNaviJson().getOrThrow()
            _navData.update { navResult }

            //tab对应列表数据
            getProjectList(0)
        }
    }

    fun getProjectList(index: Int = currentIndex) {
        val cid = navData.value.getOrNull(index)?.id ?: return
        launch {
            val list = homeRepository.getProjectList(page, cid).getOrThrow()
            val newList = ArrayList<ArticlesBean>()
            if (currentIndex == index) {
                newList.addAll(items.value)
            }
            newList.addAll(list.datas)
            _items.update { newList }
            currentIndex = index
        }
    }
}