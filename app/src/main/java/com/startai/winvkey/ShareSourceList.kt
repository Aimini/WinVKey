package com.startai.winvkey

import java.util.function.Predicate

/**
 * @Author: AiMin
 * @Description: ShareSourceList provide data mSource for Share List.
 *  so when you modify The ShareSourceList(add/remove element,sort is no support now)
 *  the ShareList will follow the change.
 * @Date: Created in 20:32 2018/11/12
 * @Modified By:
 */

class ShareSourceList<E> : ArrayList<E>() {

    private var mShareLists: MutableList<ShareList<E>> = ArrayList<ShareList<E>>()

    override fun clear() {
        mShareLists.forEach {it-> it.clear() }
        super.clear()
    }

    override fun add(element: E): Boolean {
        addToShareList(element,null)
        return super.add(element)
    }

    override fun add(index: Int, element: E) {
        addToShareList(element,null)
        super.add(index, element)
    }


    override fun addAll(elements: Collection<E>): Boolean {
        elements.forEach {
            addToShareList(it,null)
        }
        return super.addAll(elements)
    }

    override fun addAll(index: Int, elements: Collection<E>): Boolean {
        elements.forEach {
            addToShareList(it,null)
        }
        return super.addAll(index, elements)
    }

    public fun addAll(elements: Array<E>): Boolean {
        elements.forEach {
            addToShareList(it,null)
        }

        return super.addAll(elements.asList())
    }

    //TODO: connect opertaion to ShareList
    override fun set(index: Int, element: E): E {
        return super.set(index, element)
    }


    public override fun removeRange(fromIndex: Int, toIndex: Int) {
        for (index in fromIndex until toIndex) {
            removeFromShareList(this.elementAt(index),null)
        }

        super.removeRange(fromIndex, toIndex)
    }

    override fun removeAt(index: Int): E {
        removeFromShareList(this.elementAt(index),null)
        return super.removeAt(index)
    }

    override fun remove(element: E): Boolean {
        removeFromShareList(element,null)
        return super.remove(element)
    }

    /*
        remove element and Ignore list gived，
        called by ShareList
     */
    private fun removeIgnoreNotify(element: E, ignore: ShareList<E>?) {
        removeFromShareList(element,ignore)
        super.remove(element)
    }

    /*
        add element and Ignore list gived，
        called by ShareList
     */
    private fun addIgnoreNotify(element: E, ignore: ShareList<E>) {
        addToShareList(element,ignore)
        super.add(element)
    }

    private fun addToShareList(element: E, ignore: ShareList<E>?) {
        mShareLists.forEach { one ->
            if (one != ignore) {
                val f = one.getFilter()
                if (f == null || f(element))
                    one.addWithoutNotify(element)
            }
        }
    }

    private fun removeFromShareList(element: E, ignore: ShareList<E>?) {
        mShareLists.forEach { one ->
            if (one != ignore) one.removeWithoutNotify(element) }
    }

    fun shareListInstance(filter: (E) -> Boolean): ShareList<E> {
        var instance = ShareList<E>(this, filter)
        mShareLists.add(instance)
        return instance
    }


    /*

    *  ShareList must create by getShareList
    *  element it owned decide by filter function.
    *
     */
    class ShareList<E> : ArrayList<E> {
        private var mSource: ShareSourceList<E>
        private val mFilter: ((E) -> Boolean)?

        constructor(source: ShareSourceList<E>, filter: ((E) -> Boolean)? = null) : super() {
            this.mSource = source
            this.mFilter = filter
            source.forEach { e ->
                if (mFilter == null || mFilter.invoke(e))
                    super.add(e)
            }
        }

        override fun clear() {
            this.forEach { e -> mSource.removeIgnoreNotify(e, this) }
            super.clear()
        }

        override fun add(element: E): Boolean {
            mSource.addIgnoreNotify(element, this)
            return super.add(element)
        }

        override fun add(index: Int, element: E) {
            mSource.addIgnoreNotify(element, this)
            super.add(index, element)
        }

        override fun removeRange(fromIndex: Int, toIndex: Int) {
            for (i in fromIndex until toIndex) {
                mSource.removeIgnoreNotify(this.elementAt(i), this)
            }
            super.removeRange(fromIndex, toIndex)
        }

        override fun removeAt(index: Int): E {
            mSource.removeIgnoreNotify(this.elementAt(index), this)
            return super.removeAt(index)
        }

        override fun remove(element: E): Boolean {
            mSource.removeIgnoreNotify(element, this)
            return super.remove(element)
        }

        override fun toString(): String {
            return super<java.util.ArrayList>.toString()
        }

        /*
         return element and don't notify source list,
         called by source list .
         */
        internal fun removeWithoutNotify(element: E) {
            super.remove(element)
        }

        internal fun addWithoutNotify(element: E) {
            super.add(element)
        }

        public fun getFilter(): ((E) -> Boolean)? {
            return mFilter;
        }
    }
}





