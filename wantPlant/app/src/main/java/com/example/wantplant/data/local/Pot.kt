package com.example.wantplant.data.local

data class Pot(
    val potId: Int,
    val potName: String,
    val potTagColor: String,
    val proceed: Int,
    val potImageUrl: String,
    val startAt: String
)

data class PotsResult(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result
) {
    data class Result(
        val pots: List<Pot>,
        val listSize: Int,
        val totalPage: Int,
        val totalElements: Int,
        val isFirst: Boolean,
        val isLast: Boolean
    )
}

