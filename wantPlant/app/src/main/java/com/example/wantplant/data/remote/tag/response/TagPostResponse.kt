package com.example.wantplant.data.remote.tag.response

import java.time.LocalTime

data class TagPostResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result : TagPostResult
)
data class TagPostResult (
    val id: Long,
    var tagTime: String,
    var date: String,
    var tagColor: TagColor,
    var tagName: String
)
