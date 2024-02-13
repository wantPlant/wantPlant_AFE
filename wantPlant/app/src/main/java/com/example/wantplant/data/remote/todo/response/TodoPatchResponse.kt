package com.example.wantplant.data.remote.todo.response

data class TodoPatchResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result : String
)
