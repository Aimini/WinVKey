package com.startai.winvkey

import com.startai.winvkey.data_class.Key
import com.startai.winvkey.data_class.Settings
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
            val client: OkHttpClient = OkHttpClient()
            val request: Request = Request.Builder()
                    .url(HttpUrl.parse("http://${settings!!.host}:${settings!!.port}/key-down"))
                    .post(FormBody.Builder()
                            .add("KeyCode", "${key.code}")
                            .build())
                    .build()
            return client.newCall(request)
        } else {
            throw Exception("setting must be initilize")
        }
    }
}