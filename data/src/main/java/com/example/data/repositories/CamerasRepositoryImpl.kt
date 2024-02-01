package com.example.data.repositories

import com.example.common.ResponseWrapper
import com.example.data.database.dao.CameraDao
import com.example.data.network.ApiService
import com.example.data.network.safeApiCall
import com.example.domain.cameraDtoToEntity
import com.example.domain.entities.Camera
import com.example.domain.models.CameraModel
import com.example.domain.repositories.CamerasRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class CamerasRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    val dao: CameraDao
) : CamerasRepository {
    override fun getCameras(): Flow<ResponseWrapper<List<CameraModel>>> {
        val data: Flow<ResponseWrapper<List<CameraModel>>> = flow {
            emit(
                safeApiCall(Dispatchers.IO) { apiService.getCamerasInfo().data.cameras }
            )
        }
        return data
    }

    override fun getCamerasCached(): Flow<List<Camera>> {
        val data: Flow<List<Camera>> = flow {
            emit(dao.getAllCameras())
        }
        return data
    }

    override fun updateCamerasCached(data: List<CameraModel>) {
        CoroutineScope(Dispatchers.IO).launch {
            data.forEach { model ->
                dao.updateCamera(model.cameraId, model.name, model.snapshot, model.isRec)
            }
        }
    }

    override fun cacheCameras(data: List<CameraModel>) {
        CoroutineScope(Dispatchers.IO).launch {
            val entities: MutableList<Camera> = mutableListOf()
            data.forEach { model ->
                entities.add(cameraDtoToEntity(model))
            }

            dao.insertAllCameras(entities)
        }
    }

    override fun updateFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.updateFavorite(id)
        }
    }
}
