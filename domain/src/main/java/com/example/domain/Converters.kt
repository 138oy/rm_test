package com.example.domain

import com.example.domain.entities.Camera
import com.example.domain.entities.Door
import com.example.domain.models.CameraModel
import com.example.domain.models.DoorModel

fun cameraDtoToEntity(dto: CameraModel): Camera {
    return Camera(
        cameraId = dto.cameraId,
        name = dto.name,
        snapshot = dto.snapshot,
        room = dto.room,
        isFavorites = dto.isFavorites,
        isRec = dto.isRec
    )
}

fun doorDtoToEntity(dto: DoorModel): Door {
    return Door(
        doorId = dto.doorId,
        name = dto.name,
        snapshot = dto.snapshot,
        room = dto.room,
        isFavorites = dto.isFavorites
    )
}
