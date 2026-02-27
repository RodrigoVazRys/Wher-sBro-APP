package com.kazedev.wher_sbro.features.radar.domain.usecases

import javax.inject.Inject
import com.kazedev.wher_sbro.features.radar.domain.entities.Room
import com.kazedev.wher_sbro.features.radar.domain.repositories.RoomRepository

class LeaveRoomUseCase @Inject constructor(
    private val repository: RoomRepository
){
    suspend operator fun invoke(code : String){
        repository.leaveRoom(code)

    }
}