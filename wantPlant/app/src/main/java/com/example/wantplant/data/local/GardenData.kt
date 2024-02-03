package com.example.wantplant.data.local

data class GardenData(
    val gardenId: Int? = null,
    val name: String,
    val description: String,
    val category: String
)

data class GardenResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: ResultData
)

data class ResultData(
    val gardenList: List<GardenData>,
    val listSize: Int,
    val totalPage: Int,
    val totalElements: Int,
    val isFirst: Boolean,
    val isLast: Boolean
)
