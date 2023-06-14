package com.speer.technologies.domain.user.repository

import com.speer.technologies.domain.user.model.User

interface UserRepository {

    suspend fun getUserByUserName(username: String): User?
}
