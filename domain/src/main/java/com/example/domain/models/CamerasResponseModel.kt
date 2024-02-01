package com.example.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CamerasResponseModel(
    @Json(name = "data")
    val data: CamerasList,
)
