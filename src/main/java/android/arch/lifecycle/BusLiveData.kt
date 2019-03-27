package android.arch.lifecycle

open class BusLiveData<T> : MutableLiveData<T>() {

    override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
        super.observe(owner, DefaultWrapper(observer))
    }

    open fun observeSticky(owner: LifecycleOwner, observer: Observer<T>) {
        super.observe(owner, observer)
    }

    inner class DefaultWrapper(observer: Observer<T>): BusWrapper(observer), Observer<T> {
        override fun onChanged(t: T?) {
            if (lastVersion >= version) {
                return
            }

            observer.onChanged(t)
        }

    }

    abstract inner class BusWrapper(val observer: Observer<T>) {
        var lastVersion = LiveData.START_VERSION
    }
}