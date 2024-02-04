package com.example.wantplant.data.local

data class LoginResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result
) {
    data class Result(
        val accessToken: String,
        val refreshToken: String
    )
}

