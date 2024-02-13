package com.example.wantplant.data.remote.todo.request

data class TodoPostRequest(
    val goalId: Long,
    var title: String,
    var date: String,
    var time: String
)
