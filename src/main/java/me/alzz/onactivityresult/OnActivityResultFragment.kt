package me.alzz.onactivityresult

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * Created by JeremyHe on 2020-05-23.
 */
class OnActivityResultFragment: Fragment() {

    private var requestCodeSeed = 0

    lateinit var callback: OnActivityResultCallback

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = ActivityResult(requestCode, resultCode, data)
        callback.onActivityResult(result)
    }

    fun generateRequestCode() = ++requestCodeSeed

    companion object {
        private const val TAG = "OnActivityResultFragment"

        fun of(host: FragmentActivity, callback: OnActivityResultCallback): OnActivityResultFragment {
            val fm = host.supportFragmentManager
            val f = fm.findFragmentByTag(TAG) as? OnActivityResultFragment
                ?: OnActivityResultFragment().apply {
                    fm.beginTransaction().add(this, TAG).commitNowAllowingStateLoss()
                }

            f.callback = callback

            return f
        }
    }
}

data class ActivityResult(
    val requestCode: Int,
    val resultCode: Int,
    val data: Intent?
) {
    fun isOk() = resultCode == Activity.RESULT_OK
}

interface OnActivityResultCallback {
    fun onActivityResult(result: ActivityResult)
}