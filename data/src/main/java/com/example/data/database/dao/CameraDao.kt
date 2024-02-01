package com.example.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.entities.Camera

@Dao
interface CameraDao {
    @Query("SELECT * FROM cameras")
    suspend fun getAllCameras(): List<Camera>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllCameras(data: List<Camera>)

    @Query("UPDATE cameras SET favorites = NOT(favorites) WHERE cameraId = :id")
    suspend fun updateFavorite(id: Int)

    @Query("UPDATE cameras SET name = :name, snapshot = :snapshot, rec = :isRec WHERE cameraId = :id")
    suspend fun updateCamera(id: Int, name: String, snapshot: String, isRec: Boolean)
}