package com.speer.technologies.domain.user.repository

import com.speer.technologies.domain.user.model.User

interface UserRepository {

    suspend fun getUserByUserName(username: String): User?

    suspend fun getFollowers(user: User, page: Int, pageSize: Int): List<User>

    suspend fun getFollowing(user: User, page: Int, pageSize: Int): List<User>
}
