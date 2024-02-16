package com.example.wantplant.data.remote.pot.request

import com.example.wantplant.data.local.PotTagColor

data class PotsPostRequest(
    val gardenId: Long,
    var potName: String,
    var potTageColor: PotTagColor,
    var startAt: String
)
