package com.builditmyself.collectionsview.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chaquo.python.PyObject
import com.chaquo.python.Python

class MongoDataViewModel: ViewModel() {
    // DEFINE THE VARIABLES HERE ===============================================================

    // Python interface variable(s)
    private val _pythonInstance = MutableLiveData<Python>()
    val pythonInstance: LiveData<Python> = _pythonInstance

    private val _mongoInterface = MutableLiveData<PyObject>()
    val mongoInterface: LiveData<PyObject> = _mongoInterface

    // Collection fragment variable(s)
    private val _collectionSelection = MutableLiveData("")
    val collectionSelection: LiveData<String> = _collectionSelection

    // DEFINE THE FUNCTIONS HERE ==============================================================
    fun setPythonInstance(python: Python) {
        _pythonInstance.value = python
    }
    fun setMongoInterface(mongoInterfaceInput: PyObject) {
        _mongoInterface.value = mongoInterfaceInput
    }

    fun setCollection(choice: String) {
        _collectionSelection.value = choice
    }

    //TODO: Implement this ViewModel
}