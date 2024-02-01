package com.example.domain.interactors

import com.example.common.ResponseWrapper
import com.example.domain.entities.Door
import com.example.domain.models.DoorModel
import com.example.domain.repositories.DoorsRepository
import javax.inject.Inject

class DoorsInteractor @Inject constructor(
    private val repository: DoorsRepository
) {

    suspend fun getDoors(): List<Door> {

        try {
            val cachedData: MutableList<Door> = mutableListOf()
            repository.getDoorsCached().collect { list ->
                list.forEach { elem ->
                    cachedData.add(elem)
                }
            }

            val apiResponse: MutableList<DoorModel> = mutableListOf()
            repository.getDoors().collect { wrapper ->
                when (wrapper) {
                    is ResponseWrapper.Success -> wrapper.value.forEach { elem ->
                        apiResponse.add(elem)
                    }

                    is ResponseWrapper.Error -> throw Throwable(wrapper.code)
                }
            }

            if (cachedData.isNotEmpty()) {
                repository.updateDoorsCached(apiResponse)
            }

            repository.cacheDoors(apiResponse)

            cachedData.clear()
            repository.getDoorsCached().collect { list ->
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
