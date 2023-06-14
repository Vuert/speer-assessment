package com.speer.technologies.data.user.datasource

import com.speer.technologies.domain.user.model.User

interface UserRemoteDatasource {

    suspend fun getUserByUserName(username: String): User?
}
