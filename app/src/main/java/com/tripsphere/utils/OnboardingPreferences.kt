package com.tripsphere.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.onboardingDataStore: DataStore<Preferences> by preferencesDataStore(name = "onboarding_prefs")

@Singleton
class OnboardingPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val onboardingCompletedKey = booleanPreferencesKey("onboarding_completed")
    private val isLoggedInKey = booleanPreferencesKey("is_logged_in")

    val isOnboardingCompleted: Flow<Boolean> = context.onboardingDataStore.data
        .map { prefs -> prefs[onboardingCompletedKey] ?: false }

    val isLoggedIn: Flow<Boolean> = context.onboardingDataStore.data
        .map { prefs -> prefs[isLoggedInKey] ?: false }

    suspend fun setOnboardingCompleted() {
        context.onboardingDataStore.edit { prefs ->
            prefs[onboardingCompletedKey] = true
        }
    }

    suspend fun setLoggedIn(value: Boolean) {
        context.onboardingDataStore.edit { prefs ->
            prefs[isLoggedInKey] = value
        }
    }
}
