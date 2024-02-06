package com.example.domain.interactors

import com.example.common.ResponseWrapper
import com.example.common.doNothing
import com.example.domain.entities.Camera
import com.example.domain.models.CameraModel
import com.example.domain.repositories.CamerasRepository
import javax.inject.Inject

class CamerasInteractor @Inject constructor(
    private val repository: CamerasRepository
) {
    suspend fun getCameras(): List<Camera> {

        try {
            val cachedData: MutableList<Camera> = mutableListOf()
            repository.getCamerasCached().collect { list ->
                list.forEach { elem ->
                    cachedData.add(elem)
                }
            }

            val apiResponse: MutableList<CameraModel> = mutableListOf()
            repository.getCameras().collect { wrapper ->
                when (wrapper) {
                    is ResponseWrapper.Success -> wrapper.value.forEach { elem ->
                        apiResponse.add(elem)
                    }

                    is ResponseWrapper.Error -> doNothing()
                }
            }

            if (cachedData.isNotEmpty()) {
                repository.updateCamerasCached(apiResponse)
            }

            repository.cacheCameras(apiResponse)

            cachedData.clear()
            repository.getCamerasCached().collect { list ->
                list.forEach { elem ->
                    cachedData.add(elem)
                }
            }

            return cachedData

        } catch (t: Throwable) {
            throw t
        }
    }

    fun updateFavorite(id: Int) {
        repository.updateFavorite(id)
    }
}
