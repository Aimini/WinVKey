package com.win_vkey.startai.winvkey

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.win_vkey.startai.winvkey.data_class.Key
import com.win_vkey.startai.winvkey.database.database
import com.win_vkey.startai.winvkey.ext_method.get
import com.win_vkey.startai.winvkey.ext_method.size
import kotlinx.android.synthetic.main.activity_main.*
import java.net.URISyntaxException

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
        mSettingsFragment.settings = this.database.getSettings();
        myHttpHelper.settings = mSettingsFragment.settings
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

        mFavorKeysFragment.setKeysList(favorKeys)
        mFavorKeysFragment.keyDeleted = { key ->
            database.deleteKey(key)
            mKeysFragment.removeKey(key)
        }
        mFavorKeysFragment.keyModified = {  oldKey, newKey ->
            database.updateKey(oldKey, newKey)
            mKeysFragment.modifyKey(oldKey, newKey)
        }
        mFavorKeysFragment.keyFavor = {
            key ->
            database.updateKey(key, key)
            mKeysFragment.modifyKey(key, key)
            if (key.isFavor > 0)
                mFavorKeysFragment.addKey(key)
            else
                mFavorKeysFragment.removeKey(key)
        }


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
            val settings = mSettingsFragment.settings;
            if (settings.host.isBlank()) {
                Toast.makeText(this, "host can't be empty", Toast.LENGTH_SHORT).show()
                return
            }
            // create Settings object, check
            this.database.saveSettings(settings)

        } catch (e: NumberFormatException) {
            Toast.makeText(this, "invalid port(number between 0 and 65535)", Toast.LENGTH_SHORT).show()
        } catch (e: URISyntaxException) {
            Toast.makeText(this, "invalid host", Toast.LENGTH_SHORT).show()
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
