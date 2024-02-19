package com.example.wantplant.data.remote.garden.response

data class GardenPutResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result : GardenPutList
)

data class GardenPutList(
    val gardenId: Long,
    var name: String,
    var description: String,
    var gardenCategory: String
)
