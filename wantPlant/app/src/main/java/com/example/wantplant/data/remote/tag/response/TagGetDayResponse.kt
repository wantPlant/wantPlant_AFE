package com.example.wantplant.data.remote.tag.response

import java.time.LocalTime

data class TagGetDayResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result : TagDayGetDTO
)

data class TagDayGetDTO (
    val tagGetDayResult : List<TagDayGetResult>
)

data class TagDayGetResult(
    val id: Long,
    var tagTime: LocalTime,
    var date: String,
    var tagColor: String,
    var tagName: String
)