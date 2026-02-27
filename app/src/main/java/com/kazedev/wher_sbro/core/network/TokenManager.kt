package com.kazedev.wher_sbro.core.network

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
        private val USER_ID_KEY = intPreferencesKey("user_id")
        private val USERNAME_KEY = stringPreferencesKey("username")
    }

    // --- GUARDAR DATOS ---
    suspend fun saveSession(token: String, userId: Int, username: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
            prefs[USER_ID_KEY] = userId
            prefs[USERNAME_KEY] = username
        }
    }

    val tokenFlow: Flow<String?> = context.dataStore.data.map { it[TOKEN_KEY] }
    val usernameFlow: Flow<String?> = context.dataStore.data.map { it[USERNAME_KEY] }
    val userIdFlow: Flow<Int?> = context.dataStore.data.map { it[USER_ID_KEY] }

    suspend fun getToken(): String? {
        val prefs = context.dataStore.data.first()
        return prefs[TOKEN_KEY]
    }

    suspend fun clearSession() {
        context.dataStore.edit { it.clear() }
    }
}