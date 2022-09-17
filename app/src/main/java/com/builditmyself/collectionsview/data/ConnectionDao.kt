package com.builditmyself.collectionsview.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ConnectionDao {
    // Generic queries to define for the application
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(connection: Connection)
    @Update
    suspend fun update(connection: Connection)
    @Delete
    suspend fun delete(connection: Connection)

    // Customized queries specific to this application
    @Query("SELECT COUNT(*) FROM connection WHERE user_name = :userName")
    fun checkUserPresence(userName: String): LiveData<Int>

    @Query("SELECT * FROM connection WHERE user_name = :userName " +
            "AND user_pwd = :userPwd ORDER BY id ASC")
    fun getUserDetails(userName: String, userPwd: String): Flow<List<Connection>>
}