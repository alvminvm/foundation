package me.alzz.ext

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import kotlin.math.min

/**
 * Created by JeremyHe on 2019-06-22.
 */

fun Bitmap.shrink(maxWidth: Int, maxHeight: Int, minWidth: Int = 0, minHeight: Int = 0): Bitmap {
    if (this.width < maxWidth && this.height < maxHeight) {
        return this
    }

    if (this.width < minWidth || this.height < minHeight) {
        return this
    }

    var scaleH = this.height / maxHeight.toFloat()
    if (this.width / scaleH < minWidth) {
        scaleH = this.width / minWidth.toFloat()
    }

    var scaleW = this.width / maxWidth.toFloat()
    if (this.height / scaleW < minHeight) {
        scaleW = this.height / minHeight.toFloat()
    }

    val scale = min(scaleH, scaleW)
    val h = (this.height / scale).toInt()
    val w = (this.width / scale).toInt()
    val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bm)
    canvas.drawBitmap(
        this,
        Rect(0, 0, this.width, this.height),
        Rect(0, 0, w, h),
        null)
    return bm
}