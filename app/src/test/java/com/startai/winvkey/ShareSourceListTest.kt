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
        assertArrayEquals(l1.toArray(), arrayOf(1,2,3))
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
    }

    @Test
    fun addAll1() {
    }

    @Test
    fun addAll2() {
    }

    @Test
    fun removeRange() {
    }

    @Test
    fun removeAt() {
    }

    @Test
    fun remove() {
    }

    @Test
    fun shareListInstance() {
    }
}