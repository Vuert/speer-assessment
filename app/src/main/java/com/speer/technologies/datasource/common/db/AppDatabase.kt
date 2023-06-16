package com.speer.technologies.datasource.common.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.speer.technologies.datasource.user.local.dao.UserDao
import com.speer.technologies.datasource.user.local.model.UserDbo

@Database(entities = [UserDbo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        const val NAME = "App database"
    }
}
