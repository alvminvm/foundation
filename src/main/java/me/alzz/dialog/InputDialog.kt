package me.alzz.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import me.alzz.base.R
import me.alzz.base.databinding.FragmentInputDialogBinding
import me.alzz.ext.click
import me.alzz.ext.isGone
import org.jetbrains.anko.toast
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Created by JeremyHe on 2019/5/4.
 */
open class InputDialog: DialogFragment() {

    var onConfirm: ((content: String) -> Unit)? = null
    var onCancel: (() -> Unit)? = null
    var onDismiss: (() -> Unit)? = null
    
    private lateinit var binding: FragmentInputDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentInputDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 0)

        binding.confirmTv.click {
            val content = binding.inputEt.text.toString().trim()
            if (content.isBlank()) {
                context?.toast(binding.inputEt.hint)
                return@click
            }

            onConfirm?.invoke(content)
            dismiss()
        }

        binding.cancelTv.click {
            onCancel?.invoke()
            dismiss()
        }

        binding.inputEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.confirmTv.performClick()
                true
            } else {
                false
            }
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

        binding.inputEt.hint = args.getString(EXTRA_HINT) ?: "请输入..."

        val content = args.getString(EXTRA_CONTENT) ?: ""
        binding.inputEt.setText(content)
        binding.inputEt.requestFocus()

        binding.confirmTv.text = args.getString(EXTRA_CONFIRM) ?: getString(R.string.action_confirm)

        showInputMethod()
    }

    private fun showInputMethod() {
        binding.inputEt.postDelayed({
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.inputEt, InputMethodManager.SHOW_IMPLICIT)
        }, 200)
    }

    fun setArgs(title: String, hint: String, content: String, confirm: String) {
        val args = arguments ?: Bundle()
        args.putString(EXTRA_TITLE, title)
        args.putString(EXTRA_HINT, hint)
        args.putString(EXTRA_CONTENT, content)
        args.putString(EXTRA_CONFIRM, confirm)
        arguments = args

        if (isAdded) {
            showInternal()
        }
    }

    suspend fun await() = suspendCoroutine {
        onConfirm = { input -> it.resume(input) }
        onCancel = { it.resume("") }
    }

    companion object {
        private const val TAG = "inputDialog"

        private const val EXTRA_TITLE = "title"
        private const val EXTRA_HINT = "hint"
        private const val EXTRA_CONTENT = "content"
        private const val EXTRA_CONFIRM = "confirm"

        /**
         * @return 取消时返回空字符串
         */
        suspend fun awaitAdd(name: String, fm: FragmentManager) = suspendCoroutine<String> {
            val dialog = showAdd(name, fm)
            dialog.onConfirm = { reason ->
                it.resume(reason)
            }

            dialog.onCancel = {
                it.resume("")
            }
        }

        fun showAdd(name: String, fm: FragmentManager): InputDialog {
            return show("新增$name", "请输入$name", "", "确定", fm)
        }

        fun show(title: String, hint: String, content: String, confirm: String, fm: FragmentManager): InputDialog {
            val dialog = fm.findFragmentByTag(TAG) as? InputDialog
                ?: InputDialog()
            dialog.setArgs(title, hint, content, confirm)
            if (dialog.isAdded) {
                fm.beginTransaction().show(dialog).commitAllowingStateLoss()
            } else {
                dialog.show(fm, TAG)
            }
            return dialog
        }
    }
}