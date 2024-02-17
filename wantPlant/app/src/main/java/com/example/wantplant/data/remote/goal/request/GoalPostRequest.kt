package com.example.wantplant.data.remote.goal.request

data class GoalPostRequest(
    val potId: Long,
    var goalTitle: String,
    var todo: TodoList
)

data class TodoList(
    var todoTitle: String,
    var date: String,
    var time: String
)