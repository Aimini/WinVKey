package com.win_vkey.startai.winvkey

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.SpannableStringBuilder
import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import android.widget.*
import com.win_vkey.startai.winvkey.data_class.Key
import kotlinx.android.synthetic.main.fragment_keys.*
import kotlinx.android.synthetic.main.fragment_keys_item.view.*
import org.jetbrains.anko.find


class KeysFragment() : Fragment() {

    private var mData: MutableList<Key> = mutableListOf()
    var isFavor: Boolean = false
        set(value) {
            if (main_view != null) {
                button_add_key.visibility = if (isFavor) {
                    View.INVISIBLE
                } else {
                    View.VISIBLE
                }
            }
            field = value
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater!!.inflate(R.layout.fragment_keys, container, false)
        var listView: ListView = view.find<ListView>(R.id.main_view)
        registerForContextMenu(listView)
        return view

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        main_view.adapter = object : ArrayAdapter<Key>(activity, R.layout.fragment_keys_item, mData) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = LayoutInflater.from(context).inflate(R.layout.fragment_keys_item, null)
                val key = mData[position]
                view.textView.text = key.name
                view.switch_key_item_favor.isChecked = key.isFavor > 0
                view.switch_key_item_favor.setOnCheckedChangeListener { buttonView: CompoundButton, isChecked: Boolean ->
                    if (isChecked) {
                        key.isFavor = 1
                    } else
                        key.isFavor = 0
                    keyFavor(key)
                }
                return view;
            }
        }

        main_view.onItemClickListener = object :AdapterView.OnItemClickListener{
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val key:Key = main_view.adapter.getItem(position) as Key
                keyPressed(key)
            }
        }

        button_add_key.setOnClickListener { it: View? ->
            var builder = getKeyDialogBuilder(this@KeysFragment.activity!!)
            builder.show()
        }

        button_add_key.visibility = if (isFavor) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
    }


    override fun onCreateContextMenu(menu: ContextMenu, v: View,
                                     menuInfo: ContextMenuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo)
        if (v == main_view) {
            val info = menuInfo as AdapterView.AdapterContextMenuInfo
            menu.setHeaderTitle(mData[info.position].toString())
            (context as Activity).menuInflater.inflate(R.menu.key_operations, menu)
        }
    }


    override fun onContextItemSelected(item: MenuItem?): Boolean {
        if (item != null && item.menuInfo != null) {
            if (userVisibleHint) {
                var info: AdapterView.AdapterContextMenuInfo = item.menuInfo as AdapterView.AdapterContextMenuInfo
                var key: Key = main_view.adapter.getItem(info.position) as Key
                when (item.itemId) {
                    R.id.item_detele_key -> {
                        this.keyDeleted(key)
                        mData.remove(key)
                        (main_view.adapter as ArrayAdapter<*>).notifyDataSetChanged()
                        return true
                    }
                    R.id.item_modify_key -> {
                        this.showKeyModifyDialog(KeysFragment@context!!, key)
                    }
                }
                return true;
            }

        }
        return super.onContextItemSelected(item)
    }

    fun addKey(key: Key, index: Int? = null) {
        if (index != null)
            mData.add(index, key)
        mData.add(key)
        (main_view.adapter as ArrayAdapter<*>).notifyDataSetChanged()
    }

    fun modifyKey(oldKey: Key, newKey: Key) {
        mData.filter { k -> k == oldKey }
                .forEach { k ->
                    k.isFavor = newKey.isFavor
                    k.code = newKey.code
                    k.name = newKey.name
                }
        (main_view.adapter as ArrayAdapter<*>).notifyDataSetChanged()
    }

    fun removeKey(key: Key) {
        mData.remove(key)
        (main_view.adapter as ArrayAdapter<*>).notifyDataSetChanged()

    }

    fun setKeysList(keys: MutableList<Key>) {
        this.mData = keys
        if (main_view != null)
            (main_view.adapter as ArrayAdapter<*>).notifyDataSetChanged()
    }


    private fun getKeyDialogBuilder(context: Context): AlertDialog.Builder {
        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_add_key, null)
        with(builder) {
            setTitle("添加按键")
            setView(view)
        }

        val inputKeyCode = view.findViewById<EditText>(R.id.key_code)
        val inputKeyName = view.findViewById<EditText>(R.id.key_name)

        //添加按钮操作
        builder.setPositiveButton("确定") { dialog, which ->
            val keyCodeStr = inputKeyCode.text.toString().trim()
            val keyName = inputKeyName.text.toString().trim()
            Toast.makeText(activity, "$keyName,$keyCodeStr", Toast.LENGTH_SHORT).show();
            try {
                val keyCode = keyCodeStr.toInt();
                val key = Key(keyCode, keyName, isFavor = 0)
                mData.add(key)
                keyAdded(key)
                (main_view.adapter as ArrayAdapter<*>).notifyDataSetChanged()
            } catch (e: NumberFormatException) {
                Toast.makeText(activity, "key code must be a number", Toast.LENGTH_SHORT).show();
            }
        }

        builder.setNegativeButton("取消") { _, _ -> }
        return builder
    }


    private fun showKeyModifyDialog(context: Context, key: Key) {
        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_modify_key, null)
        builder.setView(view)
        with(builder) {
            setTitle("修改按键${key}")
            setView(view)
        }

        val inputKeyCode = view.findViewById<EditText>(R.id.key_code)
        val inputKeyName = view.findViewById<EditText>(R.id.key_name)
        val switchIsFovour = view.findViewById<Switch>(R.id.switch_favor)
        inputKeyCode.text = SpannableStringBuilder(key.code.toString())
        inputKeyName.text = SpannableStringBuilder(key.name)
        switchIsFovour.isChecked = key.isFavor > 0;
        //添加按钮操作
        builder.setPositiveButton("确定") { dialog, which ->
            val keyCodeStr = inputKeyCode.text.toString().trim()
            val keyName = inputKeyName.text.toString().trim()
            Toast.makeText(activity, "$keyName,$keyCodeStr", Toast.LENGTH_SHORT).show();
            try {
                val keyCode = keyCodeStr.toInt()
                var newKey = Key(
                        keyCode,
                        keyName,
                        if (switchIsFovour.isChecked) 1 else 0
                )
                mData.set(mData.indexOf(key), newKey)
                keyModified(key, newKey)
                (main_view.adapter as ArrayAdapter<*>).notifyDataSetChanged()
            } catch (e: NumberFormatException) {
                Toast.makeText(activity, "key code must be a number", Toast.LENGTH_SHORT).show();
            }
        }

        builder.setNegativeButton("取消") { _, _ -> }
        builder.show()
    }


    var keyAdded: (Key) -> Unit = { key: Key -> }
    var keyFavor: (Key) -> Unit = { key: Key -> }
    var keyModified: (Key, Key) -> Unit = { keyOld: Key, keyNew: Key -> }
    var keyDeleted: (Key) -> Unit = { key: Key -> }
    var keyPressed: (Key) -> Unit = { key: Key -> }
}// Required empty public constructor
