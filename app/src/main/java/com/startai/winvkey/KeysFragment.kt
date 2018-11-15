package com.startai.winvkey


import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.SpannableStringBuilder
import android.view.*
import android.view.ContextMenu.ContextMenuInfo
import android.widget.*
import com.startai.winvkey.data_class.ObservableList
import com.startai.winvkey.data_class.Key
import com.startai.winvkey.data_class.ObservableListObserver
import kotlinx.android.synthetic.main.fragment_keys.*
import org.jetbrains.anko.find
import java.util.*

class KeysFragment() : Fragment() {

    private var mData: ObservableList<Key> = ObservableList<Key>()
    private var mAdapter: KeyItemAdapter? = null
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

    private val mNotifyObserver = object : ObservableListObserver<Key>() {
        override fun update(o: Observable?, arg: Any?) {
            this@KeysFragment.mAdapter?.notifyDataSetChanged()
            super.update(o, arg)
        }
        override fun add(source: ObservableList<Key>?, value: Key?) {}
        override fun delete(source: ObservableList<Key>?, value: Key?) {}
        override fun set(source: ObservableList<Key>?, index: Int, value: Key?) {}
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_keys, container, false)
        val listView: ListView = view.find<ListView>(R.id.main_view)
        registerForContextMenu(listView)
        return view

    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mAdapter = KeyItemAdapter(this.activity!!, R.layout.fragment_keys_item, mData)
        mAdapter?.keyFavored = { it ->
            this@KeysFragment.keyFavor(it)
        }

            main_view.adapter = mAdapter
            main_view.onItemClickListener = object :AdapterView.OnItemClickListener{
                override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val key: Key = main_view.adapter.getItem(position) as Key
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
                        mData.remove(key)
                        return true
                    }
                    R.id.item_modify_key -> {
                        this.showKeyModifyDialog(KeysFragment@context!!, key)
                    }
                }
                return true
            }

        }
        return super.onContextItemSelected(item)
    }



    fun setKeysList(keys: ObservableList<Key>) {
        this.mData.deleteObserver(mNotifyObserver)
        keys.addObserver(mNotifyObserver)
        this.mData = keys
        mAdapter?.notifyDataSetChanged()
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

            try {
                val keyCode = keyCodeStr.toInt()
                val key = Key(keyCode, keyName, false)
                mData.add(key)
                Toast.makeText(activity, key.toString(), Toast.LENGTH_SHORT).show()

            } catch (e: NumberFormatException) {
                Toast.makeText(activity, "key code must be a number", Toast.LENGTH_SHORT).show()
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
        switchIsFovour.isChecked = key.isFavor
        //添加按钮操作
        builder.setPositiveButton("确定") { dialog, which ->
            val keyCodeStr = inputKeyCode.text.toString().trim()
            val keyName = inputKeyName.text.toString().trim()
            Toast.makeText(activity, "$keyName,$keyCodeStr", Toast.LENGTH_SHORT).show()
            try {
                val keyCode = keyCodeStr.toInt()
                val newKey = Key(
                        keyCode,
                        keyName,
                        switchIsFovour.isChecked
                )
                mData[mData.indexOf(key)] = newKey
            } catch (e: NumberFormatException) {
                Toast.makeText(activity, "key code must be a number", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("取消") { _, _ -> }
        builder.show()
    }


    var keyFavor: (Key) -> Unit = { key: Key -> }
    var keyPressed: (Key) -> Unit = { key: Key -> }
}// Required empty public constructor
