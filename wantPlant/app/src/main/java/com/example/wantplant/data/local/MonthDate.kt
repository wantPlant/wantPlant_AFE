package com.example.wantplant.data.local

import com.example.wantplant.data.remote.tag.response.TagMonthGetResult
import java.time.LocalDate

data class MonthDate (
    var date: LocalDate?,
    var tag: List<TagMonthGetResult>?,
    var isSelected: Boolean = false
)
//data class TagData (
//    val id: Long,
//    var startDate: String,
//    var tagColor: String,
//    var tagName: String
//)