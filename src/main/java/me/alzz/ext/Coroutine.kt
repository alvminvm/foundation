package me.alzz.ext

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Created by JeremyHe on 2020-05-31.
 */
suspend fun <T> withIo(block: () -> T) = withContext(Dispatchers.IO) { block() }
