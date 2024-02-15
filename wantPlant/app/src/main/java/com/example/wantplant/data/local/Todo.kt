package com.example.wantplant.data.local

data class TodoResponse(
    var isSuccess : Boolean,
    var code : String,
    var message : String,
    var result : TodoResult
)

data class TodoResult(
    val id : Int,
    var title : String,
    var date : String,
    var time : String,
    var isComplete : Boolean
)
