package com.example.wantplant.data.remote.garden

import com.example.wantplant.data.local.GardenData
import com.example.wantplant.data.local.GardenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GardenRetrofitInterfaces {
    @POST("/api/gardens") // 서버의 endpoint
    fun postData(@Body gardenData: GardenData): Call<GardenResponse>

    @GET("/api/gardens")
    fun getGardens(@Query("page") page: Int, @Query("pageSize") pageSize: Int): Call<GardenResponse>
}