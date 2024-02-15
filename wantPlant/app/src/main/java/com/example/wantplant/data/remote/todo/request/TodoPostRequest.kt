package com.example.wantplant.data.remote.todo.request

data class TodoPostRequest(
    val goalID: Long,
    var title: String,
    var date: String,
    var time: String
)
