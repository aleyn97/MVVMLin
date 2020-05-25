package com.pcl.mvvm.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.BarUtils
import com.pcl.mvvm.R
import com.pcl.mvvm.ui.home.HomeFragment
import com.pcl.mvvm.ui.me.MeFragment
import com.pcl.mvvm.ui.project.ProjectFragment
import kotlinx.android.synthetic.main.activity_main.*
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private val fragments = ArrayList<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        BarUtils.setStatusBarColor(this, resources.getColor(R.color.colorPrimary))
        initView()
    }

    private fun initView() {
        fragments.add(HomeFragment.newInstance())
        fragments.add(ProjectFragment.newInstance())
        fragments.add(MeFragment.newInstance())
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, fragments[0])
            .commitNow()

        val navCtl = page_navigation_view.material()
            .addItem(R.drawable.tab_shop_selected, "首页")
            .addItem(R.drawable.tab_car_selected, "项目")
            .addItem(R.drawable.tab_me_selected, "我的")
            .build()

        navCtl.addTabItemSelectedListener(object : OnTabItemSelectedListener {

            override fun onSelected(index: Int, old: Int) {
                switchPage(index, old)
            }

            override fun onRepeat(index: Int) {
            }
        })

    }

    private fun switchPage(index: Int, old: Int) {
        val now = fragments[index]
        supportFragmentManager.beginTransaction().apply {
            if (!now.isAdded) {
                add(R.id.container, now)
            }
            show(now)
            hide(fragments[old])
            commit()
        }
    }
}
