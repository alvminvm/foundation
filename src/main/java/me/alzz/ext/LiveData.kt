package me.alzz.ext

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.reflect.KProperty

/**
 * Created by JeremyHe on 2020-06-15.
 */

val <T> LiveData<List<T>>.size: Int
    get() = this.value?.size ?: 0

fun <T> MutableLiveData<T>.safeSet(value: T) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        this.value = value
    } else {
        this.postValue(value)
    }
}

operator fun <T> MutableLiveData<List<T>>.plusAssign(newList: List<T>) {
    val list = (this.value ?: listOf()) + newList
    this.safeSet(list)
}

operator fun MutableLiveData<Int>.plusAssign(right: Int) {
    this.safeSet((this.value ?: 0) + right)
}

operator fun MutableLiveData<Int>.minusAssign(right: Int) {
    this.safeSet((this.value ?: 0) - right)
}

operator fun <T> List<T>.plus(data: LiveData<List<T>>): List<T> {
    return this + (data.value ?: listOf())
}

@Suppress("NOTHING_TO_INLINE")
inline operator fun <T> LiveData<T>.setValue(thisObj: Any?, property: KProperty<*>, value: T) {
    this as MutableLiveData
    if (Looper.getMainLooper() == Looper.myLooper()) {
        this.value = value
    } else {
        this.postValue(value)
    }
}

@Suppress("NOTHING_TO_INLINE")
inline operator fun <T> LiveData<T>.getValue(thisObj: Any?, property: KProperty<*>): T? = this.value