package com.startai.winvkey.data_class

import org.junit.After
import org.junit.Before
import kotlin.reflect.KProperty
import org.junit.Test
import java.util.*
import kotlin.collections.ArrayList

/**
 * @Author: AiMin
 * @Description:
 * @Date: Created in 19:38 2018/11/16
 * @Modified By:
 */
class ObservableDataListTest {
    internal var list = ObservableDataList<Key>()
    internal var ob: ObservableDataListObserver<Key> = object : ObservableDataListObserver<Key>() {
        override fun add(source: ObservableList<Key>, value: Key) {
            println("add int:" + value.toString())

        }

        override fun delete(source: ObservableList<Key>, value: Key) {
            println("delete int:" + value.toString())

        }

        override fun set(source: ObservableList<Key>, index: Int, value: Key) {
            println("set int:" + source[index].toString() + "->" + value.toString())

        }

        override fun itemChanged( obj: Key, property: KProperty<*>, oldValue: Any, newValue: Any) {
            println("property ${property.name}:${property.returnType.toString()}:${oldValue}->${newValue}")
        }
    }

    @Before
    @Throws(Exception::class)
    fun setUp() {
        list.add(Key(1,"1",true))
        list.add(Key(2,"2",true))
        list.add(Key(3,"3",true))
        list.add(Key(4,"4",true))
        list.add(Key(5,"5",true))
        list.addObserver(ob)
        println(list.toString())
    }


    @After
    @Throws(Exception::class)
    fun done() {
        println(list.toString())
    }

    @Test
    fun set() {
        var k = list.get(1);
        k.code = 111;
        list.set(1,Key(11,"11",false));
        k.code = 1111;
    }

    @Test
    fun add() {
        var k = Key(11,"11",false)
        k.name   = "111";
        list.add(k);
        k.name = "111";
    }

    @Test
    fun add1() {
        var k = Key(11,"11",false)
        k.isFavor = true;
        list.add(1,k);
        k.isFavor = false;
    }

    @Test
    fun remove() {
        var index:Int = 2;
        var key = list.get(index)
        key.code =100;
        list.removeAt(index);
        key.code  = 1000;
    }

    @Test
    fun remove1() {
        var index:Int = 2;
        var key = list.get(index)
        key.name ="100";
        list.remove(key);
        key.name  = "1000";
    }

    @Test
    fun clear() {
        var index:Int = 2;
        var key1 = list.get(index)
        var key2 = list.get(list.size - 1)

        key1.name ="100";
        key2.isFavor = true;
        list.clear();
        key1.name  = "1000";
        key2.isFavor =false;
    }

    @Test
    fun addAll() {
        var key1 = Key(101,"101",true)
        var key2 = Key(102,"102",false)
        var key3 = Key(103,"103",true)
        var l:Collection<Key> = listOf(key1,key2,key3)
                l.forEach { e ->
                    e.code = e.code + 2;
                }
        list.addAll(l)
        l.forEach { e ->
            e.code = e.code + 2;
        }
    }

    @Test
    fun addAll1() {
            var key1 = Key(101,"101",true)
            var key2 = Key(102,"102",false)
            var key3 = Key(103,"103",true)
            var l:Collection<Key> = listOf(key1,key2,key3)
            l.forEach { e ->
                e.code = e.code + 2;
            }
            list.addAll(2,l)
            l.forEach { e ->
                e.code = e.code + 2;
            }
    }

    @Test
    fun removeRange() {
        var from = 1
        var to = 3
        var l = list.filterIndexed { index, key -> index in 1 until 3  }.toList()
        l.forEach { it.code += 2 }
        list.removeRange(from,to)
        l.forEach { it.code += 2 }
    }

    @Test
    fun removeAll() {
        list.forEach { it.name += "ff" }
        var l = ArrayList<Key>(list)
        list.removeAll(listOf(list.get(1),list.get(2)))
        l.forEach { it.name += "ffff" }
    }

    @Test
    fun retainAll() {
        list.forEach { it.name += "ff" }
        var l = ArrayList<Key>(list)
        list.retainAll(listOf(list.get(1),list.get(2)))
        l.forEach { it.name += "ffff" }
    }

    @Test
    fun removeIf() {
        var lambda = {key:Key -> key.code %2 == 0}
        var l = list.filter(lambda).toList()
        l.forEach { it.code += 10 }
        list.removeIf(lambda)
        l.forEach { it.code += 10 }
    }

    @Test
    fun replaceAll() {
        var lambda = {key:Key -> Key(key.code +2,"12",false)}
        list.forEach {
           it.code += 10
        }

        list.replaceAll(lambda)
        list.forEach {
            it.code += 10
        }
    }
}