package me.alzz

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import me.alzz.base.BaseFragment

class FragmentPagerAdapter(fm: FragmentManager, val fragments: Array<Fragment>, val ids: Array<Long>? = null) : androidx.fragment.app.FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val f = super.instantiateItem(container, position)
        if (f is Fragment) { fragments[position] = f }
        return f
    }

    override fun getItem(position: Int) = fragments[position]
    override fun getCount() = fragments.size
    override fun getPageTitle(position: Int) = (fragments[position] as? BaseFragment)?.title
    override fun getItemId(position: Int): Long {
        return ids?.getOrNull(position) ?: super.getItemId(position)
    }
}