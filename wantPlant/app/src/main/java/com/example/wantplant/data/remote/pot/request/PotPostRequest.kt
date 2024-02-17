package com.example.wantplant.data.remote.pot.request

data class PotPostRequest(
    val gardenId: Long,
    var potName: String,
    var startAt: String,
    var goalList: ArrayList<Goal>
)

data class Todo(
    var todoTitle: String,
    var date: String,
    var time: String
)

data class Goal(
    var goalTitle: String,
    var todoList: ArrayList<Todo>
)
