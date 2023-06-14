package com.speer.technologies.datasource.users.datasource

import com.speer.technologies.data.user.datasource.UserRemoteDatasource
import com.speer.technologies.domain.user.model.User

class UserRemoteDatasourceImpl : UserRemoteDatasource {

    override suspend fun getUserByUserName(username: String): User =
        User(
            id = "id",
            avatarUrl = "url",
            username = "Username",
            name = "name",
            description = "description",
            followersCount = 1,
            followingsCount = 3,
        )
}
