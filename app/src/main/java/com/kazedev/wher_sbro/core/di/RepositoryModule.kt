package com.kazedev.wher_sbro.core.di

import com.kazedev.wher_sbro.features.auth.data.repositories.AuthRepositoryImpl
import com.kazedev.wher_sbro.features.auth.domain.repositories.AuthRepository
import com.kazedev.wher_sbro.features.radar.data.repositories.RoomRepositoryImpl
import com.kazedev.wher_sbro.features.radar.domain.repositories.RoomRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindRoomRepository(
        roomRepositoryImpl: RoomRepositoryImpl
    ): RoomRepository
}