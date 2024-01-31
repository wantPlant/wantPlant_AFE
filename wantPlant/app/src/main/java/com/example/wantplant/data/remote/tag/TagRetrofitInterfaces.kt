package com.example.wantplant.data.remote.tag

import com.example.wantplant.data.remote.tag.request.TagPostRequest
import com.example.wantplant.data.remote.tag.response.TagGetDayResponse
import com.example.wantplant.data.remote.tag.response.TagGetMonthResponse
import com.example.wantplant.data.remote.tag.response.TagPostResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface TagRetrofitInterfaces {
    @POST("/api/tag/add")
    fun postTag(@Body tagPostRequest: TagPostRequest): Call<TagPostResponse>

    @GET("/api/tag/month")
    fun getMonthTag(@Query(value = "year") year: Int, @Query(value = "month") month: Int): Call<TagGetMonthResponse>

    @GET("/api/tag/day")
    fun getDayTag(@Query(value = "year") year: Int, @Query(value = "month") month: Int, @Query(value = "day") day: Int): Call<TagGetDayResponse>
}