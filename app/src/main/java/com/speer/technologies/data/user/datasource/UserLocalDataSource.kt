package com.speer.technologies.data.user.datasource

import com.speer.technologies.domain.user.model.User

interface UserLocalDataSource {

    suspend fun getUserByUserName(username: String): User?

    suspend fun addOrUpdateUser(user: User)

    suspend fun removeUser(user: User)
}
