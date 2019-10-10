package me.alzz

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
import me.alzz.base.BaseFragment

class FragmentPagerAdapter(fm: FragmentManager, val fragments: Array<Fragment>) : FragmentPagerAdapter(fm) {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val f = super.instantiateItem(container, position)
        if (f is Fragment) { fragments[position] = f }
        return f
    }

    override fun getItem(position: Int) = fragments[position]
    override fun getCount() = fragments.size
    override fun getPageTitle(position: Int) = (fragments[position] as? BaseFragment)?.title
}