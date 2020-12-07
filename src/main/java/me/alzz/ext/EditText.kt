package me.alzz.ext

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView

/**
 * Created by JeremyHe on 2019-11-16.
 */

/**
 * 移动光标至末尾
 */
fun EditText.toEnd() {
    this.setSelection(this.length())
}

/**
 * 总是保持英文符号
 * todo: 未完善。
 */
fun EditText.alwaysEnSymbol(): EditText {
    return this.alwaysReplace("，", ",")
}

/**
 * 总是替换 [old] 为 [new]
 */
fun EditText.alwaysReplace(old: String, new: String): EditText {
    addTextChangedListener(object : TextWatcher {
        private var postSelection = Int.MIN_VALUE
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (postSelection != Int.MIN_VALUE) {
                setSelection(postSelection)
                postSelection = Int.MIN_VALUE
            }

            val str = s?.toString() ?: return
            val text = str.replace(old, new)
            if (text == str) return

            postSelection = maxOf(0, minOf(selectionStart + (text.length - str.length), text.length))
            setText(text, TextView.BufferType.EDITABLE)
        }

        override fun afterTextChanged(s: Editable?) {}
    })

    return this
}
