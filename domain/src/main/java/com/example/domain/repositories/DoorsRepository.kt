package com.example.domain.repositories

import com.example.common.ResponseWrapper
import com.example.domain.entities.Door
import com.example.domain.models.DoorModel
import kotlinx.coroutines.flow.Flow

interface DoorsRepository {

    fun getDoors(): Flow<ResponseWrapper<List<DoorModel>>>

    fun getDoorsCached(): Flow<List<Door>>

    fun updateDoorsCached(data: List<DoorModel>)

    fun cacheDoors(data: List<DoorModel>)

    fun updateFavorite(id: Int)
}
