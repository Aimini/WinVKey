package com.startai.winvkey.data_class

import org.junit.After
import org.junit.Before
import org.junit.Test

import java.util.Arrays

/**
 * @Author: AiMin
 * @Description: Test Observable List
 * @Date: Created in 20:39 2018/11/15
 * @Modified By: AiMin
 */
class ObservableListTest {
    internal var list = ObservableList<Int>()
    internal var ob: ObservableListObserver<Int> = object : ObservableListObserver<Int>() {

        override fun add(source: ObservableList<Int>, value: Int) {
            println("list:" + source.toString())
            println("add int:" + value.toString())

        }

        override fun delete(source: ObservableList<Int>, value: Int) {
            println("list:" + source.toString())
            println("delete int:" + value.toString())

        }

        override fun set(source: ObservableList<Int>, index: Int, value: Int) {
            println("list:" + source.toString())
            println("set int:" + source[index].toString() + "->" + value.toString())

        }
    }

    @Before
    @Throws(Exception::class)
    fun setUp() {
        list.add(1)
        list.add(2)
        list.add(3)
        list.add(4)
        list.add(5)
        list.addObserver(ob)
    }


    @After
    @Throws(Exception::class)
    fun done() {
        println(list.toString())
    }


    @Test
    fun deleteObserver() {
        list.add(10)
        list.deleteObserver(ob)
        list.add(11)
    }

    @Test
    fun deleteObservers() {
        list.add(10)
        list.deleteObservers()
        list.add(11)
    }

    @Test
    fun set() {
        list[1] = 12
    }

    @Test
    fun add() {
        list.add(4)
    }

    @Test
    fun add1() {
        list.add(1, 12)
    }

    @Test
    fun remove() {
        list.removeAt(1)
    }

    @Test
    fun remove1() {
        list.remove(3)
    }

    @Test
    fun clear() {
        list.clear()
    }

    @Test
    fun addAll() {
        list.addAll(Arrays.asList(4, 5, 6))
    }

    @Test
    fun addAll1() {
        list.addAll(0, Arrays.asList(-2, -1, 0))
    }

    @Test
    fun removeRange() {
        list.removeRange(0, 2)
    }

    @Test
    fun removeAll() {
        list.removeAll(Arrays.asList(-1, 1, 2))
    }

    @Test
    fun retainAll() {
        list.retainAll(Arrays.asList(-1, 1, 2))
    }

    @Test
    fun removeIf() {
        list.removeIf { integer -> integer!! % 2 == 0 }
    }

    @Test
    fun replaceAll() {
        list.replaceAll { integer ->
            if (integer!! % 2 == 0)
                return@replaceAll integer +2
            integer
        }
    }
}