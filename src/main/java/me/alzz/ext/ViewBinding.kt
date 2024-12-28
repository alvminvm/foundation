package me.alzz.ext

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

val ViewBinding.context: Context get() = root.context

val ViewBinding.owner: LifecycleOwner get() = root.context as LifecycleOwner
