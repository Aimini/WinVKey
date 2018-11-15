package com.startai.winvkey.ext_method

import android.view.Menu
import android.view.MenuItem

/**
 * @Author: AiMin
 * @Description:
 * @Date: Created in 21:56 2018/10/2
 * @Modified By:
 */

val Menu.size: Int
    get() = this.size()

operator fun Menu.get(i: Int): MenuItem {
    return this.getItem(i)
}
