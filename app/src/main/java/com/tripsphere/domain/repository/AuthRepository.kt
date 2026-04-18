package com.tripsphere.domain.repository

interface AuthRepository {
    suspend fun signIn(email: String, password: String): Result<String>
    suspend fun signUp(name: String, email: String, password: String): Result<String>
    suspend fun signInWithGoogle(idToken: String): Result<String>
    fun signOut()
    fun isLoggedIn(): Boolean
    fun getCurrentUserEmail(): String?
    fun getCurrentUserName(): String?
}
