package com.startai.winvkey.data_class

import android.util.Patterns
import android.webkit.URLUtil
import java.net.URISyntaxException

/**
 * @Author: AiMin
 * @Description: user settings data class, include host and port check,when
 *  check failed, throw
 * @Date: Created in 2:44 2018/10/19
 * @Modified By:
 */
class Settings(host: String = "127.0.0.1", port: Int = 8889) {

    var host: String = ""
        set(value) {
            if (!Patterns.DOMAIN_NAME.matcher(value).matches())
                throw URISyntaxException(value, "")
            field = value

        }



    var port: Int = 0
        set(value) {
            if (value !in 1..65525)
                throw NumberFormatException("port must in 1 - 65525")
            field = value
        }

    init {
        this.host = host
        this.port = port
    }
}

