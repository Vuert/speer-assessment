package com.speer.technologies.domain.user.model

data class User(
    val id: String,
    val avatarUrl: String?,
    val username: String,
    val additionalInfo: UserInfo?,
)
