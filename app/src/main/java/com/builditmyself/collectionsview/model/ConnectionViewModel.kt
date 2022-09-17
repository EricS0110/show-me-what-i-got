package com.builditmyself.collectionsview.model

import androidx.lifecycle.*
import com.builditmyself.collectionsview.data.Connection
import com.builditmyself.collectionsview.data.ConnectionDao
import kotlinx.coroutines.launch

class ConnectionViewModel(private val connectionDao: ConnectionDao) : ViewModel() {
    // Define variables used for short-term caching
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _userPwd = MutableLiveData<String>()
    val userPwd: LiveData<String> = _userPwd

    private val _cluster = MutableLiveData<String>()
    val cluster: LiveData<String> = _cluster

    private val _uriString = MutableLiveData<String>()
    val uriString: LiveData<String> = _uriString

    private val _database = MutableLiveData<String>()
    val database: LiveData<String> = _database

    // Define SET methods for these variables
    fun setUserName(user: String) {
        _userName.value = user
    }
    fun setUserPass(pwd: String) {
        _userPwd.value = pwd
    }
    fun setUserCluster(cluster: String) {
        _cluster.value = cluster
    }
    fun setUserUriString(uri: String) {
        _uriString.value = uri
    }
    fun setUserDatabase(database: String) {
        _database.value = database
    }

    // Input validation method(s)
    fun isEntryValid(userName: String, userPwd: String): Boolean {
        if (userName.isBlank() || userPwd.isBlank()) {
            return false
        }
        return true
    }

    // Define functions used for Room interactions
    private fun insertConnection(connection: Connection) {
        viewModelScope.launch { connectionDao.insert(connection) }
    }
    private fun getNewConnectionEntity(userName: String,
                                       userPwd: String,
                                       cluster: String,
                                       uriString: String,
                                       database: String): Connection {
        return Connection(
            userName = userName,
            userPwd = userPwd,
            cluster = cluster,
            uriString = uriString,
            database = database
        )
    }
    fun addNewConnection(userName: String, userPwd: String,
                         cluster: String, uriString: String, database: String) {
        val newConnection = getNewConnectionEntity(userName = userName, userPwd = userPwd,
            cluster = cluster, uriString = uriString, database = database)
        insertConnection(newConnection)
    }
}

class ConnectionViewModelFactory(private val connectionDao: ConnectionDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConnectionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConnectionViewModel(connectionDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}