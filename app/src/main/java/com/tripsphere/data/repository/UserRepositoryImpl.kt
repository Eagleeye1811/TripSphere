package com.tripsphere.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tripsphere.data.local.dao.UserDao
import com.tripsphere.data.local.entity.UserEntity
import com.tripsphere.domain.model.User
import com.tripsphere.domain.repository.UserRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserRepositoryImpl @Inject constructor(
    private val dao: UserDao,
    @ApplicationContext private val context: Context
) : UserRepository {

    private val currentUserIdKey = longPreferencesKey("current_user_id")

    override suspend fun registerUser(user: User): Long {
        val entity = UserEntity(name = user.name, email = user.email, password = user.password)
        return dao.insertUser(entity)
    }

    override suspend fun loginUser(email: String, password: String): User? {
        return dao.loginUser(email, password)?.let {
            User(id = it.id, name = it.name, email = it.email)
        }
    }

    override suspend fun getCurrentUser(): User? {
        val prefs = context.dataStore.data.first()
        val userId = prefs[currentUserIdKey] ?: return dao.getFirstUser()?.let {
            User(id = it.id, name = it.name, email = it.email)
        }
        return dao.getUserById(userId)?.let {
            User(id = it.id, name = it.name, email = it.email)
        }
    }

    override suspend fun saveCurrentUserId(id: Long) {
        context.dataStore.edit { prefs ->
            prefs[currentUserIdKey] = id
        }
    }
}
