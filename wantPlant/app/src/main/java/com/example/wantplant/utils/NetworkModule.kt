package com.example.wantplant.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://ec2-3-34-198-148.ap-northeast-2.compute.amazonaws.com:8080" // /를 붙이게 되면 interface부분에서 /를 제외하고 적어줘야함

fun getRetrofit(): Retrofit {
    val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).build()

    return retrofit
}