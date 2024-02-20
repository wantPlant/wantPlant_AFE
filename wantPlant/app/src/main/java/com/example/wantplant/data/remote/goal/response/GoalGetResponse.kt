package com.example.wantplant.data.remote.goal.response

data class GoalGetResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result : GoalGetList
)

data class GoalGetList(
    val goalList: List<GoalGetResult>
)

data class GoalGetResult(
    val goalId: Long,
    var goalTitle: String,
    val todoList: List<TodoGetList>
)

data class TodoGetList(
    val todoId: Long,
    var todoTitle: String,
    var date: String,
    var time: String,
    var isComplete: Boolean
)