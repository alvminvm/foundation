package me.alzz.ext

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Process
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import me.alzz.base.BuildConfig
import org.jetbrains.anko.runOnUiThread
import org.jetbrains.anko.toast


/**
 * Context 下的工具方法
 * Created by JeremyHe on 2018/4/15.
 */

// 淘宝包名
const val PKG_TAOBAO = "com.taobao.taobao"
// 天猫包名
const val PKG_TMALL = "com.tmall.wireless"
// 拼多多包名
const val PKG_PDD = "com.xunmeng.pinduoduo"
// qq 包名
const val PKG_QQ = "com.tencent.mobileqq"

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

val Context.isMainProcess: Boolean
    get() = packageName == currentProcessName

val Context.currentProcessName: String get() {
    val pid = Process.myPid()
    val activityMgr = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (process in activityMgr.runningAppProcesses) {
        if (process.pid == pid) {
            return process.processName
        }
    }

    return ""
}

/**
 * 输出调试信息
 */
fun Context.toastDebug(message: CharSequence) {
    if (BuildConfig.DEBUG) {
        runOnUiThread { toast(message) }
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

fun Context.goHome(): Boolean {
    return try {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * 屏幕宽度
 */
val Context.displayWidthPixels: Int
    get() = resources.displayMetrics.widthPixels

/**
 * 屏幕真实高度，包含底部导航栏
 */
val Context.realHeightPixels: Int
    get() {
        val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.defaultDisplay.getRealMetrics(metrics)
        } else {
            resources.displayMetrics
        }

        return metrics.heightPixels
    }

/**
 * 屏幕可用高度，有可能不包含导航栏
 */
val Context.usableHeightPixels: Int
    get() = resources.displayMetrics.heightPixels

/**
 * 当前应用的版本号
 */
val Context.versionName: String
    get() {
        val packageInfo: PackageInfo
        return try {
            packageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo.versionName ?: ""
        } catch (e: PackageManager.NameNotFoundException) {
            ""
        }
    }

/**
 * 当前应用的版本号
 */
val Context.versionCode: Int
    get() {
        val packageInfo: PackageInfo
        return try {
            packageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            0
        }
    }