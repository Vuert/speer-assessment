package com.speer.technologies.datasource.user.local.mapper

import com.speer.technologies.datasource.user.local.model.UserDbo
import com.speer.technologies.domain.user.model.User

class UserToUserDboMapper {

    fun map(user: User): UserDbo = user.run {
        UserDbo(
            id = id,
            avatarUrl = avatarUrl,
            username = username,
            name = additionalInfo?.name,
            description = additionalInfo?.description,
            followersCount = additionalInfo?.followersCount,
            followingCount = additionalInfo?.followingCount,
        )
    }
}
