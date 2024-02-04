package com.example.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.entities.Door

@Dao
interface DoorDao {
    @Query("SELECT * FROM doors")
    suspend fun getAllDoors(): List<Door>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllDoors(data: List<Door>)

    @Query("UPDATE doors SET favorites = NOT(favorites) WHERE doorId = :id")
    suspend fun updateFavorite(id: Int)

    @Query("UPDATE doors SET name = :name WHERE doorId = :id")
    suspend fun updateName(id: Int, name: String)

    @Query("UPDATE doors SET snapshot = :snapshot WHERE doorId = :id")
    suspend fun updateDoor(id: Int, snapshot: String?)
}