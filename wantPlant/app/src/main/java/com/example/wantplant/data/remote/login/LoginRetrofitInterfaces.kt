package com.example.wantplant.data.remote.garden

import com.example.wantplant.data.local.GardenData
import com.example.wantplant.data.local.GardenResponse
import com.example.wantplant.data.local.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginRetrofitInterfaces {
    @POST("/api/members/login")
    fun login(@Query("accessToken") accessToken: String): Call<LoginResponse>
}