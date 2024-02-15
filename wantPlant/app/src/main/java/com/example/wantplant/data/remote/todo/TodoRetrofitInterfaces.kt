package com.example.wantplant.data.remote.todo

import com.example.wantplant.data.local.GardenResponse
import com.example.wantplant.data.local.TodoResponse
import com.example.wantplant.data.remote.tag.request.TagPatchRequest
import com.example.wantplant.data.remote.tag.request.TagPostRequest
import com.example.wantplant.data.remote.tag.response.TagPatchResponse
import com.example.wantplant.data.remote.tag.response.TagPostResponse
import com.example.wantplant.data.remote.todo.request.TodoPatchRequest
import com.example.wantplant.data.remote.todo.request.TodoPostRequest
import com.example.wantplant.data.remote.todo.response.TodoPatchResponse
import com.example.wantplant.data.remote.todo.response.TodoPostResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TodoRetrofitInterfaces {
    @POST("/api/todos")
    fun postTodo(@Body todoPostRequest: TodoPostRequest): Call<TodoPostResponse>

    @GET("/api/todos/{todoId}")
    fun getTodo(@Path("todoId") todoId: Int): Call<TodoResponse>

    @PATCH("/api/todos/{todoId}")
    fun patchTodo(@Path("todoId") todoId: Int, @Body todoPatchRequest: TodoPatchRequest): Call<TodoPatchResponse>
}