package com.heart256tv

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

// Creates a DataStore file called "user_prefs"
val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferencesRepository(private val context: Context) {
    private val FAVORITES_KEY = stringSetPreferencesKey("favorites")

    // Reads the favorites from storage
    val favoriteIds = context.dataStore.data.map { preferences ->
        preferences[FAVORITES_KEY]?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
    }

    // Saves the favorites to storage
    suspend fun saveFavorites(ids: Set<Int>) {
        context.dataStore.edit { preferences ->
            preferences[FAVORITES_KEY] = ids.map { it.toString() }.toSet()
        }
    }
}
