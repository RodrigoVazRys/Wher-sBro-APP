package com.kazedev.wher_sbro.features.radar.domain.repositories

import com.kazedev.wher_sbro.features.radar.domain.entities.Room

interface RoomRepository {
    suspend fun join( code : String )

    suspend fun leave( code : String )

    suspend fun getCode (): Result<Room>
}