package com.speer.technologies.datasource.user.datasource

import com.speer.technologies.data.user.datasource.UserRemoteDatasource
import com.speer.technologies.datasource.user.api.UserService
import com.speer.technologies.datasource.user.mapper.UserDtoToUserMapper
import com.speer.technologies.domain.user.model.User
import retrofit2.HttpException
import java.net.HttpURLConnection

class UserRemoteDatasourceImpl(
    private val userService: UserService,
) : UserRemoteDatasource {

    override suspend fun getUserByUserName(username: String): User? =
        try {
            userService
                .findUser(username)
                .let(UserDtoToUserMapper()::map)
        } catch (ex: HttpException) {
            if (ex.code() == HttpURLConnection.HTTP_NOT_FOUND) null else throw ex
        }

    override suspend fun getFollowers(user: User, page: Int, pageSize: Int): List<User> {
        val dtoFollowers = userService.fetchFollowers(user.username, page, pageSize)
        val mapper = UserDtoToUserMapper()
        return dtoFollowers.map(mapper::map)
    }

    override suspend fun getFollowing(user: User, page: Int, pageSize: Int): List<User> {
        val dtoFollowing = userService.fetchFollowing(user.username, page, pageSize)
        val mapper = UserDtoToUserMapper()
        return dtoFollowing.map(mapper::map)
    }
}
