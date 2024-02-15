package com.example.wantplant.data.remote.goal.response

data class GoalPostResponse(
    val isSuccess: Boolean,
    var code: String,
    var message: String,
    var result: String
)
