package com.pcl.mvvm.ui.project

import androidx.databinding.ObservableArrayList
import com.aleyn.mvvm.base.BaseViewModel
import com.aleyn.mvvm.event.Message
import com.aleyn.mvvm.extend.getOrThrow
import com.google.android.material.tabs.TabLayout
import com.pcl.mvvm.BR
import com.pcl.mvvm.R
import com.pcl.mvvm.network.entity.ArticlesBean
import com.pcl.mvvm.network.entity.NavTypeBean
import com.pcl.mvvm.utils.InjectorUtil
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 *   @author : Aleyn
 *   time   : 2019/11/12
 */
class ProjectViewModel : BaseViewModel() {

    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }
    private val itemOnClickListener = object : OnItemClickListener {
        override fun onItemClick(item: ArticlesBean) {
            launch { defUI.msgEvent.emit(Message(0, obj = item)) }
        }
    }
    var navTitle = ObservableArrayList<String>()
    var navData = ObservableArrayList<NavTypeBean>()
    var items = ObservableArrayList<ArticlesBean>()
    var itemBinding = ItemBinding.of<ArticlesBean>(BR.itemBean, R.layout.item_project_list)
        .bindExtra(BR.listenner, itemOnClickListener)

    private var page: Int = 0

    /**
     * 顺序请求
     */
    fun getFirstData() {
        launch {
            //tab 数据
            val navResult = homeRepository.getNaviJson().getOrThrow()
            navData.addAll(navResult)
            navResult.forEach { item -> navTitle.add(item.name) }

            //tab对应列表数据
            val listBean = homeRepository.getProjectList(page, navResult.first().id).getOrThrow()
            items.addAll(listBean.datas)
        }
    }

    fun getProjectList(cid: Int) {
        launch {
            homeRepository.getProjectList(page, cid).getOrThrow().let {
                items.clear()
                items.addAll(it.datas)
            }
        }
    }


    var tabOnClickListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(p0: TabLayout.Tab?) {
        }

        override fun onTabUnselected(p0: TabLayout.Tab?) {
        }

        override fun onTabSelected(p0: TabLayout.Tab?) {
            p0?.let {
                getProjectList(navData[it.position].id)
            }
        }
    }


    interface OnItemClickListener {
        fun onItemClick(item: ArticlesBean)
    }
}