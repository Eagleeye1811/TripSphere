package com.tripsphere.domain.usecase

import com.tripsphere.domain.model.User
import com.tripsphere.domain.repository.UserRepository
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User): Long = repository.registerUser(user)
}

class LoginUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): User? =
        repository.loginUser(email, password)
}

class GetCurrentUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): User? = repository.getCurrentUser()
}
