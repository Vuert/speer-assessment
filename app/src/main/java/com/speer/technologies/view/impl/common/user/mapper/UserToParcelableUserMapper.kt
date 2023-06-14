package com.speer.technologies.view.impl.common.user.mapper

import com.speer.technologies.domain.user.model.User
import com.speer.technologies.view.impl.common.user.model.ParcelableUser

class UserToParcelableUserMapper {

    fun map(user: User): ParcelableUser = user.run {
        ParcelableUser(
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
