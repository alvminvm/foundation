package me.alzz

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.alzz.base.R
import me.alzz.base.databinding.ProgressFragmentBinding

/**
 * Created by JeremyHe on 2019/4/17.
 */
class ProgressFragment: DialogFragment() {

    private lateinit var binding: ProgressFragmentBinding

    init {
        setStyle(STYLE_NO_TITLE, 0)
        isCancelable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ProgressFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val msg = arguments?.getString(EXTRA_MESSAGE) ?: getString(R.string.loading)
        binding.messageTv.text = msg
    }

    fun setMessage(message: String) {
        val args = arguments ?: Bundle().apply { arguments = this }
        args.putString(EXTRA_MESSAGE, message)
        if (isAdded) {
            binding.messageTv.text = message
        }
    }

    companion object {
        private const val EXTRA_MESSAGE = "extra.message"
    }
}