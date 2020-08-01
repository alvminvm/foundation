package me.alzz.ext

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Process
import android.util.Log
import android.widget.Toast
import me.alzz.base.BuildConfig


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