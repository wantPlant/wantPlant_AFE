package com.example.wantplant.data.remote.tag.response

data class TagGetMonthResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result : TagMonthGetDTO
)

data class TagMonthGetDTO (
    val tagResponseDtos : List<TagMonthGetResult>
)

data class TagMonthGetResult(
    val id: Long,
    var tagTime: String,
    var date: String,
    var tagColor: TagColor,
    var tagName: String
)