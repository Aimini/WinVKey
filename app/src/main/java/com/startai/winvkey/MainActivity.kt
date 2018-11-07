package com.win_vkey.startai.winvkey

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.win_vkey.startai.winvkey.data_class.Key
import com.win_vkey.startai.winvkey.database.database
import com.win_vkey.startai.winvkey.ext_method.get
import com.win_vkey.startai.winvkey.ext_method.size
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Call
import okhttp3.Response
import java.io.IOException
import java.net.URISyntaxException
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {
    private val mFragmentList = ArrayList<Fragment>();
    private val mKeysFragment = KeysFragment();
    private val mFavorKeysFragment = KeysFragment()
    private val mSettingsFragment = SettingsFragment()

    private val myHttpHelper = MyHttpHelper()
    //绑定viewPager的页面到底部导航栏按钮
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        for (i in 0 until navigation.menu.size) {
            //如果当前选中的导航选项为导航栏中下标为i的选项，则将当前viewPager设置为第i个页面
            if (item == navigation.menu[i]) {
                vp.currentItem = i
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFragments()
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }


    private fun initFragments() {
        mSettingsFragment.setSettings( this.database.getSettings())
        myHttpHelper.settings = mSettingsFragment.getSettings()
        mFavorKeysFragment.isFavor = true
        var allKeys = this.database.getAllKey().toMutableList()
        var favorKeys = allKeys.filter { key -> key.isFavor > 0 }.toMutableList()
        mKeysFragment.setKeysList(allKeys)
        mKeysFragment.keyAdded = { key ->
            database.saveKey(key)
            if (key.isFavor > 0)
                mFavorKeysFragment.addKey(key)
        }

        mKeysFragment.keyDeleted = { key ->
            database.deleteKey(key)
            if (key.isFavor > 0)
                mFavorKeysFragment.removeKey(key)
        }
        mKeysFragment.keyModified = { oldKey, newKey ->
            database.updateKey(oldKey, newKey)
            if (oldKey.isFavor > 0)
                mFavorKeysFragment.modifyKey(oldKey, newKey)
        }
        mKeysFragment.keyFavor = { key ->
            database.updateKey(key, key)
            if (key.isFavor > 0)
                mFavorKeysFragment.addKey(key)
            else
                mFavorKeysFragment.removeKey(key)
        }

        var keyPressedCallback = { key: Key ->
            myHttpHelper.sendKey(key).enqueue(object : Callback, okhttp3.Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    this@MainActivity.runOnUiThread(object : Runnable {
                        override fun run() {
                            Toast.makeText(this@MainActivity, "send key failed\n${e.toString()}", Toast.LENGTH_LONG).show()
                            Log.e("send key", e.toString())
                        }
                    })
                }

                override fun onResponse(call: Call?, response: Response?) {
                    this@MainActivity.runOnUiThread(object : Runnable {
                        override fun run() {
                            if (response != null) {
                                if (response.code() == 200)
                                    Toast.makeText(this@MainActivity, "send key succeed", Toast.LENGTH_SHORT).show()
                                else {
                                    Toast.makeText(this@MainActivity, "send key failed\n${response.body().toString()}", Toast.LENGTH_LONG).show()
                                }
                            }

                        }
                    })
                }
            })
        }
        mKeysFragment.keyPressed = keyPressedCallback;
        mFavorKeysFragment.setKeysList(favorKeys)
        mFavorKeysFragment.keyDeleted = { key ->
            database.deleteKey(key)
            mKeysFragment.removeKey(key)
        }
        mFavorKeysFragment.keyModified = { oldKey, newKey ->
            database.updateKey(oldKey, newKey)
            mKeysFragment.modifyKey(oldKey, newKey)
        }
        mFavorKeysFragment.keyFavor = { key ->
            database.updateKey(key, key)
            mKeysFragment.modifyKey(key, key)
            if (key.isFavor > 0)
                mFavorKeysFragment.addKey(key)
            else
                mFavorKeysFragment.removeKey(key)
        }
        mFavorKeysFragment.keyPressed = keyPressedCallback;

/*
        mFavorKeysFragment.keyFavor;
        mFavorKeysFragment.keyModified = this:
*/
        with(mFragmentList) {
            add(mFavorKeysFragment)
            add(mKeysFragment)
            add(mSettingsFragment)
        }

        //set pager's adapter; adapter connect the logic between  data and gui
        vp.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment = mFragmentList[position]
            override fun getCount(): Int = mFragmentList.size
        }

        vp.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                navigation.menu[position].isChecked = true;
            }
        })


    }

    fun saveConfig(v: View) {
        try {
            //get settings, check NumberFormatException,URISyntaxException
            val settings = mSettingsFragment.getSettings();
            if (settings.host.isBlank()) {
                Toast.makeText(this, "host can't be empty", Toast.LENGTH_SHORT).show()
                return
            }
            // create Settings object, check
            this.database.saveSettings(settings)

        } catch (e: NumberFormatException) {
            Toast.makeText(this, "invalid kport(number between 0 and 65535)", Toast.LENGTH_LONG).show()
        } catch (e: URISyntaxException) {
            Toast.makeText(this, "invalid host", Toast.LENGTH_LONG).show()
        }
        Toast.makeText(this, "Settings Saved", Toast.LENGTH_SHORT).show()
    }


    fun showSettingsHelp(v: View?) {
        AlertDialog.Builder(this).apply {
            setTitle("Settings help")
            setMessage("host not need preffix \"http://\"\nport must between 0 ~ 65535")
            show()
        }
    }

}
