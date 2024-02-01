package com.example.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DoorsResponseModel(
    @Json(name = "data")
    val data: List<DoorModel>,
)
