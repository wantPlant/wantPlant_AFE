package com.example.wantplant.data.remote.pot

import com.example.wantplant.data.local.PotsResult
import com.example.wantplant.data.local.CompletedPotsResult
import com.example.wantplant.data.remote.pot.request.PotsPostRequest
import com.example.wantplant.data.remote.pot.response.PotsPostResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface PotRetrofitInterfaces {
    @GET("/api/pots")
    fun getPots(@Header("Authorization") authorization: String, @Query("gardenId") gardenId: Int, @Query("page") page: Int): Call<PotsResult>

    @POST("/api/pots")
    fun postPots(@Header("Authorization") authorization: String, @Body potsPostRequest: PotsPostRequest): Call<PotsPostResponse>

    @GET("/api/pots/completed/app")
    fun getCompletedPots(@Header("Authorization") authorization: String, @Query("gardenId") gardenId: String): Call<CompletedPotsResult>
}