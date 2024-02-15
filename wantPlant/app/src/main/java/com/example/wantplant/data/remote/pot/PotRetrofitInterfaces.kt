package com.example.wantplant.data.remote.pot

import com.example.wantplant.data.local.CompletedPotResult
import com.example.wantplant.data.local.PotsResult
import com.example.wantplant.data.remote.pot.request.PotPatchRequest
import com.example.wantplant.data.remote.pot.request.PotPostRequest
import com.example.wantplant.data.remote.pot.response.PotPatchResponse
import com.example.wantplant.data.remote.pot.response.PotPostResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PotRetrofitInterfaces {
    @GET("/api/pots")
    fun getPots(@Query("gardenId") gardenId: String, @Query("page") page: Int): Call<PotsResult>

    @GET("/api/completed-pots/app")
    fun getCompletedPots(@Query("gardenId") gardenId: String): Call<CompletedPotResult>

    @GET("/api/pots/names")
    fun getPotNames(@Query("gardenId") gardenId: String): Call<PotsResult>

    @POST("/api/pots/goals/todos")
    fun postPotGoalTodo(@Body potGoalTodoPostRequest: PotPostRequest): Call<PotPostResponse>

    @PATCH("/api/pots/{potId}")
    fun patchPot(@Path("potId") potId: Int, @Body potPatchRequest: PotPatchRequest): Call<PotPatchResponse>

}