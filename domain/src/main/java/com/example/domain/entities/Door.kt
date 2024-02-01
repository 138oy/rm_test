package com.example.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "doors")
data class Door(
    @PrimaryKey
    val doorId: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "snapshot")
    val snapshot: String?,
    @ColumnInfo(name = "room")
    val room: String?,
    @ColumnInfo(name = "favorites")
    val isFavorites: Boolean,
)
