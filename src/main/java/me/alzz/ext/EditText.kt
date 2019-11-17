package me.alzz.ext

import android.widget.EditText

/**
 * Created by JeremyHe on 2019-11-16.
 */

fun EditText.toEnd() {
    this.setSelection(this.length())
}
