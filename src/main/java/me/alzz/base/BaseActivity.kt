package me.alzz.base

import android.content.Intent
import android.view.MenuItem
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.toolbar_title.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import me.alzz.base.coroutine.ActivityResult

/**
 * Created by JeremyHe on 2018/3/25.
 */
open class BaseActivity : AppCompatActivity() {

    private val channels = mutableMapOf<Int, Channel<ActivityResult>>()

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)

        toolbar?.let {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(toolbar.title.isNotEmpty())
        }
    }

    override fun setTitle(title: CharSequence?) {
        if (titleTv == null) {
            supportActionBar?.setDisplayShowTitleEnabled(true)
            super.setTitle(title)
        } else {
            super.setTitle("")
            titleTv.text = title
        }
    }

    override fun setTitle(titleId: Int) {
        title = getString(titleId)
    }

    fun setTitleGravity(gravity: Int) {
        titleTv?.gravity = gravity
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

    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val channel = channels[requestCode]
        if (channel != null) {
            lifecycleScope.launch { channel.send(ActivityResult(requestCode, resultCode, data)) }
            return
        }
    }

    suspend fun awaitForResult(intent: Intent, requestCode: Int): ActivityResult {
        val channel = channels[requestCode] ?: Channel()
        channels[requestCode] = channel

        startActivityForResult(intent, requestCode)
        return channel.receive()
    }

    @CallSuper
    override fun onDestroy() {
        channels.values.forEach { it.close() }
        super.onDestroy()
    }

}