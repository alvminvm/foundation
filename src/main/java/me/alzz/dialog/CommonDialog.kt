package me.alzz.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.fragment_common_dialog.*
import me.alzz.base.R
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_common_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 0)

        contentTv.click {
            onClickContent?.invoke(contentTv.text.toString())
        }

        confirmTv.click {
            onConfirm?.invoke(confirmTv.text.toString())
            dismiss()
        }

        cancelTv.click {
            onCancel?.invoke(cancelTv.text.toString())
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
        titleTv.isGone = title.isBlank()
        titleTv.text = title

        val content = args.getCharSequence(EXTRA_CONTENT) ?: ""
        contentTv.isGone = content.isBlank()
        contentTv.text = content

        confirmTv.text = args.getString(EXTRA_CONFIRM) ?: "确定"

        val cancel = args.getString(EXTRA_CANCEL) ?: ""
        cancelTv.isGone = cancel.isBlank()
        cancelTv.text = cancel
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