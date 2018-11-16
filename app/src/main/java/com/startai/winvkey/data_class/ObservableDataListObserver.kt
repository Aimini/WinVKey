package com.startai.winvkey.data_class

import kotlin.reflect.KProperty

import java.lang.reflect.Field
import java.util.Observable

/**
 * @Author: AiMin
 * @Description:
 * @Date: Created in 16:35 2018/11/16
 * @Modified By:
 */
abstract class ObservableDataListObserver<E : ObservableData> : ObservableListObserver<E>() {
    override fun update(o: Observable, arg: Any) {
        if (arg is ObservableData.ChangeInfo<*>) {
            try {
                this.itemChanged(arg.obj as E, arg.property, arg.oldVaule, arg.newVaule)
            } catch (e: ClassCastException) {
                println("not a suitable arg for DataObservable: " + arg.javaClass.name)
            }

        } else {
            super.update(o, arg)
        }
    }

    open fun itemChanged(obj: E, property: KProperty<*>, oldValue: Any, newValue: Any) {}
}
