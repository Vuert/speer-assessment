package com.speer.technologies.datasource.user.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.speer.technologies.datasource.user.local.model.UserDbo

@Dao
interface UserDao {

    @Query("SELECT * FROM users WHERE user_name = :username")
    suspend fun getByUsername(username: String): UserDbo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: UserDbo)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteById(id: String)
}
