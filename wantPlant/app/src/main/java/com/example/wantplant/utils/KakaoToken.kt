package com.example.wantplant.utils

import okhttp3.*
import org.json.JSONObject
import java.io.IOException

fun main() {
    val url = "https://kauth.kakao.com/oauth/token"

    val formBody = FormBody.Builder()
        .add("grant_type", "authorization_code")
        .add("client_id", "32506b027d9863ea7b622a9b750ba843")
        .add("redirect_uri", "https://localhost.com")
        .add("code", "Afl2egEmXeEWfGAfK-BNc8Os8xlTkIF9Wk_rpuL-Aaco42nh52f8S9B9LBQKKwzUAAABjWPlYeq2xj-RG-1vuA")
        .build()

    val client = OkHttpClient()
    val request = Request.Builder()
        .url(url)
        .post(formBody)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
        }

        override fun onResponse(call: Call, response: Response) {
            val responseData = response.body?.string()
            val tokens = JSONObject(responseData)
            println(tokens)
        }
    })
}
