package com.kazedev.wher_sbro.features.auth.domain.usecases

import com.kazedev.wher_sbro.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, email: String, pass: String): Result<Boolean> {
        return repository.register(username, email, pass)
    }
}