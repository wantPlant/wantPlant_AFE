package com.example.wantplant.data.remote.todo

import com.example.wantplant.data.remote.todo.request.TodoPatchCompleteRequest
import com.example.wantplant.data.remote.todo.request.TodoPatchRequest
import com.example.wantplant.data.remote.todo.request.TodoPostRequest
import com.example.wantplant.data.remote.todo.response.TodoDeleteResponse
import com.example.wantplant.data.remote.todo.response.TodoPatchCompleteResponse
import com.example.wantplant.data.remote.todo.response.TodoPatchResponse
import com.example.wantplant.data.remote.todo.response.TodoPostResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface TodoRetrofitInterfaces {
    @POST("/api/todos")
    fun postTodo(@Header("Authorization") token: String?, @Body todoPostRequest: TodoPostRequest): Call<TodoPostResponse>

    @DELETE("/api/todos/{todoId}")
    fun deleteTodo(@Header("Authorization") token: String?, @Path(value = "todoId") todoId: Long): Call<TodoDeleteResponse>

    @PATCH("/api/todos/{todoId}")
    fun patchTodo(@Header("Authorization") token: String?, @Path(value = "todoId") todoId: Long, @Body todoPatchRequest: TodoPatchRequest): Call<TodoPatchResponse>

    @PATCH("/api/todos/{todoId}/complete")
    fun patchTodoComplete(@Header("Authorization") token: String?,@Path(value = "todoId") todoId: Long, @Body todoPatchCompleteRequest: TodoPatchCompleteRequest): Call<TodoPatchCompleteResponse>
}