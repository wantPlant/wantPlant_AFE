package com.example.wantplant.data.remote.tag

import com.example.wantplant.data.remote.tag.request.TagPostRequest
import com.example.wantplant.data.remote.tag.response.TagPostResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TagRetrofitInterfaces {
    @POST("/tag/add")
    fun postTag(@Body tagAdd: TagPostRequest): Call<TagPostResponse>
}