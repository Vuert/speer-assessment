package com.speer.technologies.datasource.user.local.mapper

import com.speer.technologies.datasource.user.local.model.UserDbo
import com.speer.technologies.domain.user.model.User
import com.speer.technologies.domain.user.model.UserInfo

class UserDboToUserMapper {

    fun map(userDbo: UserDbo): User = userDbo.run {
        val additionalInfo = UserInfo(
            name = name,
            description = description,
            followersCount = followersCount,
            followingCount = followingCount,
        )

        User(
            id = id,
            avatarUrl = avatarUrl,
            username = username,
            additionalInfo = additionalInfo,
        )
    }
}
