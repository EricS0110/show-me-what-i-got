package com.builditmyself.collectionsview.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mongodb.client.MongoClient

class MongoDataViewModel: ViewModel() {
    // DEFINE THE VARIABLES HERE ===============================================================

    // MongoDB connection variable
    private val _mongoConnection = MutableLiveData<MongoClient>()
    val mongoConnection: LiveData<MongoClient> = _mongoConnection

    // Collection fragment variable(s)
    private val _collectionSelection = MutableLiveData("")
    val collectionSelection: LiveData<String> = _collectionSelection

    // Field fragment variable(s)
    private val _fieldSelection = MutableLiveData("")
    val fieldSelection: LiveData<String> = _fieldSelection

    private val _searchCriteria = MutableLiveData("")
    val searchCriteria: LiveData<String> = _searchCriteria

    // DEFINE THE FUNCTIONS HERE ==============================================================

    fun setConnection(connection: MongoClient) {
        // Set the MongoDB connection
        _mongoConnection.value = connection
    }

    fun setCollection(choice: String) {
        _collectionSelection.value = choice
    }

    fun setField(field: String) {
        _fieldSelection.value = field
    }

    fun setCriteria(criteria: String) {
        _searchCriteria.value = criteria
    }
}