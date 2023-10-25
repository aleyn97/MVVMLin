package com.pcl.mvvm.ui.me

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.pcl.mvvm.R
import com.pcl.mvvm.databinding.ItemUsedwebBinding
import com.pcl.mvvm.network.entity.UsedWeb

/**
 *   @auther : Aleyn
 *   time   : 2019/11/14
 */

class MeWebAdapter :
    BaseQuickAdapter<UsedWeb, BaseViewHolder>(R.layout.item_usedweb) {

    override fun convert(holder: BaseViewHolder, item: UsedWeb) {
        val dataBinding = DataBindingUtil.bind<ItemUsedwebBinding>(holder.itemView)
        dataBinding?.itemData = item
        dataBinding?.executePendingBindings()
    }

}