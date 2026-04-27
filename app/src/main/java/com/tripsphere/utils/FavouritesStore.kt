package com.tripsphere.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.favouritesDataStore: DataStore<Preferences>
        by preferencesDataStore(name = "favourites_prefs")

@Singleton
class FavouritesStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val FAV_IDS_KEY = stringSetPreferencesKey("favourite_destination_ids")

    val favouriteIds: Flow<Set<Int>> = context.favouritesDataStore.data
        .map { prefs ->
            prefs[FAV_IDS_KEY]?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
        }

    suspend fun toggle(id: Int) {
        context.favouritesDataStore.edit { prefs ->
            val current = prefs[FAV_IDS_KEY] ?: emptySet()
            val idStr = id.toString()
            prefs[FAV_IDS_KEY] = if (idStr in current) current - idStr else current + idStr
        }
    }
}
