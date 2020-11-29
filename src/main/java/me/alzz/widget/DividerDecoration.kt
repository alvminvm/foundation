package me.alzz.widget

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.View


/**
 * Created by JeremyHe on 2019/4/7.
 */
class DividerDecoration(val width: Int): RecyclerView.ItemDecoration() {

    var top = 0
    var left = width
    var right = width

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        when (val layoutMgr = parent.layoutManager) {
            is StaggeredGridLayoutManager -> staggerDivider(outRect, view, layoutMgr, parent)
            else -> outRect.set(width, width, width, width)
        }
    }

    private fun staggerDivider(outRect: Rect, view: View, layoutManager: StaggeredGridLayoutManager, parent: RecyclerView) {
        val lp = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        val spanCount = layoutManager.spanCount
        val position = parent.getChildAdapterPosition(view)

        val halfWidth = width / 2
        outRect.left = halfWidth
        outRect.right = halfWidth
        outRect.bottom = width

        val isTopItem = position < spanCount
        outRect.top = if (isTopItem) top else outRect.top

        val isLeftItem = lp.spanIndex == 0
        if (isLeftItem) outRect.left = left

        val isRightItem = lp.spanIndex == (spanCount - 1)
        if (isRightItem) outRect.right = right
    }
}