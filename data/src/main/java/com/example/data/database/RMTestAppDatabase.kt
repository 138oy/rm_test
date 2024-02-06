package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.database.dao.CameraDao
import com.example.data.database.dao.DoorDao
import com.example.domain.entities.Camera
import com.example.domain.entities.Door

@Database(entities = [Camera::class, Door::class], version = 1)
abstract class RMTestAppDatabase : RoomDatabase() {

    abstract fun cameraDao(): CameraDao

    abstract fun doorDao(): DoorDao
}
