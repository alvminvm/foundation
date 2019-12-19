package me.alzz.ext

/**
 * Created by JeremyHe on 2019-07-27.
 */
fun Double.trimZero() = String.format("%.2f", this).trimZero()