package me.alzz.okhttp

import android.util.Log
import okhttp3.RequestBody
import okio.*
import java.io.IOException

/**
 * Created by JeremyHe on 6/20/21.
 */
class ProgressRequestBody(
    private val delegate: RequestBody,
    private val cb: (total: Long, current: Long) -> Unit
) : RequestBody() {

    @Throws(IOException::class)
    override fun contentLength() = delegate.contentLength()

    override fun contentType() = delegate.contentType()

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        Log.d(TAG, "call $this.write to ${sink.javaClass} - $sink")
        if (sink is Buffer) {
            delegate.writeTo(sink)
            return
        }

        @Suppress("NAME_SHADOWING")
        val sink = wrapSink(sink)
        delegate.writeTo(sink)
        sink.flush()
    }

    override fun isDuplex() = delegate.isDuplex()
    override fun isOneShot() = delegate.isOneShot()

    private fun wrapSink(delegate: Sink): BufferedSink {
        return object : ForwardingSink(delegate) {

            private var total = 0L
            private var current = 0L

            @Throws(IOException::class)
            override fun write(source: Buffer, byteCount: Long) {
                if (total == 0L) total = contentLength()
                current += byteCount
                super.write(source, byteCount)
                if (current <= total) {
                    cb.invoke(total, current)
                }
            }
        }.buffer()
    }

    companion object {
        private const val TAG = "ProgressRequestBody"
    }
}