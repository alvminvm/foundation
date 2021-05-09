package me.alzz.widget

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL


/**
 * Created by JeremyHe on 2019/4/7.
 */
class DividerDecoration(val width: Int): RecyclerView.ItemDecoration() {

    var top = width
    var left = width
    var right = width
    var bottom = width

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        when (val layoutMgr = parent.layoutManager) {
            is StaggeredGridLayoutManager -> staggerDivider(outRect, view, layoutMgr, parent)
            is LinearLayoutManager -> linearDivider(outRect, view, layoutMgr, parent)
            else -> outRect.set(width, width, width, width)
        }
    }

    private fun linearDivider(outRect: Rect, view: View, lm: LinearLayoutManager, parent: RecyclerView) {
        val position = parent.getChildAdapterPosition(view)
        val isFirstOne = position == 0
        val isLastOne = position == ((parent.adapter?.itemCount ?: 0) - 1)

        if (lm.orientation == HORIZONTAL) {
            if (isFirstOne) {
                outRect.left = left
            } else {
                outRect.left = width / 2
            }

            if (isLastOne) {
                outRect.right = right
            } else {
                outRect.right = width / 2
            }
        } else {
            if (position == 0) {
                outRect.top = top
            } else {
                outRect.top = width / 2
            }

            if (isLastOne) {
                outRect.bottom = bottom
            } else {
                outRect.bottom = width / 2
            }
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