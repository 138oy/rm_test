package com.example.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CamerasList(
    @Json(name = "cameras")
    val cameras: List<CameraModel>
)

@JsonClass(generateAdapter = true)
data class CameraModel(
    @Json(name = "name")
    val name: String,
    @Json(name = "snapshot")
    val snapshot: String,
    @Json(name = "room")
    val room: String?,
    @Json(name = "id")
    val cameraId: Int,
    @Json(name = "favorites")
    val isFavorites: Boolean,
    @Json(name = "rec")
    val isRec: Boolean,
)
