package me.alzz.bus

import android.arch.lifecycle.*
import com.jeremyliao.liveeventbus.LiveEventBus
import com.jeremyliao.liveeventbus.LiveEventBus.Observable
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class Event<T>: ReadOnlyProperty<Any?, Event<T>> {
    private lateinit var liveData: MutableLiveData<T>
    private var name = ""

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any?, property: KProperty<*>): Event<T> {
        name = property.name
        liveData = LiveEventBus.get().with(name) as MutableLiveData<T>
        return this
    }

    fun send(data: T) {
        liveData.value = data
    }

    fun observe(owner: LifecycleOwner, action: (data: T) -> Unit) {
        liveData.observe(owner, Observer {
            it ?: return@Observer
            action.invoke(it)
        })
    }

    fun sticky(owner: LifecycleOwner, action: (data: T) -> Unit) {
        if (liveData !is Observable<*>) {
            throw Exception("无法对聚合消息使用此方法")
        }

        @Suppress("UNCHECKED_CAST")
        val o = liveData as Observable<T>
        o.observeSticky(owner, Observer {
            it ?: return@Observer
            action.invoke(it)
        })
    }

    @Suppress("UNCHECKED_CAST")
    operator fun plus(e: Event<*>): Event<String> {
        val event = if (liveData is MediatorLiveData) {
            this as Event<String>
        } else {
            val event = Event<String>()
            event.liveData = MediatorLiveData<String>()
            event
        }

        val bus = event.liveData as MediatorLiveData<String>
        if (this != event) {
            bus.addSource(this.liveData as LiveData<*>) { bus.value = this.name }
        }
        bus.addSource(e.liveData as LiveData<*>) { bus.value = e.name }

        return event
    }
}