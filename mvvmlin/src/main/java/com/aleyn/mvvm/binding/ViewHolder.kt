package com.aleyn.mvvm.binding

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @author : Aleyn
 * @date : 2025/4/23 20:02
 */

/**
 * Adapter 获取 ViewBinding
 */
@Suppress("UNCHECKED_CAST")
inline fun <T : ViewBinding> RecyclerView.ViewHolder.binding(
    crossinline vbCreate: (View) -> T,
): T {
    return (itemView.getTag(Int.MIN_VALUE) ?: vbCreate(itemView).also {
        itemView.setTag(Int.MIN_VALUE, it)
    }) as T
}