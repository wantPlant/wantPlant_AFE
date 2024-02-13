package com.example.wantplant.data.remote.garden.response

import com.example.wantplant.data.local.Category
import com.example.wantplant.data.local.PotTagColor
import com.example.wantplant.data.local.PotType

data class GardenGetResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result : GardenGetResult
)

data class GardenGetResult(
    val totalElements: Long,
    val gardens: List<GardenGetList>
)

data class GardenGetList(
    val gardenId: Long,
    var name: String,
    var description: String,
    var gardenCategory: String,
    val potList: List<PotList>
)

data class PotList(
    val potId: Long,
    var potName: String,
    var potType: PotType,
    var proceed: Int,
    var potTagColor: PotTagColor,
    var potImageUrl: String,
    var startAt: String,
    val garden: List<GardenList>
)

data class GardenList(
    val id: Long,
    var name: String,
    var description: String,
    var category: Category
)