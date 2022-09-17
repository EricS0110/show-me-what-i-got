package com.builditmyself.collectionsview

import android.app.Application
import com.builditmyself.collectionsview.data.ConnectionRoomDatabase

class ConnectionApplication: Application() {
    val database: ConnectionRoomDatabase by lazy { ConnectionRoomDatabase.getDatabase(this) }
}