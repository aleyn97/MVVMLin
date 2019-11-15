package com.pcl.mvvm.ui.me

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.pcl.mvvm.R
import com.pcl.mvvm.app.base.BaseDBViewHoder
import com.pcl.mvvm.databinding.ItemUsedwebBinding
import com.pcl.mvvm.network.entity.UsedWeb

/**
 *   @auther : Aleyn
 *   time   : 2019/11/14
 */

class MeWebAdapter :
    BaseQuickAdapter<UsedWeb, BaseDBViewHoder<ItemUsedwebBinding>>(R.layout.item_usedweb) {


    override fun convert(helper: BaseDBViewHoder<ItemUsedwebBinding>, item: UsedWeb?) {
        helper.getBinding().itemData = item
        helper.getBinding().executePendingBindings()
    }

    override fun getItemView(layoutResId: Int, parent: ViewGroup?): View {
        val binding = DataBindingUtil
            .inflate<ItemUsedwebBinding>(mLayoutInflater, layoutResId, parent, false)
            ?: return super.getItemView(layoutResId, parent)
        return binding.root.apply {
            setTag(R.id.BaseQuickAdapter_databinding_support, binding)
        }
    }

}