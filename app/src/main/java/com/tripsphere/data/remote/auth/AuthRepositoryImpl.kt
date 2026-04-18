package com.tripsphere.data.remote.auth

import com.tripsphere.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuthSource: FirebaseAuthSource
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): Result<String> {
        return try {
            val user = firebaseAuthSource.signIn(email, password)
            Result.success(user.displayName ?: user.email ?: "Traveler")
        } catch (e: Exception) {
            Result.failure(mapFirebaseError(e))
        }
    }

    override suspend fun signUp(name: String, email: String, password: String): Result<String> {
        return try {
            val user = firebaseAuthSource.createUser(email, password, name)
            Result.success(user.displayName ?: name)
        } catch (e: Exception) {
            Result.failure(mapFirebaseError(e))
        }
    }

    override suspend fun signInWithGoogle(idToken: String): Result<String> {
        return try {
            val user = firebaseAuthSource.signInWithGoogle(idToken)
            Result.success(user.displayName ?: user.email ?: "Traveler")
        } catch (e: Exception) {
            Result.failure(mapFirebaseError(e))
        }
    }

    override fun signOut() = firebaseAuthSource.signOut()

    override fun isLoggedIn(): Boolean = firebaseAuthSource.isLoggedIn()

    override fun getCurrentUserEmail(): String? = firebaseAuthSource.currentUser?.email

    override fun getCurrentUserName(): String? =
        firebaseAuthSource.currentUser?.displayName?.takeIf { it.isNotBlank() }
            ?: firebaseAuthSource.currentUser?.email?.substringBefore("@")

    private fun mapFirebaseError(e: Exception): Exception {
        val msg = e.message ?: "Authentication error"
        return when {
            "INVALID_EMAIL" in msg || "badly formatted" in msg.lowercase() ->
                Exception("Invalid email address")
            "WRONG_PASSWORD" in msg || "password is invalid" in msg.lowercase() ->
                Exception("Incorrect password")
            "USER_NOT_FOUND" in msg || "no user record" in msg.lowercase() ->
                Exception("No account found with this email")
            "EMAIL_ALREADY_IN_USE" in msg || "already in use" in msg.lowercase() ->
                Exception("An account already exists with this email")
            "WEAK_PASSWORD" in msg || "weak password" in msg.lowercase() ->
                Exception("Password must be at least 6 characters")
            "NETWORK_ERROR" in msg || "network" in msg.lowercase() ->
                Exception("Network error — check your connection")
            else -> Exception(msg)
        }
    }
}
