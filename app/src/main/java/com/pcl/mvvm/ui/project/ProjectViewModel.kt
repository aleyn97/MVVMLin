package com.pcl.mvvm.ui.project

import androidx.databinding.ObservableArrayList
import com.aleyn.mvvm.app.MVVMLin
import com.aleyn.mvvm.base.BaseViewModel
import com.aleyn.mvvm.event.Message
import com.aleyn.mvvm.network.ResponseThrowable
import com.blankj.utilcode.util.LogUtils
import com.google.android.material.tabs.TabLayout
import com.pcl.mvvm.BR
import com.pcl.mvvm.R
import com.pcl.mvvm.network.entity.ArticlesBean
import com.pcl.mvvm.network.entity.NavTypeBean
import com.pcl.mvvm.utils.InjectorUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import me.tatarka.bindingcollectionadapter2.ItemBinding

/**
 *   @auther : Aleyn
 *   time   : 2019/11/12
 */
class ProjectViewModel : BaseViewModel() {

    private val homeRepository by lazy { InjectorUtil.getHomeRepository() }
    private val itemOnClickListener = object : OnItemClickListener {
        override fun onItemClick(item: ArticlesBean) {
            defUI.msgEvent.postValue(Message(0, obj = item))
        }
    }
    var navTitle = ObservableArrayList<String>()
    var navData = ObservableArrayList<NavTypeBean>()
    var items = ObservableArrayList<ArticlesBean>()
    var itemBinding = ItemBinding.of<ArticlesBean>(BR.itemBean, R.layout.item_project_list)
        .bindExtra(BR.listenner, itemOnClickListener)

    private var page: Int = 0


    /**
     * 当一个请求结果，依赖另一个请求结果的时候，我们可以用 流的方式如下：
     *  以此类推，还可以 用  zip 操作符 对多个请求进行合并，以及 flatMapMerge、flatMapConcat 等。
     *  熟悉 RxJava 的你，分分钟钟可以上手的  （斜眼笑  `-` ）
     */
    @ExperimentalCoroutinesApi
    @FlowPreview
    fun getFirstData() {
        launchUI {
            launchFlow { homeRepository.getNaviJson() }
                .flatMapConcat {
                    return@flatMapConcat if (it.isSuccess()) {
                        navData.addAll(it.data)
                        it.data.forEach { item -> navTitle.add(item.name) }
                        launchFlow { homeRepository.getProjectList(page, it.data[0].id) }
                    } else throw ResponseThrowable(it.errorCode, it.errorMsg)
                }
                .onStart { defUI.showDialog.postValue(null) }
                .flowOn(Dispatchers.IO)
                .onCompletion { defUI.dismissDialog.call() }
                .catch {
                    // 错误处理
                    val err = MVVMLin.getConfig().globalExceptionHandle(it)
                    LogUtils.d("${err.code}: ${err.errMsg}")
                }
                .collect {
                    if (it.isSuccess()) items.addAll(it.data.datas)
                }
        }

    }

    fun getProjectList(cid: Int) {
        launchOnlyresult({ homeRepository.getProjectList(page, cid) }, {
            items.clear()
            items.addAll(it.datas)
        })
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