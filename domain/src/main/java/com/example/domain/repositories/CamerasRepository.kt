package com.example.domain.repositories

import com.example.common.ResponseWrapper
import com.example.domain.entities.Camera
import com.example.domain.models.CameraModel
import kotlinx.coroutines.flow.Flow

interface CamerasRepository {

    fun getCameras(): Flow<ResponseWrapper<List<CameraModel>>>

    fun getCamerasCached(): Flow<List<Camera>>

    fun updateCamerasCached(data: List<CameraModel>)

    fun cacheCameras(data: List<CameraModel>)

    fun updateFavorite(id: Int)
}
