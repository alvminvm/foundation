package me.alzz.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.toolbar_title.*

/**
 * TODO: Add comments
 * Created by JeremyHe on 2018/3/31.
 */
open class BaseFragment: Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        toolbar?.let {
            (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
            (activity as? AppCompatActivity)?.supportActionBar?.setDisplayShowTitleEnabled(false)
        }
    }

    fun setTitle(title: String) {
        titleTv?.text = title
    }
}