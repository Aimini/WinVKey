package com.startai.winvkey

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
        super.clear()
    }

    override fun add(element: E): Boolean {
        mShareLists.forEach { list ->
            val f = list.getFilter()
            if (f == null || f(element))
                list.addWithoutNotify(element)
        }
        return super.add(element)
    }

    override fun add(index: Int, element: E) {
        mShareLists.forEach { list ->
            val f = list.getFilter()
            if (f == null || f(element))
                list.addWithoutNotify(element)
        }
        super.add(index, element)
    }


    override fun addAll(elements: Collection<E>): Boolean {
        elements.forEach { element ->
            mShareLists.forEach { list ->
                val f = list.getFilter()
                if (f == null || f(element))
                    list.addWithoutNotify(element)
            }
        }
        return super.addAll(elements)
    }

    override fun addAll(index: Int, elements: Collection<E>): Boolean {
        elements.forEach { element ->
            mShareLists.forEach { list ->
                val f = list.getFilter()
                if (f == null || f(element))
                    list.addWithoutNotify(element)
            }
        }
        return super.addAll(index, elements)
    }

    public fun addAll(elements: Array<E>): Boolean {
        return addAll(elements.asList())
    }

    override fun removeRange(fromIndex: Int, toIndex: Int) {
        for (index in fromIndex until toIndex) {
            mShareLists.forEach { list -> list.removeWithoutNotify(this@ShareSourceList.elementAt(index)) }
        }

        super.removeRange(fromIndex, toIndex)
    }

    override fun removeAt(index: Int): E {
        mShareLists.forEach { list -> list.removeWithoutNotify(this@ShareSourceList.elementAt(index)) }
        return super.removeAt(index)
    }

    override fun remove(element: E): Boolean {
        mShareLists.forEach { list -> list.removeWithoutNotify(element) }
        return super.remove(element)
    }

    /*
        remove element and Ignore list gived，
        called by ShareList
     */
    private fun removeIgnoreShareList(element: E, list: ShareList<E>) {
        super.remove(element)
        mShareLists.forEach { one -> if (one != list) list.removeWithoutNotify(element) }
    }

    /*
        add element and Ignore list gived，
        called by ShareList
     */
    private fun addIgnoreShareList(element: E, list: ShareList<E>) {
        mShareLists.forEach { one -> if (one != list){
                    val f = one.getFilter()
                    if (f == null || f(element))
                        one.addWithoutNotify(element)
                }
            }
        super.add(element)
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

        constructor(source: ShareSourceList<E>, filter: ((E) -> Boolean)?=null) : super() {
            this.mSource = source
            this.mFilter = filter
            source.forEach { e ->
                if (mFilter == null || mFilter.invoke(e))
                    super.add(e)
            }
        }

        override fun clear() {
            this.forEach { e -> mSource.removeIgnoreShareList(e, this) }
            super.clear()
        }

        override fun add(element: E): Boolean {
            mSource.addIgnoreShareList(element, this)
            return super.add(element)
        }

        override fun add(index: Int, element: E) {
            mSource.addIgnoreShareList(element, this)
            super.add(index, element)
        }

        override fun removeRange(fromIndex: Int, toIndex: Int) {
            for (i in fromIndex until toIndex) {
                mSource.removeIgnoreShareList(this.elementAt(i), this)
            }
            super.removeRange(fromIndex, toIndex)
        }

        override fun removeAt(index: Int): E {
            mSource.removeIgnoreShareList(this.elementAt(index), this)
            return super.removeAt(index)
        }

        override fun remove(element: E): Boolean {
            mSource.removeIgnoreShareList(element, this)
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





