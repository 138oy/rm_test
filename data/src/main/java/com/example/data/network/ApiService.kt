package com.example.data.network

import com.example.domain.models.CamerasResponseModel
import com.example.domain.models.DoorsResponseModel
import retrofit2.http.GET

interface ApiService {
    @GET("cameras")
    suspend fun getCamerasInfo(): CamerasResponseModel

    @GET("doors")
    suspend fun getDoorsInfo(): DoorsResponseModel

}
