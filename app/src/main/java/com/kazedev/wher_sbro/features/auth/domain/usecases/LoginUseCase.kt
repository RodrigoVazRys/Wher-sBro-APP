package com.kazedev.wher_sbro.features.auth.domain.usecases

import com.kazedev.wher_sbro.features.auth.domain.entities.User
import com.kazedev.wher_sbro.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, pass: String): Result<User> {
        return repository.login(email, pass)
    }
}