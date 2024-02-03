package com.example.wantplant.data.remote.tag.response

import java.time.LocalTime

data class TagPatchResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result : TagPatchResult
)

data class TagPatchResult (
    val id: Long,
    var tagTime: String,
    var date: String,
    var tagColor: TagColor,
    var tagName: String
)
