package com.example.wantplant.data.remote.goal.response

data class GoalPatchResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result : String
)
