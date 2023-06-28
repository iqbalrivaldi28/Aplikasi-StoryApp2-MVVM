package com.example.intermediateiqbal

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    fun getUser(): Flow<User> {
        return dataStore.data.map {
            User(
                it[NAME_KEY] ?: "",
                it[TOKEN_KEY] ?: "",
                it[STATE_KEY] ?: false
            )
        }
    }

    suspend fun saveUser(user: User) {
        dataStore.edit {
            it[NAME_KEY] = user.name
            it[TOKEN_KEY] = user.token
            it[STATE_KEY] = user.isLogin
        }
    }

    suspend fun logout() {
        dataStore.edit {
            it[NAME_KEY] = ""
            it[TOKEN_KEY] = ""
            it[STATE_KEY] = false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}