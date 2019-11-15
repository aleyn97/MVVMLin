package com.pcl.mvvm.ui.me

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.aleyn.mvvm.base.BaseFragment
import com.pcl.mvvm.R
import com.pcl.mvvm.databinding.MeFragmentBinding
import com.pcl.mvvm.network.entity.UsedWeb
import com.pcl.mvvm.ui.detail.DetailActivity
import kotlinx.android.synthetic.main.me_fragment.*

class MeFragment : BaseFragment<MeViewModel, MeFragmentBinding>() {

    private val mAdapter by lazy { MeWebAdapter() }

    companion object {
        fun newInstance() = MeFragment()
    }

    override fun layoutId() = R.layout.me_fragment

    override fun initView(savedInstanceState: Bundle?) {
        with(rv_me_uesd_web) {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        viewModel.popularWeb.observe(viewLifecycleOwner, Observer {
            mAdapter.setNewData(it)
        })
        mAdapter.setOnItemClickListener { _, _, position ->
            val intent = Intent().apply {
                setClass(activity!!, DetailActivity::class.java)
                putExtra("url", (mAdapter.data[position] as UsedWeb).link)
            }
            startActivity(intent)
        }
    }

    override fun lazyLoadData() {
        viewModel.getPopularWeb()
    }
}
