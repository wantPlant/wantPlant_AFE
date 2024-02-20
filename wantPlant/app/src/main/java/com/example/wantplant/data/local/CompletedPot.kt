package com.example.wantplant.data.local

data class CompletedPot(
    val potName: String,
    val potImageUrl: String,
    val startAt: String,
    val completeAt: String
)

data class CompletedPotsResult(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: CompletedResult
) {
    data class CompletedResult(
        val pots: List<CompletedPot>
    )
}
