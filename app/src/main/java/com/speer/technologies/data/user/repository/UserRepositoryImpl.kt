package com.speer.technologies.data.user.repository

import com.speer.technologies.data.user.datasource.UserRemoteDatasource
import com.speer.technologies.domain.user.model.User
import com.speer.technologies.domain.user.repository.UserRepository

class UserRepositoryImpl(
    private val userRemoteDatasource: UserRemoteDatasource,
) : UserRepository {

    override suspend fun getUserByUserName(username: String): User? =
        userRemoteDatasource.getUserByUserName(username)

    override suspend fun getFollowers(user: User): List<User> =
        userRemoteDatasource.getFollowers(user)

    override suspend fun getFollowing(user: User): List<User> =
        userRemoteDatasource.getFollowing(user)
}
