package com.example.wantplant.data.remote.goal

import com.example.wantplant.data.local.GoalResult
import com.example.wantplant.data.remote.goal.request.GoalPostRequest
import com.example.wantplant.data.remote.goal.response.GoalPostResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface GoalRetrofitInterfaces {
    @POST("/api/goals")
    fun postGoal(@Body goalPostRequest: GoalPostRequest): Call<GoalPostResponse>

    @GET("/api/goals/todos")
    fun getGoal(@Query("potId") potId: String): Call<GoalResult>
}