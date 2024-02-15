package com.example.wantplant.utils

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://ec2-3-38-93-79.ap-northeast-2.compute.amazonaws.com:8080/"
var accessToken: String? = null // 로그인 성공 후 이 변수에 accessToken 저장

fun getRetrofit(): Retrofit {

    val httpClient = OkHttpClient.Builder()
    httpClient.addInterceptor(Interceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
        if (accessToken != null) {
            Log.i("Network", "카카오톡으로 로그인 성공 $accessToken")
            requestBuilder.addHeader("Authorization", "Bearer $accessToken")
        }
        val request = requestBuilder.build()
        return@Interceptor chain.proceed(request)
    })

    val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient.build())
        .build()

    return retrofit
}
