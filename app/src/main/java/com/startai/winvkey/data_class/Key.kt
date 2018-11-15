package com.win_vkey.startai.winvkey.data_class

import com.startai.winvkey.data_class.DataObserver
import com.startai.winvkey.data_class.ObservableData
import org.apache.tools.ant.taskdefs.Copy
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
            this.propertyChanged(this,Key::code, field, value)
            field = value

        }


    var name: String = name
        set(value){
            this.propertyChanged(this,Key::name, field, value)
            field = value
        }

    var isFavor:Boolean = isFavor
        set(value) {
            this.propertyChanged(this, Key::isFavor, field, value)
            field = value
        }

}


public open class KeyObserver(): DataObserver() {
    override fun propertyChanged(o: Observable, obj: Any, property: KProperty<*>, oldValue: Any, newValue: Any) {
        super.propertyChanged(o, obj, property, oldValue, newValue)
        if(obj is Key)
        {
            this.changed(obj)
        }

    }
    //
    open fun changed(currentkey:Key){

    }
}
