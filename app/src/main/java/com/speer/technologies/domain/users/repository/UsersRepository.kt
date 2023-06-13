package com.speer.technologies.domain.users.repository

import com.speer.technologies.domain.users.model.User

interface UsersRepository {

    suspend fun findUsersByName(name: String): List<User>
}
