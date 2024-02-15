package com.example.wantplant.data.remote.pot.response

data class PotPostResponse(
    val isSuccess: Boolean,
    var code: String,
    var message: String,
    var result: String
)
