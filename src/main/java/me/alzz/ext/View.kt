package me.alzz.ext

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * 对 view 的一些工具方法
 * Created by JeremyHe on 2018/4/13.
 */
fun View.useBtnEffect() {
    if (!this.isClickable) {
        Log.w("ViewExt", "${this} 必须为 clickable，否则点击效果异常")
    }

    this.setOnTouchListener { v, event ->
        var d: Drawable? = null
        if (v is ImageView) {
            d = v.drawable
        }

        if (d == null) {
            d = v.background
        }

        d ?: return@setOnTouchListener false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                d.setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY)
            }
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> {
                d.clearColorFilter()
            }
        }
        false
    }
}

fun View.throttleClick(action: () -> Unit): Disposable {
    return RxView.clicks(this).throttleFirst(1, TimeUnit.SECONDS).subscribe { action.invoke() }
}
