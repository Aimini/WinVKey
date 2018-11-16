package com.startai.winvkey.data_class

import java.util.*
import kotlin.reflect.KProperty

/**
 * @Author: AiMin
 * @Description:
 * @Date: Created in 17:10 2018/10/6
 * @Modified By:
 */
class Key(code:Int,name:String,isFavor:Boolean) : ObservableData() {
    var code: Int = code
        set(value) {
            this.propertyChanged(this, Key::code, field, value)
            field = value

        }


    var name: String = name
        set(value){
            this.propertyChanged(this, Key::name, field, value)
            field = value
        }

    var isFavor:Boolean = isFavor
        set(value) {
            this.propertyChanged(this, Key::isFavor, field, value)
            field = value
        }

    override fun toString(): String {
        return "Key(code=$code, name='$name', isFavor=$isFavor)"
    }

}


public open class KeyObserver(): DataObserver<Key>() {
    override fun propertyChanged(o: Observable, obj: Key, property: KProperty<*>, oldValue: Any, newValue: Any) {
        this.changed(obj)
    }
    //
    open fun changed(currentkey: Key){

    }
}
