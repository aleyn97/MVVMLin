package com.pcl.mvvm.ui.home

import android.widget.ImageView
import coil.load
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.pcl.mvvm.R
import com.pcl.mvvm.network.entity.ArticlesBean

/**
 *   @auther : Aleyn
 *   time   : 2019/11/08
 */
class HomeListAdapter : BaseQuickAdapter<ArticlesBean, BaseViewHolder>(R.layout.item_article_list),
    LoadMoreModule {


    override fun convert(holder: BaseViewHolder, item: ArticlesBean) {
        with(holder) {
            setText(R.id.tv_project_list_atticle_type, item.chapterName)
            setText(R.id.tv_project_list_atticle_title, item.title)
            setText(R.id.tv_project_list_atticle_time, item.niceDate)
            setText(R.id.tv_project_list_atticle_auther, item.author)
            val imageView = holder.getView<ImageView>(R.id.iv_project_list_atticle_ic)
            if (!item.envelopePic.isNullOrEmpty()) {
                imageView.load(item.envelopePic)
            }
        }
    }

}