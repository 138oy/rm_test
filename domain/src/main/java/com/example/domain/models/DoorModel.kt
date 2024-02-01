package com.example.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DoorModel(
    @Json(name = "name")
    val name: String,
    @Json(name = "snapshot")
    val snapshot: String?,
    @Json(name = "room")
    val room: String?,
    @Json(name = "id")
    val doorId: Int,
    @Json(name = "favorites")
    val isFavorites: Boolean,
)
