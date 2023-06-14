package com.speer.technologies.datasource.user.model

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("login")
    val login: String,

    @SerializedName("id")
    val id: Int,

    @SerializedName("avatar_url")
    val avatarUrl: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("bio")
    val bio: String? = null,

    @SerializedName("followers")
    val followers: Int?,

    @SerializedName("following")
    val following: Int?,
)
