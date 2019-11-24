package me.alzz.ext

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.jakewharton.rxbinding3.view.clicks
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

fun View.click(action: (View) -> Unit): Disposable {
    return this.clicks().throttleFirst(700, TimeUnit.MILLISECONDS).subscribe { action.invoke(this) }
}

fun View.animHeightTo(target: Int, endAction: (()->Unit)?) {
    val view = this
    val anim = ValueAnimator.ofInt(view.height, target)
    anim.addUpdateListener {
        val h = it.animatedValue as Int
        view.layoutParams?.apply {
            height = h
            view.requestLayout()
        }
    }

    anim.addListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) { }
        override fun onAnimationCancel(animation: Animator?) { }
        override fun onAnimationStart(animation: Animator?) { }

        override fun onAnimationEnd(animation: Animator?) {
            endAction ?: return
            view.post(endAction)
        }
    })

    anim.start()
}

var View.isVisible: Boolean
    set(value) {
        this.visibility = if (value) View.VISIBLE else View.INVISIBLE
    }
    get() = this.visibility == View.VISIBLE

var View.isGone: Boolean
    set(value) {
        this.visibility = if (value) View.GONE else View.VISIBLE
    }
    get() = this.visibility == View.GONE

var Array<out View>.isVisible: Boolean
    set(value) {
        this.forEach { it.isVisible = value }
    }
    get() = this.all { it.isVisible }

var Array<out View>.isGone: Boolean
    set(value) {
        this.forEach { it.isGone = value }
    }
    get() = this.all { it.isGone }




