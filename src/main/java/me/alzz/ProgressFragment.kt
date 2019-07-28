package me.alzz

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.progress_fragment.*
import me.alzz.base.R

/**
 * Created by JeremyHe on 2019/4/17.
 */
class ProgressFragment: DialogFragment() {

    init {
        setStyle(STYLE_NO_TITLE, 0)
        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.progress_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val msg = arguments?.getString(EXTRA_MESSAGE) ?: getString(R.string.loading)
        messageTv.text = msg
    }

    fun setMessage(message: String) {
        val args = arguments ?: Bundle().apply { arguments = this }
        args.putString(EXTRA_MESSAGE, message)
        if (isAdded) {
            messageTv.text = message
        }
    }

    companion object {
        private const val EXTRA_MESSAGE = "extra.message"
    }
}