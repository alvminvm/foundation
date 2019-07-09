package me.alzz.widget

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet

/**
 * 用于实现跑马灯效果
 * Created by JeremyHe on 2018/5/3.
 */

class MarqueeTextView : AppCompatTextView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun isFocused(): Boolean {
        return true
    }
}