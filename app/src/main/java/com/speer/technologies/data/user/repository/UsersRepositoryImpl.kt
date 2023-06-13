package com.speer.technologies.data.user.repository

import com.speer.technologies.data.user.datasource.UsersRemoteDatasource
import com.speer.technologies.domain.users.model.User
import com.speer.technologies.domain.users.repository.UsersRepository

class UsersRepositoryImpl(
    private val usersRemoteDatasource: UsersRemoteDatasource,
) : UsersRepository {

    override suspend fun findUsersByName(name: String): List<User> =
        usersRemoteDatasource.findUsersByName(name)
}
