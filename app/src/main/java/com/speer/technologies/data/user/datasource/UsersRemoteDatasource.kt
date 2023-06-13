package com.speer.technologies.data.user.datasource

import com.speer.technologies.domain.users.model.User

interface UsersRemoteDatasource {

    suspend fun findUsersByName(name: String): List<User>
}
