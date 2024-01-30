package com.example.wantplant.data.remote.tag.request

import com.example.wantplant.data.remote.tag.response.TagColor

data class TagPostRequest(
    val tagColor: TagColor,
    var tagName: String,
    var startDate: String
)
