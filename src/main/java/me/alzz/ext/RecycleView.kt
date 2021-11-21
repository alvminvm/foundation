package me.alzz.ext

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

fun RecyclerView.tryLoadMore(action: ()->Unit) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            if (dy > 0) {
                val lm = recyclerView.layoutManager
                when (lm) {
                    is StaggeredGridLayoutManager -> {
                        val position = lm.findLastVisibleItemPositions(null)[0]
                        if (position > lm.itemCount - (lm.spanCount * 2)) {
                            action()
                        }
                    }
                }
            }
        }
    })
}

/**
 * 数据更新完成后执行
 */
fun RecyclerView.runAfterUpdateFinished(action: Runnable, timeoutInMills: Long = 300) {
    if (timeoutInMills > 0 && this.hasPendingAdapterUpdates()) {
        Log.d("RecycleView", "runAfterUpdateFinished timeout = $timeoutInMills")
        postDelayed({ runAfterUpdateFinished(action, timeoutInMills - 16) }, 16)
        return
    }

    Log.d("RecycleView", "runAfterUpdateFinished")
    action.run()
}

