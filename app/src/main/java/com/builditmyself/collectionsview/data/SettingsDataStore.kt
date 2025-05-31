package com.builditmyself.collectionsview.data

import android.content.Context
import android.net.ipsec.ike.TunnelModeChildSessionParams.ConfigRequestIpv4Netmask
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val APP_PREFERENCES_NAME = "user_app_settings"

// Create a DataStore instance using the preferencesDataStore delegate, with the Context as
// receiver.
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = APP_PREFERENCES_NAME
)


class SettingsDataStore(context: Context) {

    // Define the keys used to store the values
    private val USERNAME_LABEL = stringPreferencesKey("username")
    private val PASSWORD_LABEL = stringPreferencesKey("password")
    private val MONGO_CLUSTER_NAME = stringPreferencesKey("mongo_cluster")
    private val MONGO_URI_NAME = stringPreferencesKey("mongo_uri")
    private val MONGO_DATABASE_NAME = stringPreferencesKey("mongo_database")

    // Define the Flow method to expose changes to specific keys, with initial defaults of ""
    val usernameFlow: Flow<String> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences -> preferences[USERNAME_LABEL] ?: "" }

    val passwordFlow: Flow<String> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences -> preferences[PASSWORD_LABEL] ?: "" }

    val clusterFlow: Flow<String> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences -> preferences[MONGO_CLUSTER_NAME] ?: "" }

    val uriFlow: Flow<String> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences -> preferences[MONGO_URI_NAME] ?: "" }

    val databaseFlow: Flow<String> = context.dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences -> preferences[MONGO_DATABASE_NAME] ?: "" }

    // Define the save methods for each of these keys
    suspend fun saveUsernameToDataStore(usernameString: String, context: Context) {
        context.dataStore.edit { preferences -> preferences[USERNAME_LABEL] = usernameString }
    }

    suspend fun savePasswordToDataStore(passwordString: String, context: Context) {
        context.dataStore.edit { preferences -> preferences[PASSWORD_LABEL] = passwordString}
    }

    suspend fun saveClusterToDataStore(clusterString: String, context: Context) {
        context.dataStore.edit { preferences -> preferences[MONGO_CLUSTER_NAME] = clusterString }
    }

    suspend fun saveUriToDataStore(uriString: String, context: Context) {
        context.dataStore.edit { preferences -> preferences[MONGO_URI_NAME] = uriString }
    }

    suspend fun saveDatabaseToDataStore(databaseString: String, context: Context) {
        context.dataStore.edit { preferences -> preferences[MONGO_DATABASE_NAME] = databaseString}
    }

    suspend fun saveCredentials(
        username: String,
        password: String,
        cluster: String,
        uri: String,
        database: String,
        context: Context
    ) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME_LABEL] = username
            preferences[PASSWORD_LABEL] = password
            preferences[MONGO_CLUSTER_NAME] = cluster
            preferences[MONGO_URI_NAME] = uri
            preferences[MONGO_DATABASE_NAME] = database
        }
    }

    suspend fun clearCredentials(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(USERNAME_LABEL)
            preferences.remove(PASSWORD_LABEL)
            preferences.remove(MONGO_CLUSTER_NAME)
            preferences.remove(MONGO_URI_NAME)
            preferences.remove(MONGO_DATABASE_NAME)
        }
    }
}