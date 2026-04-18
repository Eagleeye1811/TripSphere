package com.tripsphere.data.remote.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class FirebaseAuthSource @Inject constructor() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val currentUser: FirebaseUser? get() = auth.currentUser

    suspend fun signIn(email: String, password: String): FirebaseUser =
        suspendCancellableCoroutine { cont ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    val user = result.user
                    if (user != null) cont.resume(user)
                    else cont.resumeWithException(Exception("Authentication failed: no user returned"))
                }
                .addOnFailureListener { e -> cont.resumeWithException(e) }
        }

    suspend fun createUser(email: String, password: String, displayName: String): FirebaseUser =
        suspendCancellableCoroutine { cont ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    val user = result.user
                    if (user != null) {
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(displayName)
                            .build()
                        user.updateProfile(profileUpdates)
                            .addOnSuccessListener { cont.resume(user) }
                            .addOnFailureListener { cont.resume(user) }
                    } else {
                        cont.resumeWithException(Exception("Registration failed: no user returned"))
                    }
                }
                .addOnFailureListener { e -> cont.resumeWithException(e) }
        }

    /** Authenticate with Firebase using an ID token obtained from GoogleSignIn. */
    suspend fun signInWithGoogle(idToken: String): FirebaseUser =
        suspendCancellableCoroutine { cont ->
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            auth.signInWithCredential(credential)
                .addOnSuccessListener { result ->
                    val user = result.user
                    if (user != null) cont.resume(user)
                    else cont.resumeWithException(Exception("Google sign-in failed: no user returned"))
                }
                .addOnFailureListener { e -> cont.resumeWithException(e) }
        }

    fun signOut() {
        auth.signOut()
    }

    fun isLoggedIn(): Boolean = auth.currentUser != null
}
