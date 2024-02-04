package com.example.data.repositories

import com.example.common.ResponseWrapper
import com.example.data.database.dao.DoorDao
import com.example.data.network.ApiService
import com.example.data.network.safeApiCall
import com.example.domain.doorDtoToEntity
import com.example.domain.entities.Door
import com.example.domain.models.DoorModel
import com.example.domain.repositories.DoorsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DoorsRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dao: DoorDao
) : DoorsRepository {
    override fun getDoors(): Flow<ResponseWrapper<List<DoorModel>>> {
        val data: Flow<ResponseWrapper<List<DoorModel>>> = flow {
            emit(
                safeApiCall(Dispatchers.IO) { apiService.getDoorsInfo().data }
            )
        }
        return data
    }

    override fun getDoorsCached(): Flow<List<Door>> {
        val data: Flow<List<Door>> = flow {
            emit(dao.getAllDoors())
        }
        return data
    }

    override fun updateDoorsCached(data: List<DoorModel>) {
        CoroutineScope(Dispatchers.IO).launch {
            data.forEach { model ->
                dao.updateDoor(model.doorId, model.snapshot)
            }
        }
    }

    override fun cacheDoors(data: List<DoorModel>) {
        CoroutineScope(Dispatchers.IO).launch {
            val entities: MutableList<Door> = mutableListOf()
            data.forEach { model ->
                entities.add(doorDtoToEntity(model))
            }

            dao.insertAllDoors(entities)
        }
    }

    override fun updateFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.updateFavorite(id)
        }
    }

    override fun updateName(id: Int, name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.updateName(id, name)
        }
    }
}
