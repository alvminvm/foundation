package me.alzz.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import me.alzz.base.R
import me.alzz.base.databinding.FragmentListDialogBinding
import me.alzz.ext.click
import me.alzz.ext.isGone
import me.alzz.ext.isVisible
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Created by JeremyHe on 2020/10/24.
 */
open class ListDialog: DialogFragment() {

    var onItemClick: ((position: Int, item: String) -> Unit)? = null
    var onAdd: (() -> Unit)? = null
        set(value) {
            field = value
            binding.addTv.isVisible = value != null
        }
    var onCancel: (() -> Unit)? = null
    var onDismiss: (() -> Unit)? = null

    private lateinit var binding: FragmentListDialogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentListDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setStyle(STYLE_NO_TITLE, 0)

        binding.cancelTv.click {
            onCancel?.invoke()
            dismiss()
        }

        binding.addTv.isVisible = onAdd != null
        binding.addTv.click {
            onAdd?.invoke()
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

        val items = args.getStringArray(EXTRA_ITEMS) ?: arrayOf()
        binding.listRv.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val view = inflater.inflate(R.layout.item_list_dialog, parent, false)
                return object : RecyclerView.ViewHolder(view) {}
            }

            override fun getItemCount() = items.size

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val tv = holder.itemView as TextView
                tv.text = items[position]

                tv.setOnClickListener {
                    onItemClick?.invoke(position, items[position])
                    dismiss()
                }
            }
        }
    }

    fun setArgs(title: String, items: List<String>) {
        val args = arguments ?: Bundle()
        args.putString(EXTRA_TITLE, title)
        args.putStringArray(EXTRA_ITEMS, items.toTypedArray())
        arguments = args

        if (isAdded) {
            showInternal()
        }
    }

    companion object {
        private const val TAG = "listDialog"

        private const val EXTRA_TITLE = "title"
        private const val EXTRA_ITEMS = "items"

        fun show(title: String, items: List<String>, fm: FragmentManager): ListDialog {
            val dialog = fm.findFragmentByTag(TAG) as? ListDialog
                ?: ListDialog()
            dialog.setArgs(title, items)
            if (dialog.isAdded) {
                fm.beginTransaction().show(dialog).commitAllowingStateLoss()
            } else {
                dialog.show(fm, TAG)
            }
//            dialog.isCancelable = false
            return dialog
        }

        suspend fun awaitChoose(title: String, items: List<String>, fm: FragmentManager, enableAdd: Boolean = false) = suspendCoroutine<Pair<Int, String>?> {
            val dialog = show(title, items, fm)
            dialog.onItemClick = { position, item ->
                it.resume(position to item)
            }

            if (enableAdd) {
                dialog.onAdd = { it.resume(-1 to "新增") }
            }

            dialog.onCancel = {
                it.resume(null)
            }
        }

    }
}