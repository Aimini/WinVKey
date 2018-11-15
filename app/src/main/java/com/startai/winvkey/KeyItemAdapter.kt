package com.startai.winvkey

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import com.startai.winvkey.data_class.Key
import kotlinx.android.synthetic.main.fragment_keys_item.view.*

class KeyItemAdapter: ArrayAdapter<Key>{
    var keyFavored:((key: Key)->Unit)? = null
    private var keyFavorListener: CompoundButton.OnCheckedChangeListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
        if(keyFavored != null && buttonView != null) {
            val key: Key = buttonView.tag as Key
            key.isFavor = isChecked
            keyFavored?.invoke(key)
        }
    }

    constructor(context: Context, resource: Int) : super(context, resource)
    constructor(context: Context, resource: Int, textViewResourceId: Int) : super(context, resource, textViewResourceId)
    constructor(context: Context, resource: Int, objects: Array<Key>) : super(context, resource, objects)
    constructor(context: Context, resource: Int, textViewResourceId: Int, objects: Array<Key>) : super(context, resource, textViewResourceId, objects)
    constructor(context: Context, resource: Int, objects: MutableList<Key>) : super(context, resource, objects)
    constructor(context: Context, resource: Int, textViewResourceId: Int, objects: MutableList<Key>) : super(context, resource, textViewResourceId, objects)

    public operator fun get(index:Int): Key {
        return this.getItem(index)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_keys_item, null)
        val key = this[position]
        view.textView.text = key.name
        view.switch_key_item_favor.isChecked = key.isFavor
        view.switch_key_item_favor.tag = key
        view.switch_key_item_favor.setOnCheckedChangeListener(keyFavorListener)
        return view
    }
}