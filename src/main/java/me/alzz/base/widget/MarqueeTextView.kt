package me.alzz.base.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

/**
 * 用于实现跑马灯效果
 * Created by JeremyHe on 2018/5/3.
 */

class MarqueeTextView : TextView {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun isFocused(): Boolean {
        return true
    }
}