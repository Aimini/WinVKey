package com.startai.winvkey.data_class

import com.win_vkey.startai.winvkey.data_class.Key
import com.win_vkey.startai.winvkey.data_class.KeyObserver
import org.junit.Test

import java.util.*
import kotlin.reflect.KProperty

/**
 * @Author: AiMin
 * @Description:
 * @Date: Created in 19:51 2018/11/15
 * @Modified By:
 */
class KeyTest {
    var key:Key = Key(120,"123",false)
    var ob = object: KeyObserver() {
        override fun propertyChanged(o: Observable, obj: Any, property: KProperty<*>, oldValue: Any, newValue: Any) {
            println("property ${property.name}:${property.returnType.toString()}:${oldValue}->${newValue}")
            super.propertyChanged(o, obj, property, oldValue, newValue)
        }
    }


    @Test
    fun setCode() {
        key.addObserver(ob)
        key.code = 1000
        key.pause {
            key.code = 20
        }
        println(key.code)
        key.code = 300
    }

    @Test
    fun setName() {
        key.addObserver(ob)
        key.name = "A"
        key.pause {
            key.name = "B"
        }
        println(key.name)
        key.name = "C"
    }

    @Test
    fun setFavor() {
        key.addObserver(ob)
        key.isFavor = true
        key.pause {
            key.isFavor = false
        }
        println(key.isFavor)
        key.isFavor = true
    }
}