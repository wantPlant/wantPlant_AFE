package com.example.wantplant.data.remote.tag.response

data class TagPostResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result : TagResult
)
data class TagResult (
    val id: Long,
    var startDate: String,
    var tagColor: String,
    var tagName: String
)
