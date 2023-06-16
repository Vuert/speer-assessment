package com.speer.technologies.datasource.user.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["user_name"], unique = true)],
)
data class UserDbo(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String?,

    @ColumnInfo(name = "user_name", collate = ColumnInfo.NOCASE)
    val username: String,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "followers_amount")
    val followersCount: Int?,

    @ColumnInfo(name = "following_amount")
    val followingCount: Int?,
)
