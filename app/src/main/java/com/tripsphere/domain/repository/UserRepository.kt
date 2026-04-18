package com.tripsphere.domain.repository

import com.tripsphere.domain.model.User

interface UserRepository {
    suspend fun registerUser(user: User): Long
    suspend fun loginUser(email: String, password: String): User?
    suspend fun getCurrentUser(): User?
    suspend fun saveCurrentUserId(id: Long)
}
