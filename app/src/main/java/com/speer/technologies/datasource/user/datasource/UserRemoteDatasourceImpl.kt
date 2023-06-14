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
}
