package com.example.wantplant.data.remote.tag

import com.example.wantplant.data.remote.tag.request.TagPatchRequest
import com.example.wantplant.data.remote.tag.request.TagPostRequest
import com.example.wantplant.data.remote.tag.response.TagDeleteResponse
import com.example.wantplant.data.remote.tag.response.TagGetDayResponse
import com.example.wantplant.data.remote.tag.response.TagGetMonthResponse
import com.example.wantplant.data.remote.tag.response.TagPatchResponse
import com.example.wantplant.data.remote.tag.response.TagPostResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TagRetrofitInterfaces {
    @POST("/api/tag/add")
    fun postTag(@Header("Authorization") token: String?, @Body tagPostRequest: TagPostRequest): Call<TagPostResponse>

    @PATCH("/api/tag/update")
    fun patchTag(@Header("Authorization") token: String?, @Body tagPatchRequest: TagPatchRequest): Call<TagPatchResponse>

    @DELETE("/api/tag/{tagId}")
    fun deleteTag(@Header("Authorization") token: String?, @Path(value = "tagId") tagId: Long): Call<TagDeleteResponse>

    @GET("/api/tag/month")
    fun getMonthTag(@Header("Authorization") token: String?, @Query(value = "year") year: Int, @Query(value = "month") month: Int): Call<TagGetMonthResponse>

    @GET("/api/tag/day")
    fun getDayTag(@Header("Authorization") token: String?, @Query(value = "year") year: Int, @Query(value = "month") month: Int, @Query(value = "day") day: Int): Call<TagGetDayResponse>
}