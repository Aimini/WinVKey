package com.win_vkey.startai.winvkey

import com.win_vkey.startai.winvkey.data_class.Key
import com.win_vkey.startai.winvkey.data_class.Settings
import okhttp3.*

/**
 * @Author: AiMin
 * @Description:
 * @Date: Created in 17:02 2018/11/1
 * @Modified By:
 */
class MyHttpHelper {
    var settings: Settings? = null

    fun sendKey(key: Key): Call {
        if (settings != null) {
            var client: OkHttpClient = OkHttpClient()
            val rqeust: Request = Request.Builder()
                    .url(HttpUrl.parse("http://${settings!!.host}:${settings!!.port}"))
                    .post(FormBody.Builder()
                            .add("code", "${key.code}")
                            .build())
                    .build()
            return client.newCall(rqeust)
        } else {
            throw Exception("setting must be initilize")
        }
    }
}