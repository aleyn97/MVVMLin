package com.pcl.mvvm.ui.project

import android.content.Intent
import android.os.Bundle
import com.aleyn.mvvm.base.BaseFragment
import com.aleyn.mvvm.event.Message
import com.pcl.mvvm.R
import com.pcl.mvvm.databinding.ProjectFragmentBinding
import com.pcl.mvvm.network.entity.ArticlesBean
import com.pcl.mvvm.ui.detail.DetailActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

class ProjectFragment : BaseFragment<ProjectViewModel, ProjectFragmentBinding>() {


    companion object {
        fun newInstance() = ProjectFragment()
    }

    override fun layoutId() = R.layout.project_fragment

    override fun initView(savedInstanceState: Bundle?) {
        mBinding.viewModel = viewModel
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun lazyLoadData() {
        viewModel.getFirstData()
    }

    override fun handleEvent(msg: Message) {
        when (msg.code) {
            0 -> {
                val bean = msg.obj as ArticlesBean
                val intent = Intent().apply {
                    setClass(activity!!, DetailActivity::class.java)
                    putExtra("url", bean.link)
                }
                startActivity(intent)
            }
        }
    }
}
