package com.example.wantplant.data.remote.garden.request

data class GardenPutRequest(
    val gardenId: Long,
    var name: String,
    var description: String
)
