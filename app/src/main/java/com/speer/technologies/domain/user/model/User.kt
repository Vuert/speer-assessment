package com.speer.technologies.domain.user.model

data class User(
    val id: String,
    val avatarUrl: String?,
    val username: String,
    val additionalInfo: UserInfo?,
) {

    override fun equals(other: Any?): Boolean =
        other is User && other.id == this.id

    override fun hashCode(): Int = id.hashCode()
}
