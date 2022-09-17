package com.builditmyself.collectionsview.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Connection(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "user_name")
    val userName: String,
    @ColumnInfo(name = "user_pwd")
    val userPwd: String,
    @ColumnInfo(name = "cluster")
    val cluster: String,
    @ColumnInfo(name = "uri_string")
    val uriString: String,
    @ColumnInfo(name = "database")
    val database: String
)