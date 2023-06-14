package com.speer.technologies.datasource.user.api

import com.speer.technologies.datasource.user.model.UserDto
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {

    @GET("/users/{username}")
    suspend fun findUser(@Path("username") username: String): UserDto
}
