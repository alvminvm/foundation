package me.alzz.okhttp

/**
 * 用于协助加载更多
 * Created by JeremyHe on 2020/12/20.
 */
class PageReq {
    var curPage = -1
        private set

    var nextPage = 0
        private set

    /**
     * 判断是否可加载下一页
     */
    fun tryNextPage(): Boolean {
        if (nextPage > curPage) return false
        nextPage = curPage + 1
        return true
    }

    fun notifyLoadFail() {
        nextPage = curPage
    }

    fun <T> notifyLoadData(list: List<T>?) {
        if (list.isNullOrEmpty()) {
            notifyNoMore()
        } else {
            notifyLoadSuccess()
        }
    }

    fun notifyLoadSuccess() {
        curPage++
    }

    fun notifyNoMore() {
        nextPage = Int.MAX_VALUE
    }
}