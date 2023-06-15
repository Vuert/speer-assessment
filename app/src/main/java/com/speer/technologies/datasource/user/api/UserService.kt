package com.speer.technologies.datasource.user.api

import com.speer.technologies.datasource.user.model.UserDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {

    @GET("/users/{username}")
    suspend fun findUser(@Path("username") username: String): UserDto

    @GET("users/{username}/following")
    suspend fun fetchFollowing(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int,
    ): List<UserDto>

    @GET("/users/{username}/followers")
    suspend fun fetchFollowers(
        @Path("username") username: String,
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int,
    ): List<UserDto>
}
