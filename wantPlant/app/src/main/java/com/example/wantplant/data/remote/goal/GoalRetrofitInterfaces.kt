package com.example.wantplant.data.remote.goal

import com.example.wantplant.data.remote.goal.request.GoalPostRequest
import com.example.wantplant.data.remote.goal.response.GoalPostResponse
import com.example.wantplant.data.remote.goal.response.GoalTodoGetResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface GoalRetrofitInterfaces {
    @POST("/api/goals/todos")
    fun postGoalTodo(
        @Header("Authorization") token: String?,
        @Body goalPostRequest: GoalPostRequest): Call<GoalPostResponse>

    @GET("/api/goals/todos/date")
    fun getGoalTodo(
        @Header("Authorization") token: String?,
        @Query(value = "date") date: String,
        @Query(value = "potId") potId: Long
    ): Call<GoalTodoGetResponse>
}