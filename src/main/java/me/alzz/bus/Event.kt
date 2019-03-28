package me.alzz.bus

import android.arch.lifecycle.*
import android.os.Looper
import java.lang.Exception
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class Event<T>: ReadOnlyProperty<Any?, Event<T>> {
    private lateinit var liveData: BusLiveData<T>
    private var name = ""

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any?, property: KProperty<*>): Event<T> {
        name = property.name
        liveData = LiveBus.get().with(name) as BusLiveData<T>
        return this
    }

    fun send(data: T) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            liveData.postValue(data)
        } else {
            liveData.value = data
        }
    }

    fun observe(owner: LifecycleOwner, action: (data: T) -> Unit) {
        liveData.observe(owner, Observer {
            it ?: return@Observer
            action.invoke(it)
        })
    }

    fun sticky(owner: LifecycleOwner, action: (data: T) -> Unit) {
        @Suppress("UNCHECKED_CAST")
        liveData.observeSticky(owner, Observer {
            it ?: return@Observer
            action.invoke(it)
        })
    }

    @Suppress("UNCHECKED_CAST")
    operator fun plus(e: Event<*>): Event<String> {
        val event = if (this.name.isEmpty()) {
            this as Event<String>
        } else {
            val event = Event<String>()
            event.liveData = BusLiveData()
            event
        }

        val bus = event.liveData
        if (this != event) {
            bus.addSource(this.liveData as LiveData<*>) { bus.postValue(this.name) }
        }
        bus.addSource(e.liveData as LiveData<*>) { bus.postValue(e.name) }

        return event
    }
}