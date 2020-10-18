package me.alzz.ext

import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.ImageView

/**
 * ImageViewUtils
 * Created by JeremyHe on 2020/10/18.
 */

/**
 * 通过 colorFilter 使得 ImageView 变暗
 */
fun ImageView.darker() {
    this.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
}

/**
 * 通过 colorFilter 使得 ImageView 变暗
 */
fun ImageView.darker2() {
    this.setColorFilter(Color.DKGRAY, PorterDuff.Mode.MULTIPLY)
}