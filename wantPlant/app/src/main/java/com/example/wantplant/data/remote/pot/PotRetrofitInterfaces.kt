package com.example.wantplant.data.remote.pot

import com.example.wantplant.data.local.PotsResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PotRetrofitInterfaces {
    @GET("api/pots")
    fun getPots(@Query("gardenId") gardenId: Int, @Query("page") page: Int): Call<PotsResult>
}