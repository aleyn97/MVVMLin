package com.pcl.mvvm.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.recyclerview.widget.LinearLayoutManager
import com.aleyn.mvvm.base.BaseFragment
import com.pcl.mvvm.R
import com.pcl.mvvm.databinding.HomeFragmentBinding
import com.pcl.mvvm.network.entity.ArticlesBean
import com.pcl.mvvm.network.entity.BannerBean
import com.pcl.mvvm.ui.detail.DetailActivity
import com.pcl.mvvm.utils.GlideImageLoader
import com.youth.banner.Banner

/**
 * 此页面使用 ViewBinding
 *   @auther : Aleyn
 *   time   : 2019/11/02
 */
class HomeFragment : BaseFragment<HomeViewModel, HomeFragmentBinding>() {

    private val mAdapter by lazy { HomeListAdapter() }
    private var page: Int = 0
    private lateinit var banner: Banner<BannerBean, GlideImageLoader>

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun initView(savedInstanceState: Bundle?) {
        with(mBinding.rvHome) {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            //banner
            banner = Banner(context)
            banner.minimumWidth = MATCH_PARENT
            banner.layoutParams =
                ViewGroup.LayoutParams(MATCH_PARENT, resources.getDimension(R.dimen.dp_120).toInt())
            banner.adapter = GlideImageLoader()
        }
        mAdapter.apply {
            addHeaderView(banner)
            loadMoreModule.setOnLoadMoreListener(this@HomeFragment::loadMore)
            setOnItemClickListener { adapter, _, position ->
                val item = adapter.data[position] as ArticlesBean
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("url", item.link)
                startActivity(intent)
            }
        }
        mBinding.srlHome.setOnRefreshListener {
            dropDownRefresh()
        }
    }

    override fun lazyLoadData() {
        viewModel.run {

            getBanner().observe(this@HomeFragment, {
                banner.setDatas(it)
            })

            getHomeList(page).observe(this@HomeFragment, {
                if (mBinding.srlHome.isRefreshing) mBinding.srlHome.isRefreshing = false
                it?.let {
                    if (it.curPage == 1) mAdapter.setNewInstance(it.datas)
                    else mAdapter.addData(it.datas)
                    if (it.curPage == it.pageCount) mAdapter.loadMoreModule.loadMoreEnd()
                    else mAdapter.loadMoreModule.loadMoreComplete()
                    page = it.curPage
                }
            })
        }
    }

    /**
     * 下拉刷新
     */
    private fun dropDownRefresh() {
        page = 0
        mBinding.srlHome.isRefreshing = true
        viewModel.getHomeList(page, true)
        viewModel.getBanner(true)
    }

    /**
     * 加载更多
     */
    private fun loadMore() {
        viewModel.getHomeList(page + 1)
    }

}
