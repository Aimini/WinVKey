package com.startai.winvkey.data_class

import kotlin.reflect.KProperty

import java.util.Observable

/**
 * @Author: AiMin
 * @Description:
 * @Date: Created in 16:26 2018/11/14
 * @Modified By:
 */
abstract class ObservableData : PausableObservable() {



    fun propertyChanged(obj: Any, property: KProperty<*>, oldValue: Any, newValue: Any): Boolean {
        setChanged()
        notifyObservers(ChangeInfo(obj, property, oldValue, newValue))
        return true
    }


    inner class ChangeInfo(internal var obj: Any, internal var property: KProperty<*>, internal var oldVaule: Any, internal var newVaule: Any)
}

 open class DataObserver : java.util.Observer {
    override fun update(o: Observable, arg: Any) {
        if (arg is ObservableData.ChangeInfo) {
            propertyChanged(o, arg.obj, arg.property, arg.oldVaule, arg.newVaule)
        }
    }

    open fun propertyChanged(o: Observable, obj: Any, property: KProperty<*>, oldValue: Any, newValue: Any) {
        // in normal obj and o is the same object
    }
}

