package com.example.wantplant.data.remote.tag.request

import com.example.wantplant.data.remote.tag.response.TagColor
import java.time.LocalTime

data class TagPostRequest(
    val tagColor: TagColor,
    var tagName: String,
    var tagTime: String,
    var date: String
)
