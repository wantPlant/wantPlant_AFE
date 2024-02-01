package com.example.wantplant.data.remote.tag.request

import com.example.wantplant.data.remote.tag.response.TagColor

data class TagPatchRequest(
    val tagId: Long,
    var tagColor: TagColor,
    var tagName: String,
    var tagTime: String,
    var date: String
)
