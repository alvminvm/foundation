package me.alzz.widget

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View


/**
 * Created by JeremyHe on 2019/4/7.
 */
class DividerDecoration(val width: Int): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val layoutMgr = parent.layoutManager
        when (layoutMgr) {
            is StaggeredGridLayoutManager -> staggerDivider(outRect, view, layoutMgr, parent)
        }
    }

    private fun staggerDivider(outRect: Rect, view: View, layoutManager: StaggeredGridLayoutManager, parent: RecyclerView) {
        val lp = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        val spanCount = layoutManager.spanCount
        val position = parent.getChildAdapterPosition(view)
        val halfWidth = width / 2
        outRect.left = halfWidth
        outRect.right = halfWidth
        outRect.top = if (position < spanCount) 0 else width

        when (lp.spanIndex) {
            0 -> outRect.left = width
            (spanCount - 1) -> outRect.right = width
        }
    }
}