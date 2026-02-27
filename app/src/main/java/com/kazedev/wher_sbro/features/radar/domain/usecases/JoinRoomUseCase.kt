package com.kazedev.wher_sbro.features.radar.domain.usecases

import javax.inject.Inject
import com.kazedev.wher_sbro.features.radar.domain.entities.Room
import com.kazedev.wher_sbro.features.radar.domain.repositories.RoomRepository

class JoinRoomUseCase @Inject constructor(
    private val repository: RoomRepository
){
    suspend operator fun invoke(code : String, userId : Int, username : String) {
        repository.joinRoom(code, userId, username)
    }
}