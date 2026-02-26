package com.kazedev.wher_sbro.core.di

import com.kazedev.wher_sbro.features.auth.data.repositories.AuthRepositoryImpl
import com.kazedev.wher_sbro.features.auth.domain.repositories.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}