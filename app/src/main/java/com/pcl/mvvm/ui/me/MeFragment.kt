package com.pcl.mvvm.ui.me

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.aleyn.mvvm.base.BaseFragment
import com.pcl.mvvm.R
import com.pcl.mvvm.databinding.MeFragmentBinding
import com.pcl.mvvm.ui.detail.DetailActivity

class MeFragment : BaseFragment<MeViewModel, MeFragmentBinding>() {

    private val mAdapter by lazy { MeWebAdapter() }

    companion object {
        fun newInstance() = MeFragment()
    }

    override fun layoutId() = R.layout.me_fragment

    override fun initView(savedInstanceState: Bundle?) {
        with(mBinding.rvMeUesdWeb) {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
        viewModel.popularWeb.observe(viewLifecycleOwner, {
            mAdapter.setNewInstance(it)
        })
        mAdapter.setOnItemClickListener { _, _, position ->
            val intent = Intent().apply {
                setClass(activity!!, DetailActivity::class.java)
                putExtra("url", (mAdapter.data[position]).link)
            }
            startActivity(intent)
        }
    }

    override fun lazyLoadData() {
        viewModel.getPopularWeb()
    }
}
