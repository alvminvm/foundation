package me.alzz.ext

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

