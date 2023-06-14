package com.speer.technologies.view.impl.common.user.mapper

import com.speer.technologies.domain.user.model.User
import com.speer.technologies.domain.user.model.UserInfo
import com.speer.technologies.view.impl.common.user.model.ParcelableUser

class ParcelableUserToUserMapper {

    fun map(parcelableUser: ParcelableUser): User = parcelableUser.run {

        val additionalInfo = if (
            name.isNullOrEmpty() &&
            description.isNullOrEmpty() &&
            followersCount == null &&
            followingCount == null
        ) {
            null
        } else {
            UserInfo(
                name = name,
                description = description,
                followersCount = followersCount,
                followingCount = followingCount,
            )
        }

        User(
            id = id,
            avatarUrl = avatarUrl,
            username = username,
            additionalInfo = additionalInfo,
        )
    }
}
