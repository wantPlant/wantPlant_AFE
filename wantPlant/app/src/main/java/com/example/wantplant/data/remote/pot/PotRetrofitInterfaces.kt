package com.example.wantplant.data.remote.pot

import com.example.wantplant.data.local.PotsResult
import com.example.wantplant.data.local.CompletedPotsResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PotRetrofitInterfaces {
    @GET("/api/pots")
    fun getPots(@Query("gardenId") gardenId: String, @Query("page") page: Int): Call<PotsResult>

    @GET("/api/pots/completed/app")
    fun getCompletedPots(@Query("gardenId") gardenId: String): Call<CompletedPotsResult>
}