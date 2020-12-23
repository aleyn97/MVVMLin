package com.pcl.mvvm.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.aleyn.mvvm.base.BaseFragment
import com.blankj.utilcode.util.AdaptScreenUtils
import com.pcl.mvvm.R
import com.pcl.mvvm.network.entity.ArticlesBean
import com.pcl.mvvm.ui.detail.DetailActivity
import com.pcl.mvvm.utils.GlideImageLoader
import com.stx.xhb.androidx.XBanner
import kotlinx.android.synthetic.main.home_fragment.*

/**
 *   @auther : Aleyn
 *   time   : 2019/11/02
 */
class HomeFragment : BaseFragment<HomeViewModel, ViewDataBinding>() {

    private val mAdapter by lazy { HomeListAdapter() }
    private var page: Int = 0
    private lateinit var banner: XBanner

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun layoutId() = R.layout.home_fragment

    override fun initView(savedInstanceState: Bundle?) {
        with(rv_home) {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            //banner
            banner = XBanner(context)
            banner.minimumWidth = MATCH_PARENT
            banner.layoutParams =
                ViewGroup.LayoutParams(MATCH_PARENT, resources.getDimension(R.dimen.dp_120).toInt())
            banner.loadImage(GlideImageLoader())
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
        srl_home.setOnRefreshListener {
            dropDownRefresh()
        }
    }

    override fun lazyLoadData() {
        viewModel.run {

            getBanner().observe(this@HomeFragment, {
                banner.setBannerData(it)
            })

            getHomeList(page).observe(this@HomeFragment, {
                if (srl_home.isRefreshing) srl_home.isRefreshing = false
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
        srl_home.isRefreshing = true
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
