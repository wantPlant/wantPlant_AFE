package com.example.wantplant.data.remote.todo.response

data class TodoPostResponse(
    val isSuccess: Boolean,
    var code: String,
    var message: String,
    var result: String
)
