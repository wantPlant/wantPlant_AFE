package com.example.wantplant.data.remote.goal.response

data class GoalTodoGetResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result : GoalResult
)

data class GoalResult(
    var goals: List<GoalList>
)

data class GoalList(
    val goalId: Long,
    var goalTitle: String,
    var todos: List<GoalTodoList>
)

data class GoalTodoList(
    val todoId: Long,
    var todoTitle: String,
    var date: String,
    var time: String,
    var isComplete: Boolean
)