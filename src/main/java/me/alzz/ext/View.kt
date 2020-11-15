package me.alzz.ext

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.disposables.Disposable
import java.sql.Struct
import java.util.concurrent.TimeUnit


/**
 * 对 view 的一些工具方法
 * Created by JeremyHe on 2018/4/13.
 */
fun View.useBtnEffect(darker: Boolean = true) {
    if (!this.isClickable) {
        Log.w("ViewExt", "${this} 必须为 clickable，否则点击效果异常")
    }

    this.setOnTouchListener { v, event ->
        val d = (v as? ImageView)?.drawable
            ?: v.background
            ?: return@setOnTouchListener false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (darker) {
                    d.setColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY)
                } else {
                    d.alpha = (0xff * 0.5).toInt()
                }
            }
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> {
                if (darker) {
                    d.clearColorFilter()
                } else {
                    d.alpha = 0xff
                }
            }
        }
        false
    }
}

fun View.click(action: (View) -> Unit): Disposable {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || foreground == null) {
        this.useBtnEffect(false)
    }
    return this.clicks().throttleFirst(700, TimeUnit.MILLISECONDS).subscribe { action.invoke(this) }
}

fun TextView.alert(title: String, msg: String, positive: String = text.toString(), action: ()->Unit) = this.click {
    AlertDialog.Builder(context)
        .setPositiveButton(positive) { dialog, _ -> action(); dialog.dismiss() }
        .setNegativeButton("取消") { dialog, _ -> dialog.dismiss() }
        .setTitle(title)
        .setMessage(msg)
        .show()
}

/**
 * 扩大 View 的点击区域
 * @param width 四周扩大的距离。
 */
fun View.expandTouchDelegate(width: Int) {
    val parentView = this.parent as View
    parentView.post {
        val rect = Rect()
        this.getHitRect(rect)
        // 4个方向增加矩形区域
        rect.top -= width
        rect.bottom += width
        rect.left -= width
        rect.right += width
        parentView.touchDelegate = TouchDelegate(rect, this)
    }
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

var View.smoothAlpha: Float
    set(value) {
        if (alpha != value) {
            animate().cancel()
            animate().alpha(value).start()
        }
    }
    get() = alpha


