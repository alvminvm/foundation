package android.arch.lifecycle

open class BusLiveData<T> : MediatorLiveData<T>() {

    private val wrappers = mutableMapOf<Observer<T>, BusWrapper>()

    override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
        super.observe(owner, DefaultWrapper(observer))
    }

    open fun observeSticky(owner: LifecycleOwner, observer: Observer<T>) {
        super.observe(owner, observer)
    }

//    override fun observeForever(observer: Observer<T>) {
//        val wrapper = DefaultWrapper(observer)
//        wrappers[observer] = wrapper
//        super.observeForever(wrapper)
//    }

    override fun removeObserver(observer: Observer<T>) {
        val wrapper = if (observer is BusWrapper) {
            observer
        } else {
            wrappers[observer] as? Observer<T> ?: observer
        }
        super.removeObserver(wrapper)
    }

    inner class DefaultWrapper(observer: Observer<T>): BusWrapper(observer) {
        override fun onChanged(t: T?) {
            if (lastVersion >= version) {
                return
            }

            lastVersion = version
            observer.onChanged(t)
        }

    }

    abstract inner class BusWrapper(val observer: Observer<T>): Observer<T> {
        var lastVersion = version
    }
}