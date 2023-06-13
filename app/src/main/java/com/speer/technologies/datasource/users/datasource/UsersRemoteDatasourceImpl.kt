package com.speer.technologies.datasource.users.datasource

import com.speer.technologies.data.user.datasource.UsersRemoteDatasource
import com.speer.technologies.domain.users.model.User

class UsersRemoteDatasourceImpl : UsersRemoteDatasource {

    override suspend fun findUsersByName(name: String): List<User> {
        // Mock users
        return buildList {
            repeat(30) {
                add(
                    User(
                        id = it.toString(),
                        avatarUrl = "url",
                        username = "Username $it",
                        name = "Username $it",
                        description = "Username $it",
                        followersCount = it,
                        followingsCount = it,
                    )
                )
            }
        }
    }
}
