package me.alzz.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import me.alzz.base.R
import me.alzz.base.databinding.FragmentCommonDialogBinding
import me.alzz.ext.click
import me.alzz.ext.isGone

/**
 * Created by JeremyHe on 2019/5/4.
 */
open class CommonDialog: DialogFragment() {

    var onClickContent: ((content: String) -> Unit)? = null
    var onConfirm: ((action: String) -> Unit)? = null
    var onCancel: ((action: String) -> Unit)? = null
    var onDismiss: (() -> Unit)? = null
    
    private lateinit var binding: FragmentCommonDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCommonDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 0)

        binding.contentTv.click {
            onClickContent?.invoke(binding.contentTv.text.toString())
        }

        binding.confirmTv.click {
            onConfirm?.invoke(binding.confirmTv.text.toString())
            dismiss()
        }

        binding.cancelTv.click {
            onCancel?.invoke(binding.cancelTv.text.toString())
            dismiss()
        }

        showInternal()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss?.invoke()
    }

    private fun showInternal() {
        val args = arguments ?: return

        val title = args.getString(EXTRA_TITLE) ?: ""
        binding.titleTv.isGone = title.isBlank()
        binding.titleTv.text = title

        val content = args.getCharSequence(EXTRA_CONTENT) ?: ""
        binding.contentTv.isGone = content.isBlank()
        binding.contentTv.text = content

        binding.confirmTv.text = args.getString(EXTRA_CONFIRM) ?: "确定"

        val cancel = args.getString(EXTRA_CANCEL) ?: ""
        binding.cancelTv.isGone = cancel.isBlank()
        binding.cancelTv.text = cancel
    }

    fun setArgs(title: String, content: CharSequence, confirm: String, cancel: String) {
        val args = arguments ?: Bundle()
        args.putString(EXTRA_TITLE, title)
        args.putCharSequence(EXTRA_CONTENT, content)
        args.putString(EXTRA_CONFIRM, confirm)
        args.putString(EXTRA_CANCEL, cancel)
        arguments = args

        if (isAdded) {
            showInternal()
        }
    }

    companion object {
        private const val TAG = "commonDialog"

        private const val EXTRA_TITLE = "title"
        private const val EXTRA_CONTENT = "content"
        private const val EXTRA_CONFIRM = "confirm"
        private const val EXTRA_CANCEL = "cancel"

        fun show(title: String, content: CharSequence, confirm: String, cancel: String, fm: FragmentManager): CommonDialog {
            val dialog = fm.findFragmentByTag(TAG) as? CommonDialog
                ?: CommonDialog()
            dialog.setArgs(title, content, confirm, cancel)
            if (dialog.isAdded) {
                fm.beginTransaction().show(dialog).commitAllowingStateLoss()
            } else {
                dialog.show(fm, TAG)
            }
            return dialog
        }
    }
}