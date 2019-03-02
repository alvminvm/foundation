package me.alzz.base

import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

/**
 * Created by JeremyHe on 2018/4/15.
 */
/**
 * 生成 fragment 的 tag
 */
fun AppCompatActivity.tag(clazz: Class<*>) = this.javaClass.canonicalName + "." + clazz.canonicalName

/**
 * 获取 fragment
 */
fun AppCompatActivity.fragment(clazz: Class<*>, tag: String = ""): Fragment {
    var fragmentTag = tag
    if (fragmentTag.isEmpty()) {
        fragmentTag = tag(clazz)
    }

    var fragment = supportFragmentManager.findFragmentByTag(fragmentTag)
    if (fragment == null) {
        fragment = clazz.newInstance() as Fragment
    }

    return fragment
}

/**
 * 展示 fragment
 */
fun AppCompatActivity.showFragment(@IdRes containerId: Int, clazz: Class<*>, fragments: MutableSet<Fragment>) {
    if (this.isFinishing) {
        return
    }

    val t = supportFragmentManager.beginTransaction()

    val fragment = fragment(clazz)
    for (f in fragments) {
        if (f != fragment) {
            t.hide(f)
        }
    }

    if (fragment.isAdded) {
        t.show(fragment)
    } else {
        t.add(containerId, fragment, tag(clazz))
    }

    t.commit()

    fragments.add(fragment)
}