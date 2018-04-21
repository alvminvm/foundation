package me.alzz.base

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast

/**
 * Context 下的工具方法
 * Created by JeremyHe on 2018/4/15.
 */

/**
 * 淘宝包名
 */
const val PKG_TAOBAO = "com.taobao.taobao"

/**
 * 天猫包名
 */
const val PKG_TMALL = "com.tmall.wireless"

/**
 * 是否安装了某应用
 */
fun Context.isPkgInstalled(pkg: String): Boolean {
    val packageInfo: PackageInfo? = try {
        this.packageManager.getPackageInfo(pkg, 0)
    } catch (e: PackageManager.NameNotFoundException) {
        null
    } catch (e: Exception) {
        null
    }

    return packageInfo != null
}

/**
 * 输出调试信息
 */
fun Context.toastDebug(message: CharSequence) {
    if (BuildConfig.DEBUG) {
        Toast
                .makeText(this, message, Toast.LENGTH_SHORT)
                .show()
    }
}

/**
 * Log.d
 */
fun Context.logd(msg: String) {
    Log.d("foundation", msg)
}

/**
 * debug 时输出
 */
fun Context.debug(msg: String) {
    if (BuildConfig.DEBUG) {
        logd(msg)
    }
}