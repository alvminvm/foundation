package me.alzz.ext

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Created by JeremyHe on 2020-06-15.
 */

val <T> LiveData<List<T>>.size: Int
    get() = this.value?.size ?: 0

operator fun <T> MutableLiveData<List<T>>.plusAssign(newList: List<T>) {
    value = (this.value ?: listOf()) + newList
}

operator fun <T> List<T>.plus(data: LiveData<List<T>>): List<T> {
    return this + (data.value ?: listOf())
}

