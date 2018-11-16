package com.startai.winvkey.data_class

import java.lang.ClassCastException
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
        notifyObservers(ChangeInfo<Any>(obj, property, oldValue, newValue))
        return true
    }


    inner class ChangeInfo<T>(internal var obj: T, internal var property: KProperty<*>, internal var oldVaule: Any, internal var newVaule: Any)
}

 open class DataObserver<T> : java.util.Observer {
    override fun update(o: Observable, arg: Any) {
        if (arg is ObservableData.ChangeInfo<*>) {
            try{
                val castedObject = (arg.obj) as T
                propertyChanged(o, castedObject, arg.property, arg.oldVaule, arg.newVaule)
            }catch (e:ClassCastException ){
                println("not a suitable arg for DataObservable: " + arg.javaClass.name)
            }
        }
    }

    open fun propertyChanged(o: Observable, obj: T, property: KProperty<*>, oldValue: Any, newValue: Any) {
        // in normal obj and o is the same object
    }
}

