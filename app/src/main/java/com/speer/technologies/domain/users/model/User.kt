package com.speer.technologies.domain.users.model

data class User(
    val id: String,
    val avatarUrl: String,
    val username: String,
    val name: String,
    val description: String,
    val followersCount: Int,
    val followingsCount: Int,
) {

    override fun equals(other: Any?): Boolean =
        other is User && other.id == this.id

    override fun hashCode(): Int = id.hashCode()
}
