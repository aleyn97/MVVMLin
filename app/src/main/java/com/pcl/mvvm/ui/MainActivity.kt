package com.pcl.mvvm.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.aleyn.mvvm.base.BaseActivity
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.PermissionUtils
import com.pcl.mvvm.R
import com.pcl.mvvm.databinding.ActivityMainBinding
import com.pcl.mvvm.ui.home.HomeFragment
import com.pcl.mvvm.ui.me.MeFragment
import com.pcl.mvvm.ui.project.ProjectFragment

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val fragments = ArrayList<Fragment>()

    private lateinit var showFragment: Fragment

    override fun initView(savedInstanceState: Bundle?) {
        BarUtils.setStatusBarColor(this, ColorUtils.getColor(R.color.colorPrimary))
        fragments.add(HomeFragment.newInstance())
        fragments.add(ProjectFragment.newInstance())
        fragments.add(MeFragment.newInstance())
        showFragment = fragments[0]
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, showFragment)
            .commitNow()

        mBinding.bottomNavigationView.setOnItemSelectedListener {
            switchPage(it.itemId)
            return@setOnItemSelectedListener true
        }
        PermissionUtils.permission(*PermissionUtils.getPermissions().toTypedArray())
            .callback(object : PermissionUtils.FullCallback {
                override fun onGranted(granted: MutableList<String>) {

                }

                override fun onDenied(
                    forever: MutableList<String>, denied: MutableList<String>
                ) {

                }

            })
            .request()
    }

    private fun switchPage(itemId: Int) {
        val index = when (itemId) {
            R.id.action_home -> 0
            R.id.action_project -> 1
            R.id.action_me -> 2
            else -> return
        }
        val now = fragments[index]
        supportFragmentManager.beginTransaction().apply {
            if (!now.isAdded) {
                add(R.id.container, now)
            }
            hide(showFragment)
            show(now)
            showFragment = now
            commit()
        }
    }

    override fun initData() {
    }
}
