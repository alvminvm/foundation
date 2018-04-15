package me.alzz.base

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

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