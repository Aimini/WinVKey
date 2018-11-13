package com.startai.winvkey

import org.junit.Test

import org.junit.Assert.*

/**
 * @Author: AiMin
 * @Description:
 * @Date: Created in 22:23 2018/11/12
 * @Modified By:
 */
class ShareSourceListTest {
    var s = ShareSourceList<Int>();
    var l1 = s.shareListInstance { e -> e%2 == 0 }
    var l2 = s.shareListInstance { e -> e%2 == 1 }




    @Test
    fun clear() {
        s.addAll(arrayOf(1,2,3))
        s.clear()
        assertArrayEquals(l1.toArray(), arrayOf( ))
        assertArrayEquals(l2.toArray(), arrayOf( ))
    }

    @Test
    fun add() {
        s.add(1)
        assertArrayEquals(l1.toArray(), arrayOf( ))
        assertArrayEquals(l2.toArray(), arrayOf(1))
        s.add(2)
        assertArrayEquals(l1.toArray(), arrayOf(2))
        assertArrayEquals(l2.toArray(), arrayOf(1))
        s.add(3)
        assertArrayEquals(s.toArray(), arrayOf(1,2,3))
        assertArrayEquals(l1.toArray(), arrayOf(2))
        assertArrayEquals(l2.toArray(), arrayOf(1,3))
    }

    @Test
    fun add1() {
        s.add(0,1)
        s.add(1,2)
        s.add(0,3)
        assertArrayEquals(s.toArray(), arrayOf(3,1,2))
        assertArrayEquals(l1.toArray(), arrayOf(2))
        assertArrayEquals(l2.toArray(), arrayOf(1,3))

    }

    @Test
    fun addAll() {
        s.addAll(listOf(1,2,4))
        assertArrayEquals(s.toArray(), arrayOf(1,2,4))
        assertArrayEquals(l1.toArray(), arrayOf(2,4))
        assertArrayEquals(l2.toArray(), arrayOf(1))
    }

    @Test
    fun addAll1() {
        s.addAll(listOf(1,2,4))
        s.addAll(1,listOf(3,5,6))
        assertArrayEquals(s.toArray(), arrayOf(1,3,5,6,2,4))
        assertArrayEquals(l1.toArray(), arrayOf(2,4,6))
        assertArrayEquals(l2.toArray(), arrayOf(1,3,5))
    }

    @Test
    fun addAll2() {
        s.addAll(arrayOf(1,2,4))
        assertArrayEquals(s.toArray(), arrayOf(1,2,4))
        assertArrayEquals(l1.toArray(), arrayOf(2,4))
        assertArrayEquals(l2.toArray(), arrayOf(1))
    }

    @Test
    fun removeRange() {
        s.addAll(arrayOf(1,2,4,5,6,77,1,3))
        s.removeRange(2,5)
        assertArrayEquals(s.toArray(), arrayOf(1,2,77,1,3))
        assertArrayEquals(l1.toArray(), arrayOf(2))
        assertArrayEquals(l2.toArray(), arrayOf(1,77,1,3))
    }

    @Test
    fun removeAt() {
        s.addAll(arrayOf(1,2,4,3))
        s.removeAt(1)
        assertArrayEquals(s.toArray(), arrayOf(1,4,3))
        assertArrayEquals(l1.toArray(), arrayOf(4))
        assertArrayEquals(l2.toArray(), arrayOf(1, 3))
    }

    @Test
    fun remove() {
        var a:Int = 1;
        s.addAll(arrayOf(1,2,4,3,a))
        s.remove(a)
        assertArrayEquals(s.toArray(), arrayOf(2,4,3,1))
        assertArrayEquals(l1.toArray(), arrayOf(2,4))
        assertArrayEquals(l2.toArray(), arrayOf(3,1))
    }

    @Test
    fun shareListInstance() {
    }
}