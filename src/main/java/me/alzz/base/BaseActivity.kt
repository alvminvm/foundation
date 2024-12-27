package me.alzz.base

import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import me.alzz.base.databinding.ToolbarTitleBinding

/**
 * Created by JeremyHe on 2018/3/25.
 */
open class BaseActivity : AppCompatActivity() {
    
    private lateinit var toolbarBinding: ToolbarTitleBinding

    override fun setContentView(view: View) {
        super.setContentView(view)
        toolbarBinding = ToolbarTitleBinding.bind(view)
        
        toolbarBinding.toolbar?.let {
            setSupportActionBar(toolbarBinding.toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(toolbarBinding.toolbar.title.isNotEmpty())
        }
    }

    override fun setTitle(title: CharSequence?) {
        if (toolbarBinding.titleTv == null) {
            supportActionBar?.setDisplayShowTitleEnabled(true)
            super.setTitle(title)
        } else {
            super.setTitle("")
            toolbarBinding.titleTv.text = title
        }
    }

    override fun setTitle(titleId: Int) {
        title = getString(titleId)
    }

    fun setTitleGravity(gravity: Int) {
        toolbarBinding.titleTv?.gravity = gravity
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}