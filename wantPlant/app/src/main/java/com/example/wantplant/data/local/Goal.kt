package com.example.wantplant.data.local

data class Goal(
    val goalId: Long,
    var goalTitle: String,
    var todoList: List<TodoResult>
)

/*data class Todo(
    val todoTitle: String,
    val date: String,
    val time: String
)*/

data class GoalResult(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result
) {
    data class Result(
        val goalList: List<Goal>
    )
}