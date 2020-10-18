package me.alzz.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by JeremyHe on 2020/10/18.
 */
open class BaseViewHolder(parent: ViewGroup, @LayoutRes layoutId: Int) :
    RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)) {
}