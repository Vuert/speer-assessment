package com.speer.technologies.datasource.user.remote.datasource

import com.speer.technologies.data.user.datasource.UserRemoteDatasource
import com.speer.technologies.datasource.user.remote.service.UserService
import com.speer.technologies.datasource.user.remote.mapper.UserDtoToUserMapper
import com.speer.technologies.domain.common.exception.NoNetworkException
import com.speer.technologies.domain.user.model.User
import retrofit2.HttpException
import java.net.HttpURLConnection
import java.net.UnknownHostException

class UserRemoteDatasourceImpl(
    private val userService: UserService,
) : UserRemoteDatasource {

    override suspend fun getUserByUserName(username: String): User? = wrap {
        try {
            userService
                .findUser(username)
                .let(UserDtoToUserMapper()::map)
        } catch (ex: HttpException) {
            if (ex.code() == HttpURLConnection.HTTP_NOT_FOUND) null else throw ex
        }
    }

    override suspend fun getFollowers(user: User, page: Int, pageSize: Int): List<User> = wrap {
        val dtoFollowers = userService.fetchFollowers(user.username, page, pageSize)
        val mapper = UserDtoToUserMapper()
        return dtoFollowers.map(mapper::map)
    }

    override suspend fun getFollowing(user: User, page: Int, pageSize: Int): List<User> = wrap {
        val dtoFollowing = userService.fetchFollowing(user.username, page, pageSize)
        val mapper = UserDtoToUserMapper()
        return dtoFollowing.map(mapper::map)
    }

    private inline fun <T> wrap(block: () -> T): T =
        try {
            block.invoke()
        } catch (_: UnknownHostException) {
            throw NoNetworkException()
        }
}
